package com.test.helmes.dtos;


import lombok.Getter;
import lombok.Setter;

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
