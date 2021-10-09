package com.learning.spring.library.api.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO implements Serializable {
    private Long id;

    @NotEmpty(message = "isbn must not be empty")
    private String isbn;

    @NotEmpty(message = "customer must not be empty")
    private String customer;
}
