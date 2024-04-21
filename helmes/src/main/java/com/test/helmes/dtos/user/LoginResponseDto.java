package com.test.helmes.dtos.user;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * This object is for sending the login response between the front-end and back-end,
 * it is the response that is sent when the user logs-in.
 */
@Setter
@Getter
@ToString
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
