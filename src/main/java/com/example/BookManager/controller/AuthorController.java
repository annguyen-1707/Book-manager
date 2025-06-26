package com.example.BookManager.controller;

import com.example.BookManager.dto.AuthorDTO;
import com.example.BookManager.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/authors")
public class AuthorController {
    
    private final AuthorService authorService;
    
    @GetMapping
    public String getAllAuthors(Model model) {
        model.addAttribute("authors", authorService.getAllAuthors());
        return "authors";
    }
    
    @GetMapping("/form")
    public String showAuthorForm(Model model) {
        model.addAttribute("author", new AuthorDTO());
        return "author-form";
    }
    
    @PostMapping
    public String createAuthor(@ModelAttribute("author") AuthorDTO authorDTO, 
                              RedirectAttributes redirectAttributes) {
        try {
            AuthorDTO savedAuthor = authorService.createAuthor(authorDTO);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Author '" + savedAuthor.getName() + "' created successfully!");
            return "redirect:/authors";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error creating author: " + e.getMessage());
            return "redirect:/authors/form";
        }
    }
} 