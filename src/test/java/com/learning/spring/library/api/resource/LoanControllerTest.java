package com.learning.spring.library.api.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.spring.library.api.dto.LoanDTO;
import com.learning.spring.library.api.model.entity.Book;
import com.learning.spring.library.api.model.entity.Loan;
import com.learning.spring.library.service.BookService;
import com.learning.spring.library.service.LoanService;
import com.learning.spring.library.utils.CommonFeaturesUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
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
}
