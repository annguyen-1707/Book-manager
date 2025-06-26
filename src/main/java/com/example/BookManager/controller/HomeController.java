package com.example.BookManager.controller;

import com.example.BookManager.service.AuthorService;
import com.example.BookManager.service.BookService;
import com.example.BookManager.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    
    private final BookService bookService;
    private final AuthorService authorService;
    private final LoanService loanService;
    
    @GetMapping("/")
    public String home(Model model) {
        // Add some basic stats to the home page
        model.addAttribute("totalBooks", bookService.getAllBooks().size());
        model.addAttribute("totalAuthors", authorService.getAllAuthors().size());
        model.addAttribute("totalLoans", loanService.getAllLoans().size());
        return "index";
    }
} 