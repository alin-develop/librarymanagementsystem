package com.lms.lms.security;

import lombok.Getter;

@Getter
public enum ApplicationUserPermission {
    APP_USER_READ("app_user:read"),
    APP_USER_WRITE("app_user:write"),
    BOOK_READ("book:read"),
    BOOK_WRITE("book:write"),
    CATEGORY_READ("category:read"),
    CATEGORY_WRITE("category:write"),
    PHOTO_READ("photo:read"),
    PHOTO_WRITE("photo:write"),
    BORROWED_BOOKS_WRITE("borrowed_books:write"),
    BORROWED_BOOKS_READ("borrowed_books:read");

    private final String permission;


    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }
}
