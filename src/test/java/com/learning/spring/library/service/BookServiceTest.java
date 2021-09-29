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
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class BookServiceTest {
    BookService bookService;

    @MockBean
    BookRepository bookRepository;

    @MockBean
    ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        this.bookService = new BookServiceImpl(bookRepository, modelMapper);
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
}
