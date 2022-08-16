package com.lms.lms.error;

public class PhotoNotFoundException extends RuntimeException {

    public PhotoNotFoundException(Long id) {
        super("Photo with id = " + id + " was not found.");
    }
}
