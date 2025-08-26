package com.cys.controller;

import com.cys.dto.AccessTokenDTO;
import com.cys.dto.GithubUser;
import com.cys.mapper.UserMapper;
import com.cys.model.User; // 引入我们自己的 User 模型
import com.cys.provider.GithubProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse; // 必须是 Response
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Controller
public class AuthorizeController {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizeController.class);

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code", required = false) String code,
                           @RequestParam(name="state") String state,
                           HttpServletResponse response, // 确保是 Response
                           HttpSession session) {

        if (code == null || code.trim().isEmpty()) {
            logger.warn("GitHub OAuth 回调失败，code 为空。");
            return "redirect:/";
        }

        try {
            // ... 获取 accessToken 和 githubUser 的逻辑不变 ...
            AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
            accessTokenDTO.setClient_id(clientId);
            accessTokenDTO.setClient_secret(clientSecret);
            accessTokenDTO.setCode(code);
            accessTokenDTO.setRedirect_uri(redirectUri);
            accessTokenDTO.setState(state);
            String accessToken = githubProvider.getAccessToken(accessTokenDTO);

            if (accessToken == null) {
                logger.error("获取 access_token 失败");
                return "redirect:/";
            }

            GithubUser githubUser = githubProvider.getUser(accessToken);
            if (githubUser == null) {
                logger.error("获取 GitHub 用户信息失败");
                return "redirect:/";
            }

            // --- 核心修改在这里 ---
            // 1. 创建我们自己数据库对应的 User 对象
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName()); // 直接用 name
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());

            // 2. 将所有前端需要的信息，从 githubUser 完整地复制到我们自己的 user 对象中
            user.setAvatarUrl(githubUser.getAvatar_url());
            user.setBio(githubUser.getBio());
            user.setPublic_repos(githubUser.getPublic_repos());
            user.setFollowers(githubUser.getFollowers());
            user.setFollowing(githubUser.getFollowing());
            user.setHtml_url(githubUser.getHtml_url());

            userMapper.insert(user);

            Cookie cookie = new Cookie("token", token);
            // 设置有效期为 7 天 (单位是秒)
            cookie.setMaxAge(60 * 60 * 24 * 7);
            // 设置 cookie 的作用域为整个网站
            cookie.setPath("/");
            response.addCookie(cookie);

            session.setAttribute("user", user);

            return "redirect:/";

        } catch (Exception e) {
            logger.error("回调处理时发生未知错误", e);
            return "redirect:/";
        }
    }
}