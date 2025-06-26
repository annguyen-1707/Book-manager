package com.example.BookManager.service;

import com.example.BookManager.dto.AuthorDTO;
import com.example.BookManager.dto.BookDTO;
import com.example.BookManager.entity.Author;
import com.example.BookManager.entity.Book;
import com.example.BookManager.repository.AuthorRepository;
import com.example.BookManager.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final AuthorRepository authorRepository;

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookDTO::fromEntity)
                .toList();
    }

    public Optional<BookDTO> getBookById(Long id) {
        return bookRepository.findById(id)
                .map(BookDTO::fromEntity);
    }

    public BookDTO createBook(BookDTO bookDTO) {
        Book book = bookDTO.toEntity();
        Book savedBook = bookRepository.save(book);
        return BookDTO.fromEntity(savedBook);
    }

    public Optional<BookDTO> updateBook(Long id, BookDTO bookDTO) {
        return bookRepository.findById(id)
                .map(existingBook -> {
                    bookDTO.setId(id);
                    Book updatedBook = bookDTO.toEntity();
                    Book savedBook = bookRepository.save(updatedBook);
                    return BookDTO.fromEntity(savedBook);
                });
    }

    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<BookDTO> getBooksByAuthor(Long authorId) {
        return bookRepository.findByAuthorsId(authorId).stream()
                .map(BookDTO::fromEntity)
                .toList();
    }

    public BookDTO addAuthorToBook(Long bookId, Long authorId) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        Optional<Author> authorOpt = authorRepository.findById(authorId);
        
        if (bookOpt.isPresent() && authorOpt.isPresent()) {
            Book book = bookOpt.get();
            Author author = authorOpt.get();
            book.getAuthors().add(author);
            Book savedBook = bookRepository.save(book);
            return BookDTO.fromEntity(savedBook);
        }
        return null;
    }

    public BookDTO removeAuthorFromBook(Long bookId, Long authorId) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            book.getAuthors().removeIf(author -> author.getId() == authorId);
            Book savedBook = bookRepository.save(book);
            return BookDTO.fromEntity(savedBook);
        }
        return null;
    }
}
