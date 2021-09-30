package com.learning.spring.library.exception;

public class InvalidBookException extends RuntimeException {
    public static final String MESSAGE = "This book have fields empty ou It is invalid.";

    public InvalidBookException() {
        super(MESSAGE);
    }
}
