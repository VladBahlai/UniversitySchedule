package com.github.vladbahlai.university.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getAdminPanel() {
        return "admin/menu";
    }
}
