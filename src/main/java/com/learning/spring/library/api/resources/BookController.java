package com.learning.spring.library.api.resources;

import com.learning.spring.library.api.dto.BookDTO;
import com.learning.spring.library.api.model.entity.Book;
import com.learning.spring.library.service.BookService;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController extends BaseController {
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
    public BookDTO get(@PathVariable Long id) {
        var book = service.getById(id);
        return modelMapper.map(book, BookDTO.class);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        var bookDTO = service.getById(id);
        var book = modelMapper.map(bookDTO, Book.class);
        service.delete(book);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDTO update(@PathVariable Long id, @RequestBody @Valid BookDTO bookDTO) {
        var bookUpdate = service.getById(id);
        bookUpdate.setTitle(bookDTO.getTitle());
        bookUpdate.setAuthor(bookDTO.getAuthor());
        var book = service.update(bookUpdate);

        return modelMapper.map(book, BookDTO.class);
    }

    @GetMapping
    public PageImpl<BookDTO> find(BookDTO filters, Pageable pageRequest) {
        Book bookFilters = modelMapper.map(filters, Book.class);
        Page<Book> result = service.find(bookFilters, pageRequest);
        List<BookDTO> list = result.getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, BookDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(list, pageRequest, result.getTotalElements());
    }
}
