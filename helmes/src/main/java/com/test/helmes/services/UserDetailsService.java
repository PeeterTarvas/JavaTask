package com.test.helmes.services;

import com.test.helmes.dbos.UserDbo;
import com.test.helmes.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for handling users accounts details related logic that implements UserDetailsService provided by
 * springboot security.
 * This is a helper service used in JwtRequestFilter.
 */
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;
    private final MapperService mapperService;

    @Autowired
    public UserDetailsService(UserRepository userRepository, MapperService mapperService) {
        this.userRepository = userRepository;
        this.mapperService = mapperService;
    }

    /**
     * Method that overrides the loadUserByUsername in the original UserDetailsService.
     * It gets an optional by making a database request with the users account username.
     * If optional isn't empty then it returns the Dto of the user that hold the details of the account.
     * @param username the username identifying the user whose data is required.
     * @return userDto that holds the account details of the users account
     * @throws UsernameNotFoundException if an account with that username doesn't exists.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws Error {
        Optional<UserDbo> userDbo = userRepository.getUserDboByUsername(username);
        if (userDbo.isEmpty()) {
            throw new Error("No user found");
        }
        return mapperService.convertToUserDto(userDbo.get());
    }
}
