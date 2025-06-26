package com.example.BookManager.dto;

import com.example.BookManager.entity.Loan;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LoanDTO {
    private long id;
    private BookDTO book;
    private String borrowerName;
    private String borrowerEmail;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status; // Changed from enum to String for better Thymeleaf compatibility
    
    // For form binding
    private Long bookId;

    public Loan toEntity() {
        Loan loan = new Loan();
        loan.setId(this.id);
        loan.setBorrowerName(this.borrowerName);
        loan.setBorrowerEmail(this.borrowerEmail);
        loan.setIssueDate(this.issueDate);
        loan.setDueDate(this.dueDate);
        loan.setReturnDate(this.returnDate);
        
        // Convert String status to enum
        if (this.status != null) {
            try {
                loan.setStatus(Loan.LoanStatus.valueOf(this.status));
            } catch (IllegalArgumentException e) {
                loan.setStatus(Loan.LoanStatus.ACTIVE); // Default to ACTIVE
            }
        } else {
            loan.setStatus(Loan.LoanStatus.ACTIVE);
        }
        
        if (this.book != null) {
            loan.setBook(this.book.toEntity());
        }
        
        return loan;
    }

    public static LoanDTO fromEntity(Loan loan) {
        if (loan == null) {
            return null;
        }

        return LoanDTO.builder()
                .id(loan.getId())
                .book(BookDTO.fromEntity(loan.getBook()))
                .borrowerName(loan.getBorrowerName())
                .borrowerEmail(loan.getBorrowerEmail())
                .issueDate(loan.getIssueDate())
                .dueDate(loan.getDueDate())
                .returnDate(loan.getReturnDate())
                .status(loan.getStatus() != null ? loan.getStatus().name() : null)
                .build();
    }

    // Helper method to check if loan is overdue
    public boolean isOverdue() {
        return "ACTIVE".equals(status) && LocalDate.now().isAfter(dueDate);
    }

    // Helper method to get status display text
    public String getStatusDisplay() {
        if (status == null) return "Unknown";
        switch (status) {
            case "ACTIVE":
                return isOverdue() ? "Overdue" : "Active";
            case "RETURNED":
                return "Returned";
            case "OVERDUE":
                return "Overdue";
            default:
                return "Unknown";
        }
    }
} 