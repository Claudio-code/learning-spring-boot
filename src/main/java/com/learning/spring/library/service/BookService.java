package com.learning.spring.library.service;

import com.learning.spring.library.api.model.entity.Book;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface BookService {
    Book save(Book any);

    Book getById(Long id);

    void delete(Book book);

    Book update(Book book);

    PageImpl<Book> find(Book filter, Pageable pageable);
}
