package com.lms.lms.error;

public class PhotoExistsException extends RuntimeException {
    public PhotoExistsException(Long id) {
        super("Photo with id = " + id + " already exists.");
    }
}
