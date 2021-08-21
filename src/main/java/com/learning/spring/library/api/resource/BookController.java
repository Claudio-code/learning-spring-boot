package com.learning.spring.library.api.resource;

import com.learning.spring.library.api.DTO.BookDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(11L);
        bookDTO.setAuthor("Author");
        bookDTO.setIsbn("123");
        bookDTO.setTitle("My book");
        return bookDTO;
    }
}
