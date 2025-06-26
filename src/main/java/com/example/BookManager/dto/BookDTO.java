package com.example.BookManager.dto;

import com.example.BookManager.entity.Author;
import com.example.BookManager.entity.Book;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BookDTO {
    private long id;
    private String name;
    private String isBn;
    private LocalDate year;
    private int reprint;
    private Set<AuthorDTO> authors;
    
    // For form binding - stores author IDs
    private Set<Long> authorIds;

    public Book toEntity() {
        Book book = new Book();
        book.setId(this.id);
        book.setName(this.name);
        book.setIsBn(this.isBn);
        book.setReprint(this.reprint);
        book.setYear(this.year);

        Set<Author> authorSet = new HashSet<>();
        if (this.authors != null) {
            for (AuthorDTO dto : this.authors) {
                authorSet.add(dto.toEntity());
            }
        }
        book.setAuthors(authorSet);
        return book;
    }

    public static BookDTO fromEntity(Book book) {
        if (book == null) {
            return null;
        }

        Set<AuthorDTO> authorDTOs = new HashSet<>();
        if (book.getAuthors() != null) {
            authorDTOs = book.getAuthors().stream()
                    .map(AuthorDTO::fromEntity)
                    .collect(Collectors.toSet());
        }

        return BookDTO.builder()
                .id(book.getId())
                .name(book.getName())
                .isBn(book.getIsBn())
                .year(book.getYear())
                .reprint(book.getReprint())
                .authors(authorDTOs)
                .build();
    }
}

