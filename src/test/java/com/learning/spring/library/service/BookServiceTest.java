package com.learning.spring.library.service;

import com.learning.spring.library.api.model.entity.Book;
import com.learning.spring.library.api.model.repository.BookRepository;
import com.learning.spring.library.exception.InvalidBookException;
import com.learning.spring.library.exception.IsbnAlreadyUsedByAnotherBookException;
import com.learning.spring.library.service.implementation.BookServiceImpl;
import com.learning.spring.library.utils.CommonFeaturesUtils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class BookServiceTest {
    BookService bookService;

    @MockBean
    BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        this.bookService = new BookServiceImpl(bookRepository);
    }

    private Book makeValidSavedBook() {
        Book bookReturned = CommonFeaturesUtils.createBook();
        Mockito.when(bookRepository.findById(bookReturned.getId())).thenReturn(Optional.of(bookReturned));
        return bookReturned;
    }

    @Test
    @DisplayName("should save book")
    void saveBookTest() {
        Book book = CommonFeaturesUtils.createBook();
        Mockito.when(bookRepository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        Mockito.when(bookRepository.save(book)).thenReturn(CommonFeaturesUtils.createBook());
        Book savedBook = bookService.save(book);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo(book.getIsbn());
        assertThat(savedBook.getTitle()).isEqualTo(book.getTitle());
        assertThat(savedBook.getAuthor()).isEqualTo(book.getAuthor());
    }

    @Test
    @DisplayName("should throw business error to try register book with isbn already used another book")
    void shouldNotSaveABookWithDuplicateIsbn() {
        Book book = CommonFeaturesUtils.createBook();
        Mockito.when(bookRepository.existsByIsbn(Mockito.anyString())).thenReturn(true);
        Mockito.verify(bookRepository, Mockito.never()).save(book);
        Throwable exception = Assertions.catchThrowable(() -> bookService.save(book));

        assertThat(exception)
                .isInstanceOf(IsbnAlreadyUsedByAnotherBookException.class)
                .hasMessage(IsbnAlreadyUsedByAnotherBookException.MESSAGE);
    }

    @Test
    @DisplayName("should throw get book when user passes id how parameter")
    void shouldGetBookWhenUserPassesIdHowParameter() {
        Book bookReturned = makeValidSavedBook();
        Book bookFound = bookService.getById(bookReturned.getId());

        assertThat(bookFound.getId()).isEqualTo(bookReturned.getId());
        assertThat(bookFound.getIsbn()).isEqualTo(bookReturned.getIsbn());
        assertThat(bookFound.getAuthor()).isEqualTo(bookReturned.getAuthor());
        assertThat(bookFound.getTitle()).isEqualTo(bookReturned.getTitle());
    }

    @Test
    @DisplayName("should return error if book not found in database")
    void shouldReturnErrorIfBookNotFoundInDatabase() {
        Long bookId = Mockito.anyLong();
        Mockito.when(bookRepository.findById(bookId)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        Throwable throwable = Assertions.catchThrowable(() -> bookService.getById(bookId));

        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
    }


    @Test
    @DisplayName("should return error if book not found in database")
    void shouldDeletedBookWhenFoundItInDatabase() {
        Book book = CommonFeaturesUtils.createBook();
        bookService.delete(book);

        Mockito.verify(bookRepository, Mockito.times(1)).delete(book);
    }

    @Test
    @DisplayName("should return error if book fields is null")
    void shouldReturnErrorIfBookFieldsIsNull() {
        Book book = new Book();

        assertThrows(InvalidBookException.class, () -> bookService.delete(book));
        Mockito.verify(bookRepository, Mockito.never()).delete(book);
    }

    @Test
    @DisplayName("should updated book if found it in database")
    void shouldUpdatedBookIfFoundItInDatabase() {
        Book book = CommonFeaturesUtils.createBook();
        Mockito.when(bookService.update(book)).thenReturn(book);
        Book bookUpdated = bookService.update(book);

        assertThat(bookUpdated.getId()).isNotNull();
        assertThat(bookUpdated.getIsbn()).isEqualTo(book.getIsbn());
        assertThat(bookUpdated.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookUpdated.getAuthor()).isEqualTo(book.getAuthor());
    }

    @Test
    @DisplayName("should filter books by properties")
    void shouldFilterBooksByProperties() {
        Book book = makeValidSavedBook();
        List<Book> list = List.of(book);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Book> page = new PageImpl<>(list, pageRequest, 1);
        Mockito.when(bookRepository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
                .thenReturn(page);

        Page<Book> result = bookService.find(book, pageRequest);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(list);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }
}
