package com.lms.lms.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.lms.appuser.AppUserService;
import com.lms.lms.authentication.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    private final AppUserService appUserService;

    public AuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, AppUserService appUserService) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.appUserService = appUserService;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            log.info("Start of auth filter attempt auth.");
            LoginRequest credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequest.class);

            UserDetails userDetails = appUserService.loadUserByUsername(credentials.getUsername());


            return this.getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(),
                            credentials.getPassword(),
                            userDetails.getAuthorities())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                      FilterChain chain, Authentication auth) throws IOException, ServletException {
        log.info("Start of auth filter successful auth.");
        String token = jwtUtil.generateToken(((UserDetails) auth.getPrincipal()).getUsername(), auth.getAuthorities());
        response.addHeader("Authorization", "Bearer " + token);
    }

}
