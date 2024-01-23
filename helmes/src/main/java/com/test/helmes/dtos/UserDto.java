package com.test.helmes.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;

/**
 * This object is for sending the users account details between the front-end and back-end.
 */
@Setter
@Getter
public class UserDto extends User {

    @NotNull
    private String userId;
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public UserDto(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, Collections.emptyList());
        this.username = username;
        this.password = password;
    }
}
