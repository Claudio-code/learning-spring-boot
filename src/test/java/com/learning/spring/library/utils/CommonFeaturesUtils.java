package com.learning.spring.library.utils;

import com.learning.spring.library.api.dto.BookDTO;
import com.learning.spring.library.api.model.entity.Book;

public class CommonFeaturesUtils {
    public static BookDTO createBookDTO() {
        return BookDTO.builder()
                .author("Author")
                .isbn("123")
                .title("My book")
                .build();
    }

    public static Book createBook() {
        return Book.builder()
                .id(10L)
                .author("Author")
                .isbn("123")
                .title("My book")
                .build();
    }

    public static BookDTO createBookDTOWithId() {
        return BookDTO.builder()
                .id(10L)
                .author("Author")
                .isbn("123")
                .title("My book")
                .build();
    }

}
