package com.cys.controller;

import org.springframework.stereotype.Controller; // 1. 必须使用 @Controller 注解
import org.springframework.ui.Model; // 2. 必须导入正确的 Model 类
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller // 3. 加上这个注解，Spring Boot 才知道它是一个控制器
public class HelloController {

    @GetMapping("/hello")
    public String hello(@RequestParam(name = "name") String name, Model model) {
        // 4. 使用 addAttribute 方法，将数据存入 model
        //    第一个参数 "name" 是键，第二个参数 name 是值
        model.addAttribute("name", name);

        // 5. 返回模板文件的名字（注意：不带 .html 后缀）
        return "hello";
    }
}