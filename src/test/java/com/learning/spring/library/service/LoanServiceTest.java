package com.learning.spring.library.service;

import com.learning.spring.library.api.dto.LoanDTO;
import com.learning.spring.library.api.model.entity.Book;
import com.learning.spring.library.api.model.entity.Loan;
import com.learning.spring.library.api.model.repository.LoanRepository;
import com.learning.spring.library.exception.BookAlreadyLoanedException;
import com.learning.spring.library.service.implementation.LoanServiceImpl;
import com.learning.spring.library.utils.CommonFeaturesUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    LoanService loanService;

    @MockBean
    LoanRepository loanRepository;

    @BeforeEach
    void setUp() {
        this.loanService = new LoanServiceImpl(loanRepository);
    }

    @Test
    @DisplayName("should save loan")
    void shouldSaveLoan() {
        Book book = CommonFeaturesUtils.createBook();
        LoanDTO loanDTO = CommonFeaturesUtils.createLoanDTO();
        Loan savedLoan = CommonFeaturesUtils.createLoan();
        savedLoan.setBook(book);

        Mockito.when(loanRepository.save(Mockito.any(Loan.class))).thenReturn(savedLoan);
        Loan loanSaved = loanService.save(loanDTO, book);

        Mockito.verify(loanRepository, Mockito.times(1))
                .save(Mockito.any(Loan.class));
        assertThat(loanSaved.getId()).isNotNull();
    }

    @Test
    @DisplayName("should return error if this book already loaned")
    void shouldReturnErrorIfThisBookAlreadyLoaned() {
        Book book = CommonFeaturesUtils.createBook();
        LoanDTO loanDTO = CommonFeaturesUtils.createLoanDTO();
        book.setLoan(CommonFeaturesUtils.createLoan());
        Throwable throwable = Assertions.catchThrowable(() -> loanService.save(loanDTO, book));

        Mockito.verify(loanRepository, Mockito.never()).save(CommonFeaturesUtils.createLoan());
        assertThat(throwable)
                .isInstanceOf(BookAlreadyLoanedException.class)
                .hasMessage(BookAlreadyLoanedException.MESSAGE);
    }
}
