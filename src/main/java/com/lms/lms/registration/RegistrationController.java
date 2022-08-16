package com.lms.lms.registration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.AuthenticationFailedException;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/registration")
@RequiredArgsConstructor
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

    @PostMapping
    public Map<String, String> register(@RequestBody RegistrationRequest request) throws AuthenticationFailedException {
        return registrationService.register(request);
    }

    @PostMapping(path = "/librarian")
    @PreAuthorize("hasAuthority('app_user:write')")
    public Map<String, String> registerLibrarian(@RequestBody RegistrationRequest request){
        return registrationService.registerLibrarian(request);
    }

    @GetMapping
    public String confirm(@RequestParam("token") String token){
        return registrationService.confirmToken(token);
    }
}
