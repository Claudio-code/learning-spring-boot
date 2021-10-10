package com.learning.spring.library.service;

import com.learning.spring.library.api.dto.LoanDTO;
import com.learning.spring.library.api.dto.LoanFilterDTO;
import com.learning.spring.library.api.dto.ReturnedLoanDTO;
import com.learning.spring.library.api.model.entity.Book;
import com.learning.spring.library.api.model.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface LoanService {
    Loan save(LoanDTO loanDTO, Book book);

    Loan getById(Long id);

    void update(Loan loan, ReturnedLoanDTO returnedLoanDTO);

    Page<Loan> find(LoanFilterDTO filter, Pageable pageable);
}
