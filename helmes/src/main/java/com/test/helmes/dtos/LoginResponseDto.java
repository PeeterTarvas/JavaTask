package com.test.helmes.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * This object is for sending the login response between the front-end and back-end,
 * it is the response that is sent when the user logs-in.
 */
@Setter
@Getter
public class LoginResponseDto {

    @NotBlank
    private String username;
    @NotBlank
    private String token;
    public LoginResponseDto(String username, String token) {
        this.username = username;
        this.token = token;
    }
}
