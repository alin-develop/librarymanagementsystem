package com.lms.lms.error;

public class BookExtensionException extends RuntimeException {
    public BookExtensionException() {
        super("Borrowing period can not be extended.");
    }
}
