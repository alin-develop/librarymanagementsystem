package com.lms.lms.error;

public class BookDeletionException extends RuntimeException {
    public BookDeletionException(Long id) {
        super("There was an error during deletion of the book with id = "+id+".");
    }
}
