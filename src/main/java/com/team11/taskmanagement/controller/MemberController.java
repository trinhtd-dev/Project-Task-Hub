package com.team11.taskmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {
    @GetMapping("/members")
    public String members(Model model) {
        model.addAttribute("currentUrl", "/members");
        return "members/index";
    }
}
