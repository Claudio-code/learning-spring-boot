package com.learning.spring.library.service.implementation;

import com.learning.spring.library.api.model.entity.Book;
import com.learning.spring.library.api.model.repository.BookRepository;
import com.learning.spring.library.exception.IsbnAlreadyUsedByAnotherBookException;
import com.learning.spring.library.service.BookService;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new IsbnAlreadyUsedByAnotherBookException();
        }
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> getById(Long id) {
        var book = bookRepository.getById(id);
        return Optional.of(book);
    }
}
