package com.lms.lms.email;

import javax.mail.AuthenticationFailedException;

public interface EmailSender {
    void send(String to, String email) throws AuthenticationFailedException;
}
