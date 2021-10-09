package com.learning.spring.library.exception;

public class BookAlreadyLoanedException extends BusinessException {
    public static final String MESSAGE = "This book has already been loaned.";

    public BookAlreadyLoanedException() {
        super(MESSAGE);
    }
}
