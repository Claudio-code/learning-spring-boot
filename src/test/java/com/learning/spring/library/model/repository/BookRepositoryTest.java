package com.learning.spring.library.model.repository;

import com.learning.spring.library.api.model.entity.Book;
import com.learning.spring.library.api.model.repository.BookRepository;
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
class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository bookRepository;

    @Test
    @DisplayName("should return true when exists book in database with isbn field")
    void shouldReturnTrueWhenIsbnExists() {
        var book = CommonFeaturesUtils.createBook();
        bookRepository.save(book);
        var resultIfIsbnExists = bookRepository.existsByIsbn(book.getIsbn());

        assertThat(resultIfIsbnExists).isTrue();
    }

    @Test
    @DisplayName("should save book entity and create id field")
    void shouldSaveBookEntityAndCreateIdField() {
        var bookToSave = CommonFeaturesUtils.createBookNotId();
        var bookSaved = bookRepository.save(bookToSave);

        assertThat(bookSaved.getId()).isNotNull();
    }

    @Test
    @DisplayName("should get book if it book exists")
    void shouldGetBookIfItBookExists() {
        var bookToSave = CommonFeaturesUtils.createBookNotId();
        bookToSave = bookRepository.save(bookToSave);

        var bookFound = bookRepository.findById(bookToSave.getId()).get();

        assertThat(bookFound.getId()).isEqualTo(bookToSave.getId());
        assertThat(bookFound.getIsbn()).isEqualTo(bookToSave.getIsbn());
        assertThat(bookFound.getAuthor()).isEqualTo(bookToSave.getAuthor());
        assertThat(bookFound.getTitle()).isEqualTo(bookToSave.getTitle());
    }

    @Test
    @DisplayName("should get null book if book nonexistent")
    void shouldGetNullBookIfBookNonexistent() {
        var bookFound = entityManager.find(Book.class, 1L);

        assertThat(bookFound).isNull();
    }

    @Test
    @DisplayName("should return null later delete book")
    void shouldReturnNullLaterDeleteBook() {
        var bookToSave = CommonFeaturesUtils.createBookNotId();
        entityManager.persist(bookToSave);

        var bookFound = entityManager.find(Book.class, bookToSave.getId());
        bookRepository.delete(bookFound);
        var bookDeleted = entityManager.find(Book.class, bookToSave.getId());

        assertThat(bookDeleted).isNull();
    }
}
