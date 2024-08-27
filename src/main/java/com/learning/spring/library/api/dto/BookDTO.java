package com.learning.spring.library.api.dto;

import lombok.*;

import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO implements Serializable {
    private Long id;

    @NotEmpty(message = "title must not be empty")
    private String title;

    @NotEmpty(message = "author must not be empty")
    private String author;

    @NotEmpty(message = "isbn must not be empty")
    private String isbn;
}
