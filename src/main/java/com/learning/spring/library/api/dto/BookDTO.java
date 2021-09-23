package com.learning.spring.library.api.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;

    @NotEmpty(message = "title must not be empty")
    private String title;

    @NotEmpty(message = "author must not be empty")
    private String author;

    @NotEmpty(message = "isbn must not be empty")
    private String isbn;
}
