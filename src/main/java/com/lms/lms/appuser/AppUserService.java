package com.lms.lms.appuser;

import com.lms.lms.error.AppUserNotFoundException;
import com.lms.lms.registration.confirmation.ConfirmationToken;
import com.lms.lms.registration.confirmation.ConfirmationTokenService;
import com.lms.lms.security.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//we find users that are trying to log in
@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "User with email %s was not found!";

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final JwtUtil jwtUtil;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
        return new User(appUser.getUsername(), appUser.getPassword(), appUser.getAuthorities());
    }

    public Map<String, String> signUpUser(AppUser appUser){
        boolean userExists = appUserRepository.findByEmail(appUser.getEmail()).isPresent();

        if (userExists) throw new IllegalStateException("A user with this email already exists!");

        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);

        appUserRepository.save(appUser);

        String jwt_token = jwtUtil.generateToken(appUser.getEmail(),appUser.getAuthorities());


        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(20),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        Map<String, String> map = new HashMap<>();
        map.put("jwt-token", jwt_token);
        map.put("confirmation-token", token);
        return map;
    }

    public AppUser getAppUserById(Long id){
        return appUserRepository.findById(id).orElseThrow(() -> new AppUserNotFoundException(id));
    }

    public AppUser getAppUserByUsername(String username){
        return appUserRepository.findByEmail(username).orElseThrow(()->new AppUserNotFoundException(username));
    }


    public void enableAppUser(String email) {
        this.loadUserByUsername(email);
        appUserRepository.enableAppUser(email);
    }

    public void deleteAppUser(String username){
        AppUser appUser = appUserRepository.findByEmail(username).orElseThrow(() -> new AppUserNotFoundException(username));

        appUserRepository.deleteById(appUser.getId());
    }
}
