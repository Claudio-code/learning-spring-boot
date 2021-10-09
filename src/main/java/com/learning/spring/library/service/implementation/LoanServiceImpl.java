package com.learning.spring.library.service.implementation;

import com.learning.spring.library.api.dto.LoanDTO;
import com.learning.spring.library.api.model.entity.Book;
import com.learning.spring.library.api.model.entity.Loan;
import com.learning.spring.library.api.model.repository.LoanRepository;
import com.learning.spring.library.service.LoanService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LoanServiceImpl implements LoanService {
    private LoanRepository loanRepository;

    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public Loan save(LoanDTO loanDTO, Book book) {
        Loan loan = Loan.builder()
                .book(book)
                .customer(loanDTO.getCustomer())
                .loanDate(LocalDate.now())
                .build();
        loan = loanRepository.save(loan);

        return loan;
    }
}
