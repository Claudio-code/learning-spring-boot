package com.learning.spring.library.service.implementation;

import com.learning.spring.library.api.model.entity.Book;
import com.learning.spring.library.api.model.repository.BookRepository;
import com.learning.spring.library.exception.IsbnAlreadyUsedByAnotherBookException;
import com.learning.spring.library.service.BookService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {
//        if (bookRepository.existsByIsbn(book.getIsbn())) {
//            throw new IsbnAlreadyUsedByAnotherBookException();
//        }
        return bookRepository.save(book);
    }

    @Override
    @Cacheable(cacheNames = Book.CACHE_NAME, key = "#id")
    public Book getById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    @CacheEvict(cacheNames = Book.CACHE_NAME, key = "#book.id")
    public void delete(Book book) {
        book.validFields();
        bookRepository.delete(book);
    }

    @Override
    @CachePut(cacheNames = Book.CACHE_NAME, key = "#book.id")
    public Book update(Book book) {
        book.validFields();
        return bookRepository.save(book);
    }

    @Override
    public PageImpl<Book> find(Book filter, Pageable pageable) {
        Example<Book> example = Example.of(filter,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
        );
        return (PageImpl<Book>) bookRepository.findAll(example, pageable);
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        return bookRepository.getBookByIsbn(isbn)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "book not found"));
    }
}
