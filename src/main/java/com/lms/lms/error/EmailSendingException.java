package com.lms.lms.error;

public class EmailSendingException extends RuntimeException {

    public EmailSendingException() {
        super("Failed to send email.");
    }
}
