package com.lms.lms.error;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException(String isbn) {
        super("Book with isbn = " + isbn + " already exists.");
    }
}
