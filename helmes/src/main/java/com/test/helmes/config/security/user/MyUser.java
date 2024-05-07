package com.test.helmes.config.security.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


@Getter
@Setter
public class MyUser extends User {
    private UserRole userRole;

    public MyUser(String username, String password, Collection<? extends GrantedAuthority> authorities, UserRole userRole) {
        super(username, password, authorities);
        this.userRole = userRole;
    }

}
