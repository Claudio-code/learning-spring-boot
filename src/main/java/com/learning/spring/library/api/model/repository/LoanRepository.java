package com.learning.spring.library.api.model.repository;

import com.learning.spring.library.api.model.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
