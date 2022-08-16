package com.lms.lms.error;

public class CategoryExistsException extends RuntimeException {

    public CategoryExistsException(Long id) {
        super("Category with id = "+id+" already exists.");
    }
}
