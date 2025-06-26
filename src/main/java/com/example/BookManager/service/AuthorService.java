package com.example.BookManager.service;

import com.example.BookManager.dto.AuthorDTO;
import com.example.BookManager.entity.Author;
import com.example.BookManager.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(AuthorDTO::fromEntity)
                .toList();
    }

    public Optional<AuthorDTO> getAuthorById(Long id) {
        return authorRepository.findById(id)
                .map(AuthorDTO::fromEntity);
    }

    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Author author = authorDTO.toEntity();
        Author savedAuthor = authorRepository.save(author);
        return AuthorDTO.fromEntity(savedAuthor);
    }

    public Optional<AuthorDTO> updateAuthor(Long id, AuthorDTO authorDTO) {
        return authorRepository.findById(id)
                .map(existingAuthor -> {
                    authorDTO.setId(id);
                    Author updatedAuthor = authorDTO.toEntity();
                    Author savedAuthor = authorRepository.save(updatedAuthor);
                    return AuthorDTO.fromEntity(savedAuthor);
                });
    }

    public boolean deleteAuthor(Long id) {
        if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Legacy method for backward compatibility
    @Deprecated
    public AuthorDTO authorToAuthorDTO(Author author) {
        return AuthorDTO.fromEntity(author);
    }
}
