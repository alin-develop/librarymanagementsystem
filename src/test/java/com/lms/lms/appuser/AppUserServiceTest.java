package com.lms.lms.appuser;

import com.lms.lms.error.AppUserNotFoundException;
import com.lms.lms.registration.confirmation.ConfirmationToken;
import com.lms.lms.registration.confirmation.ConfirmationTokenService;
import com.lms.lms.security.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @Mock
    private JwtUtil jwtUtil;
    private AppUserService underTest;

    AppUser user1 = new AppUser("Tessa", "Thompson", "tessathompson@gmail.com",
            "thompsontessa2022", AppUserRole.LIBRARIAN);
    AppUser user2 = new AppUser("Dolores", "Abernathy", "doloresabernathy@gmail.com",
            "abernathydolores2022", AppUserRole.USER);

    @BeforeEach
    void setUp() {
        underTest = new AppUserService(appUserRepository, bCryptPasswordEncoder,
                confirmationTokenService, jwtUtil);
    }

    @Test
    void loadUserByUsername_givenUsernameExists_ReturnUser() {
        //given
        String email = "tessathompson@gmail.com";
        given(appUserRepository.findByEmail(email)).willReturn(Optional.ofNullable(user1));

        //when
        underTest.loadUserByUsername(email);
        //then
        verify(appUserRepository).findByEmail(email);



    }

    @Test
    void loadUserByUsername_givenUsernameNotExists_ThrowException() {
        //given
        String email = "emaildoesnotexist@gmail.com";
        //when
        assertThrows(UsernameNotFoundException.class, () -> underTest.loadUserByUsername(email));

    }

    @Test
    void signUpUser_givenUserAlreadyExists() {
        String email = user1.getEmail();

        given(appUserRepository.findByEmail(email)).willReturn(Optional.ofNullable(user1));

        assertThrows(IllegalStateException.class, () -> underTest.signUpUser(user1));

    }

    @Test
    void signUpUser_givenUserDoesNotExist() {
        String email = user2.getEmail();
        //when
        given(appUserRepository.findByEmail(email)).willReturn(Optional.empty());
        underTest.signUpUser(user2);
        //then
        verify(appUserRepository).save(user2);
        verify(confirmationTokenService).saveConfirmationToken(any(ConfirmationToken.class));
    }

    @Test
    void getAppUserById_givenAppUserExists() {
        //given
        Long id = user1.getId();
        //when
        given(appUserRepository.findById(id)).willReturn(Optional.ofNullable(user1));
        AppUser appUserById = underTest.getAppUserById(id);
        //then
        assertEquals(user1, appUserById);
    }

    @Test
    void getAppUserById_givenAppUserDoesNotExist() {
        //given
        Long id = user1.getId();
        //when
        given(appUserRepository.findById(id)).willReturn(Optional.empty());
        //then
        assertThrows(AppUserNotFoundException.class, () -> underTest.getAppUserById(id) );
    }

    @Test
    void enableAppUser() {
        String email = user1.getEmail();

        given(appUserRepository.findByEmail(email)).willReturn(Optional.ofNullable(user1));
        underTest.enableAppUser(email);

        verify(appUserRepository).enableAppUser(email);
    }
}