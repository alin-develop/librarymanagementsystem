package com.lms.lms.appuser;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppUserRepositoryTest {
    @Autowired
    private AppUserRepository appUserRepository;

    AppUser user1 = new AppUser("Tessa", "Thompson", "tessathompson@gmail.com",
            "thompsontessa2022", AppUserRole.LIBRARIAN);



    @Test
    void findByEmail_givenEmailExistsInDatabase_Return() {
        //given
        appUserRepository.save(user1);
        //when
        Optional<AppUser> byEmail = appUserRepository.findByEmail(user1.getEmail());

        //then
        assertNotNull(byEmail);
        assertEquals(user1.getEmail(), byEmail.get().getEmail());
    }

    @Test
    void findByEmail_givenEmailDoesNotExistInDatabase_ThrowException() {
        //given

        //when
        Optional<AppUser> optionalAppUser = appUserRepository.findByEmail(user1.getEmail());
        //then
        assertEquals(Optional.empty(), optionalAppUser);
    }

    @Test
    void enableAppUser_givenAppUserExistsAndEnabledIsFalse_ThenUpdateEnabledIsTrue() {
        //given
        appUserRepository.save(user1);
        //when
        int enableAppUser = appUserRepository.enableAppUser(user1.getEmail());
        //then
        assertEquals(true, appUserRepository.findByEmail(user1.getEmail()).get().getEnabled());
    }

    @Test
    void enableAppUser_givenAppUserExistsAndEnabledIsTrue_ThenUpdateEnabledIsTrue() {
        //given
        appUserRepository.save(user1);
        appUserRepository.enableAppUser(user1.getEmail());
        //when
        appUserRepository.enableAppUser(user1.getEmail());
        //then
        assertEquals(true,
                appUserRepository.findByEmail(user1.getEmail()).get().getEnabled());
    }

    @Test
    void enableAppUser_givenAppUserDoesNotExist_ThenThrows() {
        //then
        assertEquals(0,appUserRepository.enableAppUser(user1.getEmail()));
    }

    @AfterEach
    void tearDown() {
        appUserRepository.deleteAll();
    }
}