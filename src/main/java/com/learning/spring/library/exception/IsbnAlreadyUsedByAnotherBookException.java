package com.learning.spring.library.exception;

public class IsbnAlreadyUsedByAnotherBookException extends BusinessException {
    public static final String MESSAGE = "It isbn already registered in another book.";

    public IsbnAlreadyUsedByAnotherBookException() {
        super(MESSAGE);
    }
}
