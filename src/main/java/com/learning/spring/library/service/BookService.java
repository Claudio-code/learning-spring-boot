package com.learning.spring.library.service;

import com.learning.spring.library.api.dto.BookDTO;
import com.learning.spring.library.api.model.entity.Book;

public interface BookService {
    Book save(Book any);

    BookDTO getById(Long id);
}
