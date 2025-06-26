package com.example.BookManager.controller;

import com.example.BookManager.entity.Book;
import org.springframework.ui.Model;
import com.example.BookManager.dto.BookDTO;
import com.example.BookManager.dto.AuthorDTO;
import com.example.BookManager.service.AuthorService;
import com.example.BookManager.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final AuthorService authorService;
    
    @GetMapping
    public String getAllBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books";
    }
    
    @GetMapping("/form")
    public String showBookForm(Model model) {
        model.addAttribute("book", new BookDTO());
        model.addAttribute("authors", authorService.getAllAuthors());
        return "book-form";
    }

    @PostMapping
    public String saveBook(@ModelAttribute("book") BookDTO bookDTO, 
                          RedirectAttributes redirectAttributes) {
        try {
            // Convert author IDs to AuthorDTO objects
            if (bookDTO.getAuthorIds() != null && !bookDTO.getAuthorIds().isEmpty()) {
                Set<AuthorDTO> authorDTOs = new HashSet<>();
                for (Long authorId : bookDTO.getAuthorIds()) {
                    authorService.getAuthorById(authorId).ifPresent(authorDTOs::add);
                }
                bookDTO.setAuthors(authorDTOs);
            }
            
            BookDTO savedBook = bookService.createBook(bookDTO);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Book '" + savedBook.getName() + "' saved successfully!");
            return "redirect:/books";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error saving book: " + e.getMessage());
            return "redirect:/books/form";
        }
    }
}
