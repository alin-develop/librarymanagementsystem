package com.lms.lms.error;

public class BookNotBorrowedException extends RuntimeException {
    public BookNotBorrowedException(Long bookId, Long appUserId) {
        super("Book with id ="+bookId+"was not borrowed by User with id = "+appUserId+".");
    }
}
