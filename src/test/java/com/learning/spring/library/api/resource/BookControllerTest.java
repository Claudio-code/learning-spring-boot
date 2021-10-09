package com.learning.spring.library.api.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.spring.library.api.dto.BookDTO;
import com.learning.spring.library.api.model.entity.Book;
import com.learning.spring.library.exception.IsbnAlreadyUsedByAnotherBookException;
import com.learning.spring.library.service.BookService;
import com.learning.spring.library.utils.CommonFeaturesUtils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
class BookControllerTest {
    static String BOOK_API = "/api/books";

    @Autowired
    MockMvc mvc;

    @MockBean
    BookService bookService;

    @Test
    @DisplayName("should create book with success.")
    void createBookTest() throws Exception {
        BookDTO bookDTO = CommonFeaturesUtils.createBookDTO();
        Book bookMock = CommonFeaturesUtils.createBook();
        BDDMockito.given(bookService.save(Mockito.any(Book.class))).willReturn(bookMock);
        String json = new ObjectMapper().writeValueAsString(bookDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("title").value(bookDTO.getTitle()))
                .andExpect(jsonPath("author").value(bookDTO.getAuthor()))
                .andExpect(jsonPath("isbn").value(bookDTO.getIsbn()))
        ;
    }

    @Test
    @DisplayName("should throw validation error when there is not data to create book entity")
    void createInvalidBookTest() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new BookDTO());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(3)));
    }

    @Test
    @DisplayName("should throw error to try register book with isbn already used by another book")
    void createBookWithDuplicatedIsbn() throws Exception {
        BookDTO bookDTO = CommonFeaturesUtils.createBookDTO();
        String json = new ObjectMapper().writeValueAsString(bookDTO);
        BDDMockito.given(bookService.save(Mockito.any(Book.class)))
                .willThrow(new IsbnAlreadyUsedByAnotherBookException());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(IsbnAlreadyUsedByAnotherBookException.MESSAGE));
    }

    @Test
    @DisplayName("should get information's book")
    void getBookDetailsTest() throws Exception {
        var book = CommonFeaturesUtils.createBook();
        BDDMockito.given(bookService.getById(book.getId())).willReturn(book);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/" + book.getId()))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("title").value(book.getTitle()))
                .andExpect(jsonPath("author").value(book.getAuthor()))
                .andExpect(jsonPath("isbn").value(book.getIsbn()));
    }

    @Test
    @DisplayName("should return resource not found when book researched not exists")
    void bookNotFound() throws Exception {
        var exception = new ResponseStatusException(HttpStatus.NOT_FOUND);
        BDDMockito.given(bookService.getById(Mockito.anyLong())).willThrow(exception);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/" + 1L))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return status not content when deleted book")
    void deleteBookTest() throws Exception {
        var book = CommonFeaturesUtils.createBook();
        BDDMockito.given(bookService.getById(book.getId())).willReturn(book);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/" + book.getId()));

        mvc.perform(request).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("should return Errors if book not found in database")
    void deleteIntentBookTest() throws Exception {
        var exception = new ResponseStatusException(HttpStatus.NOT_FOUND);
        BDDMockito.given(bookService.getById(Mockito.anyLong())).willThrow(exception);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/" + 1L));

        mvc.perform(request).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should update book")
    void shouldUpdateBook() throws Exception {
        Book book = CommonFeaturesUtils.createBook();
        Book bookToJson = CommonFeaturesUtils.createBook();
        bookToJson.setAuthor("vanderson");
        bookToJson.setTitle("update test");
        String json = new ObjectMapper().writeValueAsString(bookToJson);

        BDDMockito.given(bookService.getById(bookToJson.getId())).willReturn(book);
        BDDMockito.given(bookService.update(book)).willReturn(bookToJson);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(BOOK_API.concat("/" + bookToJson.getId()))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value(bookToJson.getTitle()))
                .andExpect(jsonPath("author").value(bookToJson.getAuthor()))
                .andExpect(jsonPath("id").value(bookToJson.getId()));
    }

    @Test
    @DisplayName("should return error if nonexistent book")
    void shouldReturnErrorIfNonexistentBookTest() throws Exception {
        String json = new ObjectMapper().writeValueAsString(CommonFeaturesUtils.createBook());
        var exception = new ResponseStatusException(HttpStatus.NOT_FOUND);

        BDDMockito.given(bookService.getById(Mockito.anyLong())).willThrow(exception);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(BOOK_API.concat("/" + Mockito.anyLong()))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should returned books when parameters are sent")
    void findBooksTest() throws Exception {
        var book = CommonFeaturesUtils.createBook();
        var queryString = "?title=%s&author=%s&page=0&size=100";
        var queryStringFormatted = String.format(queryString, book.getTitle(), book.getAuthor());

        BDDMockito.given(bookService.find(Mockito.any(Book.class), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(List.of(book), PageRequest.of(0, 100), 1));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat(queryStringFormatted))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(100))
                .andExpect(jsonPath("pageable.pageNumber").value(0));
    }
}
