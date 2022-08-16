package com.lms.lms.error;

public class AppUserNotFoundException extends RuntimeException {

    public AppUserNotFoundException(Long id) {
        super("App User with id = " + id + " Not Found.");
    }

    public AppUserNotFoundException(String username) {
        super("App User with username = " + username + " not found." );
    }
}
