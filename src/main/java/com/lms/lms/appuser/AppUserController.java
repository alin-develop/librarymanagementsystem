package com.lms.lms.appuser;

import lombok.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/appuser")
public class AppUserController {
    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PreAuthorize("hasAuthority('LIBRARIAN') or principal.username == #username")
    @DeleteMapping(path = "/delete/{username}")
    public void deleteAppUser(@PathVariable("username") @NonNull String username){
        this.appUserService.deleteAppUser(username);
    }
}
