package com.jwtauth.api;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class AuthRequests {


    @Email
    private String email;
    private String password;

}
