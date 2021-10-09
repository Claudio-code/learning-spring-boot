package com.learning.spring.library.service.implementation;

import com.learning.spring.library.api.dto.LoanDTO;
import com.learning.spring.library.api.dto.ReturnedLoanDTO;
import com.learning.spring.library.api.model.entity.Book;
import com.learning.spring.library.api.model.entity.Loan;
import com.learning.spring.library.api.model.repository.LoanRepository;
import com.learning.spring.library.exception.BookAlreadyLoanedException;
import com.learning.spring.library.service.LoanService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;

    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public Loan save(LoanDTO loanDTO, Book book) {
        if (loanRepository.existsByBookAndNotReturned(book)) {
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

    @Override
    @Cacheable(cacheNames = Loan.CACHE_NAME, key = "#id")
    public Loan getById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    @CachePut(cacheNames = Loan.CACHE_NAME, key = "#loan.id")
    public void update(Loan loan, ReturnedLoanDTO returnedLoanDTO) {
        loan.setReturned(returnedLoanDTO.getReturned());
        loanRepository.save(loan);
    }
}
