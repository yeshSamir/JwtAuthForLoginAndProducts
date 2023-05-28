package com.jwtauth.controller;

import com.jwtauth.api.AuthRequests;
import com.jwtauth.api.AuthResponse;
import com.jwtauth.entity.Users;
import com.jwtauth.util.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenUtils jwtTokenUtils;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(
            @RequestBody @Valid AuthRequests authRequests
    ) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequests.getEmail(), authRequests.getPassword())
            );
            Users user = (Users) authentication.getPrincipal();
            String accessToken=jwtTokenUtils.generateAccessToken(user);
            AuthResponse authResponse = new AuthResponse(user.getEmail(), accessToken);
            return ResponseEntity.ok(authResponse);

        } catch (BadCredentialsException badCredentialsException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        }

    }

}
