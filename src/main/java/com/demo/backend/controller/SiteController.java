package com.demo.backend.controller;

import com.demo.backend.config.aop.Log;
import com.demo.backend.service.SiteService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping("/site")
@RestController
public class SiteController {

    @Resource
    private SiteService siteService;

    @Log("get请求")
    @GetMapping("/login")
    public String login(
            @RequestParam(value = "username", required = false, defaultValue = "Pig") String userName){

        return "Hello , " + userName;
    }

    @Log(value = "post请求登录")
    @PostMapping("/login")
    public String Login(
            @RequestParam(value = "username") String userName,
            @RequestParam(value = "password") String password){
        return "userName, password, ----------This just one example , combine your own business to implement logic！";
    }

}
