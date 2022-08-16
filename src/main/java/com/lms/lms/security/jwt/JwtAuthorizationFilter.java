package com.lms.lms.security.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.lms.lms.appuser.AppUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@AllArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Autowired
    private final JwtUtil jwtUtil;
    @Autowired
    private final AppUserService appUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("Start of jwt filter.");
        if(!request.getServletPath().equals("/api/v1/registration")&&!request.getServletPath().equals("/api/v1/auth/login")){
            String authorization = request.getHeader("Authorization");
            if(authorization!=null && authorization.startsWith("Bearer ")){
                log.info("Request has a jwt token.");
                try{
                    String token = authorization.substring("Bearer ".length());
                    String email = jwtUtil.validateTokenAndRetrieveSubject(token);
                    UserDetails userDetails = appUserService.loadUserByUsername(email);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(email, null, userDetails.getAuthorities());
                    if(SecurityContextHolder.getContext().getAuthentication() == null){
                        log.info("GetAuthentication() is null.");
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }catch(JWTVerificationException exc){
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token");
                }
            }
        }
        filterChain.doFilter(request,response);
        log.info("End of jwt filter.");
    }
}
