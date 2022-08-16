package com.lms.lms.error;

public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(Long id) {
        super("Book with id = " + id + " is not available to borrow.");
    }
}
