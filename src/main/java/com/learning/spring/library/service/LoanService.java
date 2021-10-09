package com.learning.spring.library.service;

import com.learning.spring.library.api.dto.LoanDTO;
import com.learning.spring.library.api.model.entity.Book;
import com.learning.spring.library.api.model.entity.Loan;

public interface LoanService {
    Loan save(LoanDTO loanDTO, Book book);
}
