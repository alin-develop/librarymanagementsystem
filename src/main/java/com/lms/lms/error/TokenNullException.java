package com.lms.lms.error;

public class TokenNullException extends RuntimeException {

    public TokenNullException() {
        super("Token is null");
    }
}
