package com.cys.controller;

import com.cys.dto.GithubUser;
import org.springframework.beans.factory.annotation.Value; // 新增导入
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class IndexController {

    @Value("${github.client.id}") // 新增注入
    private String clientId;

    @Value("${github.redirect.uri}") // 新增注入
    private String redirectUri;

    @GetMapping("/")
    public String index(HttpSession session, Model model) { // 新增HttpSession和Model参数
        GithubUser user = (GithubUser) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user); // 传入用户到模板
        }
        model.addAttribute("githubClientId", clientId); // 传入clientId到模板
        model.addAttribute("githubRedirectUri", redirectUri); // 传入redirectUri到模板
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 或 session.removeAttribute("user");
        return "redirect:/";
    }
}