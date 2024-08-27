package com.learning.spring.library.api.resources;

import com.learning.spring.library.api.dto.LoanDTO;
import com.learning.spring.library.api.dto.LoanFilterDTO;
import com.learning.spring.library.api.dto.ReturnedLoanDTO;
import com.learning.spring.library.api.model.entity.Loan;
import com.learning.spring.library.service.BookService;
import com.learning.spring.library.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController extends BaseController {
    private final LoanService loanService;
    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody LoanDTO loanDTO) {
        var book = bookService.getBookByIsbn(loanDTO.getIsbn());
        var loan = loanService.save(loanDTO, book);

        return loan.getId();
    }

    @PatchMapping("{id}")
    public void returnBook(@PathVariable Long id, @RequestBody @Valid ReturnedLoanDTO returnedLoanDTO) {
        Loan loan = loanService.getById(id);
        loanService.update(loan, returnedLoanDTO);
    }

    @GetMapping("{id}")
    public Loan getOne(@PathVariable Long id) {
        return loanService.getById(id);
    }

    @GetMapping
    public Page<Loan> find(LoanFilterDTO loanFilterDTO, Pageable pageable) {
        return loanService.find(loanFilterDTO, pageable);
    }
}
