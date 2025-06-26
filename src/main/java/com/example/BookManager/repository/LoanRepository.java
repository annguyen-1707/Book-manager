package com.example.BookManager.repository;

import com.example.BookManager.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    
    // Find loans by book ID
    List<Loan> findByBookId(Long bookId);
    
    // Find active loans
    List<Loan> findByStatus(Loan.LoanStatus status);
    
    // Find loans by borrower name
    List<Loan> findByBorrowerNameContainingIgnoreCase(String borrowerName);
    
    // Find loans by borrower email
    List<Loan> findByBorrowerEmail(String borrowerEmail);
    
    // Find overdue loans
    @Query("SELECT l FROM Loan l WHERE l.status = 'ACTIVE' AND l.dueDate < :today")
    List<Loan> findOverdueLoans(@Param("today") LocalDate today);
    
    // Find loans due today
    @Query("SELECT l FROM Loan l WHERE l.status = 'ACTIVE' AND l.dueDate = :today")
    List<Loan> findLoansDueToday(@Param("today") LocalDate today);
    
    // Find loans due within next X days
    @Query("SELECT l FROM Loan l WHERE l.status = 'ACTIVE' AND l.dueDate BETWEEN :today AND :futureDate")
    List<Loan> findLoansDueSoon(@Param("today") LocalDate today, @Param("futureDate") LocalDate futureDate);
    
    // Check if a book is currently borrowed
    @Query("SELECT COUNT(l) > 0 FROM Loan l WHERE l.book.id = :bookId AND l.status = 'ACTIVE'")
    boolean isBookCurrentlyBorrowed(@Param("bookId") Long bookId);
} 