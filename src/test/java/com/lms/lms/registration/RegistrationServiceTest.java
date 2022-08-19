package com.lms.lms.registration;

import com.lms.lms.appuser.AppUser;
import com.lms.lms.appuser.AppUserRole;
import com.lms.lms.appuser.AppUserService;
import com.lms.lms.email.EmailSender;
import com.lms.lms.registration.confirmation.ConfirmationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.mail.AuthenticationFailedException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class RegistrationServiceTest {

    @Mock private AppUserService appUserService;
    @Mock private ConfirmationTokenService confirmationTokenService;
    @Mock private EmailValidator emailValidator;
    @Mock private EmailSender emailSender;
    @Captor private ArgumentCaptor<AppUser> appUserArgumentCaptor;
    private RegistrationService underTest;

    RegistrationRequest request = new RegistrationRequest("Name", "Surname",
            "emailsaddress@gmail.com", "difficultpassword");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new RegistrationService(appUserService, confirmationTokenService, emailValidator, emailSender);
    }

    @Test
    void register_ifEmailNotValid() {
        RegistrationRequest email_not_valid = new RegistrationRequest("Name","Surname","email","password");
        assertThrows(IllegalStateException.class, () -> underTest.register(email_not_valid) );
    }

    @Test
    void register_ifNull() {
        RegistrationRequest request_null = new RegistrationRequest("Name","Surname","Email@gmail.com",null);
        assertThrows(IllegalStateException.class, () -> underTest.register(request_null) );
    }

    @Test
    void register_ifEmpty() {
        RegistrationRequest request_empty = new RegistrationRequest("Name","Surname","Email@gmail.com","");
        assertThrows( IllegalStateException.class, () -> underTest.register(request_empty) );
    }

    @Test
    void register_ifValid() throws AuthenticationFailedException {
        given(emailValidator.test(any())).willReturn(true);
        //when
        Map<String, String> map = underTest.register(request);
        //then
        then(emailSender).should().send(any(), any());

        assertNotNull(map);
    }

    @Test
    void registerLibrarian_ifValid() {
        given(emailValidator.test(request.getEmail())).willReturn(true);
        underTest.registerLibrarian(request);

        then(appUserService).should().signUpUser(appUserArgumentCaptor.capture());
        assertEquals(AppUserRole.LIBRARIAN, appUserArgumentCaptor.getValue().getAppUserRole());
        assertEquals(request.getEmail(), appUserArgumentCaptor.getValue().getEmail());
        assertEquals(request.getName(), appUserArgumentCaptor.getValue().getFirstName());
        assertEquals(request.getSurname(), appUserArgumentCaptor.getValue().getLastName());
    }

}