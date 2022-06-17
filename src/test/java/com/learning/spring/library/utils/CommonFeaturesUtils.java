package com.learning.spring.library.utils;

import com.learning.spring.library.api.dto.BookDTO;
import com.learning.spring.library.api.dto.LoanDTO;
import com.learning.spring.library.api.model.entity.Book;
import com.learning.spring.library.api.model.entity.Loan;

import java.time.LocalDate;

public class CommonFeaturesUtils {
    public static BookDTO createBookDTO() {
        return BookDTO.builder()
                .author("Author")
                .isbn("123")
                .title("My book")
                .build();
    }

    public static Book createBook() {
        return Book.builder()
                .id(10L)
                .author("Author")
                .isbn("123")
                .title("My book")
                .build();
    }

    public static Book createBookNotId() {
        return Book.builder()
                .author("Author")
                .isbn("123")
                .title("My book")
                .build();
    }

    public static BookDTO createBookDTOWithId() {
        return BookDTO.builder()
                .id(10L)
                .author("Author")
                .isbn("123")
                .title("My book")
                .build();
    }

    public static LoanDTO createLoanDTO() {
        return LoanDTO.builder()
                .isbn("123")
                .customer("antonio")
                .build();
    }

    public static Loan createLoan() {
        return Loan.builder()
                .id(1L)
                .loanDate(LocalDate.now())
                .customer("antonio")
                .returned(false)
                .build();
    }

    public static Loan createLoanWithReturnedTrue() {
        return Loan.builder()
                .id(1L)
                .loanDate(LocalDate.now())
                .customer("antonio")
                .returned(true)
                .build();
    }


    public static Loan createLoanNotId() {
        return Loan.builder()
                .loanDate(LocalDate.now())
                .customer("antonio")
                .returned(false)
                .build();
    }
}
