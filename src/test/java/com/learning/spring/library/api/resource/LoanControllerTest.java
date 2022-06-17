package com.learning.spring.library.api.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.spring.library.api.dto.LoanDTO;
import com.learning.spring.library.api.dto.LoanFilterDTO;
import com.learning.spring.library.api.dto.ReturnedLoanDTO;
import com.learning.spring.library.api.model.entity.Book;
import com.learning.spring.library.api.model.entity.Loan;
import com.learning.spring.library.api.resources.LoanController;
import com.learning.spring.library.exception.BookAlreadyLoanedException;
import com.learning.spring.library.service.BookService;
import com.learning.spring.library.service.LoanService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = LoanController.class)
@AutoConfigureMockMvc
class LoanControllerTest {
    static String LOAN_API = "/api/loans";

    @Autowired
    MockMvc mvc;

    @MockBean
    BookService bookService;

    @MockBean
    LoanService loanService;

    @Test
    @DisplayName("should execute loan")
    void shouldExecuteLoan() throws Exception {
        var loanDTO = CommonFeaturesUtils.createLoanDTO();
        var loan = CommonFeaturesUtils.createLoan();
        var book = CommonFeaturesUtils.createBook();
        String json = new ObjectMapper().writeValueAsString(loanDTO);

        BDDMockito.given(bookService.getBookByIsbn(loanDTO.getIsbn())).willReturn(book);
        BDDMockito.given(loanService.save(Mockito.any(LoanDTO.class), Mockito.any(Book.class))).willReturn(loan);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }

    @Test
    @DisplayName("should return error to try make loan if nonexistent book")
    void shouldReturnErrorToTryMakeLoanIfNonexistentBook() throws Exception {
        LoanDTO loanDTO = CommonFeaturesUtils.createLoanDTO();
        String json = new ObjectMapper().writeValueAsString(loanDTO);

        BDDMockito.given(bookService.getBookByIsbn(loanDTO.getIsbn()))
                .willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)));
    }

    @Test
    @DisplayName("should return error when try save loan book already loaned")
    void shouldReturnErrorWhenTrySaveLoanBookAlreadyLoaned() throws Exception {
        var loanDTO = CommonFeaturesUtils.createLoanDTO();
        var book = CommonFeaturesUtils.createBook();
        String json = new ObjectMapper().writeValueAsString(loanDTO);

        BDDMockito.given(bookService.getBookByIsbn(loanDTO.getIsbn())).willReturn(book);
        BDDMockito.given(loanService.save(Mockito.any(LoanDTO.class), Mockito.any(Book.class)))
                .willThrow(new BookAlreadyLoanedException());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(BookAlreadyLoanedException.MESSAGE));
    }

    @Test
    @DisplayName("should update loan and return book")
    void shouldUpdateLoanAndReturnBook() throws Exception {
        Loan loan = CommonFeaturesUtils.createLoan();
        loan.setId(1L);
        ReturnedLoanDTO returnedLoanDTO = ReturnedLoanDTO.builder().returned(true).build();
        String json = new ObjectMapper().writeValueAsString(returnedLoanDTO);

        BDDMockito.given(loanService.getById(loan.getId())).willReturn(loan);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch(LOAN_API.concat("/1"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request).andExpect(status().isOk());
        Mockito.verify(loanService, Mockito.times(1))
                .update(Mockito.any(Loan.class), Mockito.any(ReturnedLoanDTO.class));
    }

    @Test
    @DisplayName("should return error if loan nonexistent")
    void shouldReturnErrorIfLoanNonexistent() throws Exception {
        Loan loan = CommonFeaturesUtils.createLoan();
        loan.setId(1L);
        ReturnedLoanDTO returnedLoanDTO = ReturnedLoanDTO.builder().returned(true).build();
        String json = new ObjectMapper().writeValueAsString(returnedLoanDTO);

        BDDMockito.given(loanService.getById(loan.getId()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch(LOAN_API.concat("/1"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isNotFound());
        Mockito.verify(loanService, Mockito.never())
                .update(Mockito.any(Loan.class), Mockito.any(ReturnedLoanDTO.class));
    }

    @Test
    @DisplayName("should return loan`s info")
    void shouldReturnDataOfLoan() throws Exception {
        Loan loan = CommonFeaturesUtils.createLoan();
        loan.setId(1L);
        loan.setBook(CommonFeaturesUtils.createBook());

        BDDMockito.given(loanService.getById(loan.getId())).willReturn(loan);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(LOAN_API.concat("/1"));

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(loan.getId()))
                .andExpect(jsonPath("book.id").value(loan.getBook().getId()));
    }

    @Test
    @DisplayName("should find loan and return it")
    void shouldFindLoanAndReturnIt() throws Exception {
        LoanDTO loanDTO = CommonFeaturesUtils.createLoanDTO();
        Loan loan = CommonFeaturesUtils.createLoan();
        Book book = CommonFeaturesUtils.createBook();
        loan.setBook(book);
        String queryString = "?isbn=%s&customer=%s&page=0&size=100";
        String queryStringFormatted = String.format(queryString, loanDTO.getIsbn(), loan.getCustomer());

        BDDMockito.given(loanService.find(Mockito.any(LoanFilterDTO.class), Mockito.any(PageRequest.class)))
                .willReturn(new PageImpl<>(Arrays.asList(loan), PageRequest.of(0, 100), 1));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(LOAN_API.concat(queryStringFormatted))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(100))
                .andExpect(jsonPath("pageable.pageNumber").value(0));
    }
}
