package com.example.BookManager.service;

import com.example.BookManager.dto.LoanDTO;
import com.example.BookManager.entity.Book;
import com.example.BookManager.entity.Loan;
import com.example.BookManager.repository.BookRepository;
import com.example.BookManager.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanService {
    
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;
    
    public List<LoanDTO> getAllLoans() {
        return loanRepository.findAll().stream()
                .map(LoanDTO::fromEntity)
                .toList();
    }
    
    public Optional<LoanDTO> getLoanById(Long id) {
        return loanRepository.findById(id)
                .map(LoanDTO::fromEntity);
    }
    
    public LoanDTO createLoan(LoanDTO loanDTO) {
        // Validate that the book exists and is available
        if (loanDTO.getBookId() == null) {
            throw new IllegalArgumentException("Book ID is required");
        }
        
        Optional<Book> bookOpt = bookRepository.findById(loanDTO.getBookId());
        if (bookOpt.isEmpty()) {
            throw new IllegalArgumentException("Book not found with ID: " + loanDTO.getBookId());
        }
        
        // Check if book is currently borrowed
        if (loanRepository.isBookCurrentlyBorrowed(loanDTO.getBookId())) {
            throw new IllegalArgumentException("Book is currently borrowed and not available");
        }
        
        Book book = bookOpt.get();
        Loan loan = loanDTO.toEntity();
        loan.setBook(book);
        
        // Set default values if not provided
        if (loan.getIssueDate() == null) {
            loan.setIssueDate(LocalDate.now());
        }
        if (loan.getDueDate() == null) {
            loan.setDueDate(LocalDate.now().plusDays(14)); // Default 2 weeks
        }
        if (loan.getStatus() == null) {
            loan.setStatus(Loan.LoanStatus.ACTIVE);
        }
        
        Loan savedLoan = loanRepository.save(loan);
        return LoanDTO.fromEntity(savedLoan);
    }
    
    public Optional<LoanDTO> updateLoan(Long id, LoanDTO loanDTO) {
        return loanRepository.findById(id)
                .map(existingLoan -> {
                    loanDTO.setId(id);
                    Loan updatedLoan = loanDTO.toEntity();
                    
                    // Preserve the book relationship
                    if (updatedLoan.getBook() == null) {
                        updatedLoan.setBook(existingLoan.getBook());
                    }
                    
                    Loan savedLoan = loanRepository.save(updatedLoan);
                    return LoanDTO.fromEntity(savedLoan);
                });
    }
    
    public boolean deleteLoan(Long id) {
        if (loanRepository.existsById(id)) {
            loanRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public LoanDTO returnBook(Long loanId) {
        Optional<Loan> loanOpt = loanRepository.findById(loanId);
        if (loanOpt.isPresent()) {
            Loan loan = loanOpt.get();
            loan.returnBook();
            Loan savedLoan = loanRepository.save(loan);
            return LoanDTO.fromEntity(savedLoan);
        }
        throw new IllegalArgumentException("Loan not found with ID: " + loanId);
    }
    
    public List<LoanDTO> getActiveLoans() {
        return loanRepository.findByStatus(Loan.LoanStatus.ACTIVE).stream()
                .map(LoanDTO::fromEntity)
                .toList();
    }
    
    public List<LoanDTO> getOverdueLoans() {
        return loanRepository.findOverdueLoans(LocalDate.now()).stream()
                .map(LoanDTO::fromEntity)
                .toList();
    }
    
    public List<LoanDTO> getLoansByBook(Long bookId) {
        return loanRepository.findByBookId(bookId).stream()
                .map(LoanDTO::fromEntity)
                .toList();
    }
    
    public List<LoanDTO> getLoansByBorrower(String borrowerName) {
        return loanRepository.findByBorrowerNameContainingIgnoreCase(borrowerName).stream()
                .map(LoanDTO::fromEntity)
                .toList();
    }
    
    public boolean isBookAvailable(Long bookId) {
        return !loanRepository.isBookCurrentlyBorrowed(bookId);
    }
    
    public List<LoanDTO> getLoansDueSoon(int days) {
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(days);
        return loanRepository.findLoansDueSoon(today, futureDate).stream()
                .map(LoanDTO::fromEntity)
                .toList();
    }
    
    public void updateOverdueStatus() {
        List<Loan> overdueLoans = loanRepository.findOverdueLoans(LocalDate.now());
        for (Loan loan : overdueLoans) {
            loan.setStatus(Loan.LoanStatus.OVERDUE);
            loanRepository.save(loan);
        }
    }
} 