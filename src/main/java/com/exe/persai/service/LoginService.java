package com.exe.persai.service;

import com.exe.persai.model.entity.Users;
import com.exe.persai.model.exception.ResourceNotFoundException;
import com.exe.persai.model.response.LoginResponse;
import com.exe.persai.security.UserDetailsServiceImpl;
import com.exe.persai.security.jwt.JwtTokenProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${google.client-id}")
    private String WEB_GOOGLE_CLIENT_ID;
    @Value("${google.ios-client-id}")
    private String IOS_GOOGLE_CLIENT_ID;
    @Value("${google.other-client-id}")
    private String OTHER_GOOGLE_CLIENT_ID;
    private GoogleIdTokenVerifier verifier;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final RegisterService registerService;

    @PostConstruct
    public void initVerifier() {
        verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                // Specify the CLIENT_ID of the app that accesses the backend:
//                .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                // Or, if multiple clients access the backend:
                .setAudience(Arrays.asList(WEB_GOOGLE_CLIENT_ID, IOS_GOOGLE_CLIENT_ID, OTHER_GOOGLE_CLIENT_ID))
                .build();
    }
    public LoginResponse authenticateGoogle(String idToken) {
        GoogleIdToken googleIdToken;
        try {
            googleIdToken = verifier.verify(idToken);
        }
        catch (Exception e) {
            throw new AccessDeniedException("Invalid Google ID token");
        }

        if (googleIdToken != null) {
            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            String email = payload.getEmail();
            try {
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);
                //If email is valid, check account's status
                if (userDetails.isAccountNonLocked()) {
                    return jwtTokenProvider.generateTokenForLoginUser(userDetails);
                }
                // Account cannot be accessed
                throw new LockedException("Your account is locked");
            }
            catch (ResourceNotFoundException e) {
                // Email is valid but account doesn't exist in database
                // register new account with given email
                Users user = registerService.registerBasicUserViaGoogle(payload);
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);
                return jwtTokenProvider.generateTokenForLoginUser(userDetails);
            }
        }
        else {
            // Invalid token
            throw new BadCredentialsException("Invalid Google ID token or there is an error when authenticating with Google");
        }
    }
}
