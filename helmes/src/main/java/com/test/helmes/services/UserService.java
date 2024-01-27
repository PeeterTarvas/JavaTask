package com.test.helmes.services;

import com.test.helmes.errors.Error;
import com.test.helmes.config.jwt.JwtTokenProvider;
import com.test.helmes.dbos.UserDbo;
import com.test.helmes.dtos.LoginResponseDto;
import com.test.helmes.dtos.UserDto;
import com.test.helmes.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for all user related activities - login and register.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final MapperService mapperService;

    @Autowired
    public UserService(UserRepository repository,
                       JwtTokenProvider jwtTokenProvider,
                       AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder,
                       MapperService mapperService) {
        this.userRepository = repository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.mapperService = mapperService;
    }

    /**
     * This method registers the users account by:
     *  - checking if the users details are valid
     *  - encoding their password
     *  - save the account
     * @param userDto with the details of the account.
     * @throws Error if user already exists(username already exists) or their data is invalid.
     */
    @Transactional()
    public void register(UserDto userDto) throws Error {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            UserDbo userDbo = mapperService.convertToUserDbo(userDto);
            try {
                userRepository.saveAndFlush(userDbo);
            } catch (Exception e) {
                throw new Error("User already exists");
            }
    }

    /**
     * This method is for users to login to their account.
     * Steps are:
     * - it validates login details with isValidUser method.
     * - if it is valid then authenticate the user by their password, with authentication manager
     * - get the users details object by with the principal - user details - object returned by the authentication
     * - Generate the token with the users token
     * @param userDto with the account details of the user.
     * @return a data transfer object that contains the accounts username and jwt.
     * @throws Error if password or username is incorrect or when their data isn't valid.
     */
    public LoginResponseDto login(UserDto userDto) {
            Authentication authentication;
            authentication = authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           userDto.getUsername(), userDto.getPassword()));
            UserDto principle = (UserDto) authentication.getPrincipal();
            String token = jwtTokenProvider.generateToken(principle.getUsername());
            return new LoginResponseDto(principle.getUsername(), token);
    }


}
