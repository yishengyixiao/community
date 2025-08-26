package com.cys.controller;

import com.cys.mapper.UserMapper;
import com.cys.model.User; // 引入我们自己的 User 模型
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse; // 退出登录需要用到 Response
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        // 1. 先尝试从 session 中获取 user 对象
        User user = (User) request.getSession().getAttribute("user");

        // 2. 如果 session 中没有，再尝试通过 cookie 从数据库中获取
        if (user == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null && cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        String token = cookie.getValue();
                        user = userMapper.findByToken(token); // 从数据库查找
                        if (user != null) {
                            // 如果找到了，就放入 session，完成自动登录
                            request.getSession().setAttribute("user", user);
                        }
                        break;
                    }
                }
            }
        }

        // 3. 此时，user 要么是从 session 或 cookie 中成功获取的对象，要么是 null
        if (user != null) {
            model.addAttribute("user", user);
        }

        // 这两行保持不变，用于渲染“登录”按钮
        model.addAttribute("githubClientId", clientId);
        model.addAttribute("githubRedirectUri", redirectUri);

        return "index";
    }

    // 【重要修正】完善退出登录功能
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // 1. 从服务器清除 session
        request.getSession().removeAttribute("user");

        // 2. 从浏览器清除 cookie
        Cookie cookie = new Cookie("token", null); // 创建一个同名但值为空的 cookie
        cookie.setMaxAge(0); // 设置有效期为0，命令浏览器立即删除
        cookie.setPath("/"); // 确保路径与创建时一致
        response.addCookie(cookie);

        // 3. 重定向回首页
        return "redirect:/";
    }
}