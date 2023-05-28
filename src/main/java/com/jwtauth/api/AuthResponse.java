package com.jwtauth.api;

import lombok.Data;

@Data
public class AuthResponse {
    private String email;
    private  String accessToken;

    public AuthResponse(String email,String accessToken) {
        this.email = email;
        this.accessToken=accessToken;
    }
}
