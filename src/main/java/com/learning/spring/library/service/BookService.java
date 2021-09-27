package com.learning.spring.library.service;

import com.learning.spring.library.api.model.entity.Book;

import java.util.Optional;

public interface BookService {
    Book save(Book any);

    Optional<Book> getById(Long id);
}
