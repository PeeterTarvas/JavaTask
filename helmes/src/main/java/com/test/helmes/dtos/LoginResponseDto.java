package com.test.helmes.dtos;


import lombok.Getter;
import lombok.Setter;

/**
 * This object is for sending the login response between the front-end and back-end,
 * it is the response that is sent when the user logs-in.
 */
@Setter
@Getter
public class LoginResponseDto {

    private String username;
    private String token;
    public LoginResponseDto(String username, String token) {
        this.username = username;
        this.token = token;
    }
}
