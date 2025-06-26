package com.example.BookManager.dto;

import com.example.BookManager.entity.Author;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthorDTO {
    private long id;
    private String name;
    private LocalDate dateOfBirth;
    private String address;

    public Author toEntity() {
        Author author = new Author();
        author.setId(this.id);
        author.setName(this.name);
        author.setAddress(this.address);
        author.setDateOfBirth(this.dateOfBirth);
        return author;
    }

    public static AuthorDTO fromEntity(Author author) {
        if (author == null) {
            return null;
        }

        return AuthorDTO.builder()
                .id(author.getId())
                .name(author.getName())
                .dateOfBirth(author.getDateOfBirth())
                .address(author.getAddress())
                .build();
    }
}
