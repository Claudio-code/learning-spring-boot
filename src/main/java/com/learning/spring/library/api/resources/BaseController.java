package com.learning.spring.library.api.resources;

import com.learning.spring.library.api.exception.ApiErrors;
import com.learning.spring.library.exception.IsbnAlreadyUsedByAnotherBookException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class BaseController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationExceptions(MethodArgumentNotValidException validException) {
        return new ApiErrors(validException.getBindingResult());
    }

    @ExceptionHandler(IsbnAlreadyUsedByAnotherBookException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleIsbnAlreadyUsed(IsbnAlreadyUsedByAnotherBookException isbnException) {
        return new ApiErrors(isbnException);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrors> handleResponseStatusException(ResponseStatusException statusException) {
        return new ResponseEntity<>(new ApiErrors(statusException), statusException.getStatus());
    }
}
