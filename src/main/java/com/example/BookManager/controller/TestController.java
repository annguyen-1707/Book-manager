package com.example.BookManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    
    @GetMapping("/test")
    public String test(Model model) {
        model.addAttribute("message", "Test page is working!");
        return "test";
    }
    
    @GetMapping("/test-loans")
    public String testLoans(Model model) {
        model.addAttribute("message", "Loans test page");
        model.addAttribute("loans", java.util.Collections.emptyList());
        return "loans";
    }
} 