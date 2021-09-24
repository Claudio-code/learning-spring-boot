package com.learning.spring.library.exception;

public class IsbnAlreadyUsedByAnotherBookException extends RuntimeException {
    public static final String MESSAGE = "It isbn already registered in another book.";

    public IsbnAlreadyUsedByAnotherBookException() {
        super(MESSAGE);
    }
}
