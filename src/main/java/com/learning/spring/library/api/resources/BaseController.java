package com.learning.spring.library.api.resources;

import com.learning.spring.library.api.exception.ApiErrors;
import com.learning.spring.library.exception.IsbnAlreadyUsedByAnotherBookException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface BaseController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    default ApiErrors handleValidationExceptions(MethodArgumentNotValidException validException) {
        return new ApiErrors(validException.getBindingResult());
    }

    @ExceptionHandler(IsbnAlreadyUsedByAnotherBookException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    default ApiErrors handleIsbnAlreadyUsed(IsbnAlreadyUsedByAnotherBookException isbnException) {
        return new ApiErrors(isbnException);
    }
}
