package com.learning.spring.library.api.exception;

import com.learning.spring.library.exception.IsbnAlreadyUsedByAnotherBookException;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApiErrors {
    private final List<String> errors;

    public ApiErrors() {
        this.errors = new ArrayList<>();
    }

    public ApiErrors(BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors()
                .forEach(error -> this.errors.add(error.getDefaultMessage()));
    }

    public ApiErrors(IsbnAlreadyUsedByAnotherBookException exception) {
        this.errors = List.of(exception.getMessage());
    }

    public ApiErrors(ResponseStatusException statusException) {
        this.errors = List.of(Objects.requireNonNull(statusException.getMessage()));
    }

    public List<String> getErrors() {
        return errors;
    }
}
