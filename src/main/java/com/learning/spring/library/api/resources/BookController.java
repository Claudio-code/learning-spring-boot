package com.learning.spring.library.api.resources;

import com.learning.spring.library.api.dto.BookDTO;
import com.learning.spring.library.api.model.entity.Book;
import com.learning.spring.library.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController implements BaseController {
    private final BookService service;
    private final ModelMapper modelMapper;


    public BookController(BookService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO bookDTO) {
        Book bookEntity = modelMapper.map(bookDTO, Book.class);
        bookEntity = service.save(bookEntity);
        return modelMapper.map(bookEntity, BookDTO.class);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Cacheable(cacheNames = "book-get-one-", key = "#id", sync = true)
    public BookDTO get(@PathVariable Long id) {
        return service
                .getById(id)
                .map(book -> modelMapper.map(book, BookDTO.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
