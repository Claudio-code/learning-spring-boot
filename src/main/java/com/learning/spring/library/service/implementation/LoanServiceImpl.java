package com.learning.spring.library.service.implementation;

import com.learning.spring.library.api.dto.LoanDTO;
import com.learning.spring.library.api.model.entity.Book;
import com.learning.spring.library.api.model.entity.Loan;
import com.learning.spring.library.api.model.repository.LoanRepository;
import com.learning.spring.library.exception.BookAlreadyLoanedException;
import com.learning.spring.library.service.LoanService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;

    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public Loan save(LoanDTO loanDTO, Book book) {
        if (book.getLoan() != null) {
            throw new BookAlreadyLoanedException();
        }

        Loan loan = Loan.builder()
                .book(book)
                .customer(loanDTO.getCustomer())
                .loanDate(LocalDate.now())
                .returned(false)
                .build();
        return loanRepository.save(loan);
    }
}
