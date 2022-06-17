package com.learning.spring.library.api.model.repository;

import com.learning.spring.library.api.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);
    Optional<Book> getBookByIsbn(String isbn);
}
