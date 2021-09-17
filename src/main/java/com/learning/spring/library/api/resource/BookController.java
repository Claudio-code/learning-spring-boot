package com.learning.spring.library.api.resource;

import com.learning.spring.library.api.dto.BookDTO;
import com.learning.spring.library.api.model.entity.Book;
import com.learning.spring.library.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody BookDTO bookDTO) {
        Book bookEntity = Book.builder()
                .id(10L)
                .author(bookDTO.getAuthor())
                .isbn(bookDTO.getIsbn())
                .title(bookDTO.getTitle())
                .build();
        bookEntity = service.save(bookEntity);

        return BookDTO.builder()
                .id(10L)
                .author(bookEntity.getAuthor())
                .isbn(bookEntity.getIsbn())
                .title(bookEntity.getTitle())
                .build();
    }
}
