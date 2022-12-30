package com.github.vladbahlai.university.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthController {

    @RequestMapping(value = "/auth/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String loginPage() {
        return "auth/login";
    }

    @RequestMapping("/accessDenied")
    public String accessDeniedPage() {
        return "auth/accessDenied";
    }

}
