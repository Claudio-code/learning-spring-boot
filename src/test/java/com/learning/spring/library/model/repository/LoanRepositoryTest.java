package com.learning.spring.library.model.repository;

import com.learning.spring.library.api.model.repository.BookRepository;
import com.learning.spring.library.api.model.repository.LoanRepository;
import com.learning.spring.library.utils.CommonFeaturesUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
class LoanRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    BookRepository bookRepository;

    @Test
    @DisplayName("should verify if exist loan not returned for this book")
    void shouldVerifyIfExistLoanNotReturnedForThisBook() {
        var loanToSave = CommonFeaturesUtils.createLoanNotId();
        var bookToSave = CommonFeaturesUtils.createBookNotId();
        bookToSave = bookRepository.save(bookToSave);
        loanToSave.setBook(bookToSave);
        loanRepository.save(loanToSave);

        boolean exists = loanRepository.existsByBookAndNotReturned(bookToSave);

        assertThat(exists).isTrue();
    }
}
