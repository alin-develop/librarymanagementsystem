package com.lms.lms.appuser;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppUserConfig {

    CommandLineRunner appUserconfig(AppUserRepository appUserRepository){
        return args -> {
//            AppUser jane = new AppUser("Jane","Doe",
//                    "janedoe@gmail.com", "doejane2022", AppUserRole.USER);
//            appUserRepository.save(jane);
        };
    }
}
