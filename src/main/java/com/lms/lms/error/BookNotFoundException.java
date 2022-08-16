package com.lms.lms.error;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long id) {
        super("Book with this id =" +
                 id + " was not found.");
    }

    public BookNotFoundException(String isbn) {
        super("Book with this isbn =" +
                 isbn + " was not found.");
    }
}
