package com.cys.controller;

import com.cys.dto.AccessTokenDTO;
import com.cys.dto.GithubUser;
import com.cys.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AuthorizeController {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizeController.class);

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}") // 新增注入
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpSession session) { // 新增HttpSession参数
        try {
            logger.info("OAuth 回调开始, code: {}, state: {}", code, state);

            AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
            accessTokenDTO.setClient_id(clientId);
            accessTokenDTO.setClient_secret(clientSecret);
            accessTokenDTO.setCode(code);
            accessTokenDTO.setRedirect_uri(redirectUri); // 修改：使用注入的redirectUri
            accessTokenDTO.setState(state);

            String accessToken = githubProvider.getAccessToken(accessTokenDTO);
            if (accessToken == null) {
                System.err.println("❌ 获取访问令牌失败");
                return "error";
            }
            System.out.println("✅ 成功获取访问令牌");

            GithubUser user = githubProvider.getUser(accessToken);
            if (user == null) {
                System.err.println("❌ 获取用户信息失败");
                return "error";
            }
            System.out.println("✅ 成功获取用户信息");

            // 存储用户到session（新增）
            session.setAttribute("user", user);

            // 详细打印用户信息（原有）
            System.out.println("=== 最终用户信息 ===");
            String userName = user.getName();
            String userLogin = user.getLogin();

            if (userName != null && !userName.trim().isEmpty()) {
                System.out.printf("用户显示名称: %s%n", userName);
            } else {
                System.out.println("⚠️ 用户显示名称为空");
                if (userLogin != null) {
                    System.out.printf("使用GitHub用户名代替: %s%n", userLogin);
                }
            }

            System.out.println("完整用户对象: " + user.toString());

            return "index"; // 或 "redirect:/" 以重定向首页
        } catch (Exception e) {
            System.err.println("❌ 回调处理错误: " + e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }
}