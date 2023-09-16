package com.test.helmes.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;

@Setter
@Getter
public class UserDto extends User {

    private String userId;
    private String username;
    private String password;

    public UserDto(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, Collections.emptyList());
        this.username = username;
        this.password = password;
    }
}
