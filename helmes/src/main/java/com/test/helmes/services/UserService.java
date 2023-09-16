package com.test.helmes.services;


import com.test.helmes.config.jwt.JwtTokenProvider;
import com.test.helmes.dbos.UserDbo;
import com.test.helmes.dtos.LoginResponseDto;
import com.test.helmes.dtos.UserDto;
import com.test.helmes.errors.InvalidDataException;
import com.test.helmes.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final ConverterService converterService;

    @Autowired
    public UserService(UserRepository repository,
                       JwtTokenProvider jwtTokenProvider,
                       AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder,
                       ConverterService converterService) {
        this.userRepository = repository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.converterService = converterService;
    }

    public void register(UserDto userDto) throws InvalidDataException {
        if (isValidUser(userDto)) {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            UserDbo userDbo = new UserDbo();
            userDbo.setUsername(userDto.getUsername());
            userDbo.setPassword(userDto.getPassword());
            try {
                userRepository.saveAndFlush(userDbo);
            } catch (DataAccessException ex) {
                throw new InvalidDataException("User already exists");
            }
        } else {
            throw new InvalidDataException("No username or password");
        }
    }

    public LoginResponseDto login(UserDto userDto) throws InvalidDataException {
        if (isValidUser(userDto)) {
            Authentication authentication;
            try {
                 authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                userDto.getUsername(), userDto.getPassword()));
            } catch (BadCredentialsException e) {
                throw new InvalidDataException("Bad credentials");
            }
            UserDto principle = (UserDto) authentication.getPrincipal();
            String token = jwtTokenProvider.generateToken(principle.getUsername());
            return new LoginResponseDto(principle.getUsername(), token);
        } else {
            throw new InvalidDataException("Wrong username or password");
        }
    }

    private boolean isValidUser(UserDto userDto) {
        return (!userDto.getUsername().isBlank() && !userDto.getPassword().isBlank());
    }
}