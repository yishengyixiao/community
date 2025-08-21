package com.cys.controller;

import org.springframework.stereotype.Controller; // 1. 必须使用 @Controller 注解
import org.springframework.ui.Model; // 2. 必须导入正确的 Model 类
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller // 3. 加上这个注解，Spring Boot 才知道它是一个控制器
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
}