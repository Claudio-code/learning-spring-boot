package com.learning.spring.library.service;

import com.learning.spring.library.api.model.entity.Book;
import com.learning.spring.library.api.model.repository.BookRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        Book bookReturned = CommonFeaturesUtils.createBook();
        Mockito.when(bookRepository.findById(bookReturned.getId())).thenReturn(Optional.of(bookReturned));
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
}
