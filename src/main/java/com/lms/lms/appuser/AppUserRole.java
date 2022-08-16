package com.lms.lms.appuser;


import com.lms.lms.security.ApplicationUserPermission;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;


public enum AppUserRole {
    USER(Permissions.userSet),
    LIBRARIAN(Permissions.librarianSet);

    private static class Permissions{
        private static final EnumSet<ApplicationUserPermission> userSet = EnumSet.of(
                ApplicationUserPermission.BOOK_READ,
                ApplicationUserPermission.CATEGORY_READ, ApplicationUserPermission.PHOTO_READ);

        private static final EnumSet<ApplicationUserPermission> librarianSet = EnumSet.of(
                ApplicationUserPermission.APP_USER_READ,ApplicationUserPermission.APP_USER_WRITE,
                ApplicationUserPermission.BOOK_READ, ApplicationUserPermission.BOOK_WRITE,
                ApplicationUserPermission.BORROWED_BOOKS_WRITE,ApplicationUserPermission.BORROWED_BOOKS_READ,
                ApplicationUserPermission.CATEGORY_READ, ApplicationUserPermission.CATEGORY_WRITE,
                ApplicationUserPermission.PHOTO_READ, ApplicationUserPermission.PHOTO_WRITE
        );
    }
    private final EnumSet<ApplicationUserPermission> permissions;


    AppUserRole(EnumSet<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public EnumSet<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        simpleGrantedAuthorities.add(new SimpleGrantedAuthority(this.name()));
        return simpleGrantedAuthorities;
    }
}
