package com.lms.lms.registration;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {
    private final EmailValidator underTest;

    public EmailValidatorTest() {
        this.underTest = new EmailValidator();
    }

    @ParameterizedTest
    @CsvSource({
            "emails@gmail.com,true",
            "email@gmail,false",
            "@gmail.com,false",
            "addressgmail.com, false",
            "addressgmailcom, false",
            "emailsaddress@gmail.com, true"
    })
    void test1(String email, boolean expected) {
        boolean result = underTest.test(email);
        assertEquals(expected, result);
    }
}