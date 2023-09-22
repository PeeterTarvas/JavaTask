package com.test.helmes.services;

import com.test.helmes.dbos.UserDbo;
import com.test.helmes.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;
    private final ConverterService converterService;

    @Autowired
    public UserDetailsService(UserRepository userRepository, ConverterService converterService) {
        this.userRepository = userRepository;
        this.converterService = converterService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDbo> userDbo = userRepository.getUserDboByUsername(username);
        if (userDbo.isEmpty()) {
            throw new UsernameNotFoundException("No user found");
        }
        return converterService.convertToUserDto(userDbo.get());
    }
}
