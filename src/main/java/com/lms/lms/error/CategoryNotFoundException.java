package com.lms.lms.error;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long categoryId) {
        super("Category with id = " + categoryId + " was not found.");
    }
}
