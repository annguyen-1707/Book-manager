package com.example.BookManager.controller;

import com.example.BookManager.dto.LoanDTO;
import com.example.BookManager.service.BookService;
import com.example.BookManager.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/loans")
public class LoanController {
    
    private final LoanService loanService;
    private final BookService bookService;
    
    @GetMapping
    public String getAllLoans(Model model) {
        try {
            List<LoanDTO> loans = loanService.getAllLoans();
            model.addAttribute("loans", loans);
            return "loans";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error loading loans: " + e.getMessage());
            return "loans";
        }
    }
    
    @GetMapping("/form")
    public String showLoanForm(Model model) {
        try {
            // Get only available books (not currently borrowed)
            List<LoanDTO> availableBooks = bookService.getAllBooks().stream()
                    .filter(book -> loanService.isBookAvailable(book.getId()))
                    .map(book -> {
                        LoanDTO loanDTO = new LoanDTO();
                        loanDTO.setBook(book);
                        loanDTO.setBookId(book.getId());
                        return loanDTO;
                    })
                    .toList();
            
            model.addAttribute("loan", new LoanDTO());
            model.addAttribute("availableBooks", availableBooks);
            model.addAttribute("today", LocalDate.now());
            model.addAttribute("defaultDueDate", LocalDate.now().plusDays(14));
            return "loan-form";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error loading form: " + e.getMessage());
            return "loan-form";
        }
    }
    
    @PostMapping
    public String createLoan(@ModelAttribute("loan") LoanDTO loanDTO, 
                            RedirectAttributes redirectAttributes) {
        try {
            LoanDTO savedLoan = loanService.createLoan(loanDTO);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Book '" + savedLoan.getBook().getName() + "' borrowed successfully by " + savedLoan.getBorrowerName());
            return "redirect:/loans";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error creating loan: " + e.getMessage());
            return "redirect:/loans/form";
        }
    }
    
    @PostMapping("/{id}/return")
    public String returnBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            LoanDTO returnedLoan = loanService.returnBook(id);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Book '" + returnedLoan.getBook().getName() + "' returned successfully");
            return "redirect:/loans";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error returning book: " + e.getMessage());
            return "redirect:/loans";
        }
    }
} 