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

/**
 * Service class for all user related activities - login and register.
 */
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

    /**
     * This method registers the users account by:
     *  - checking if the users details are valid
     *  - encoding their password
     *  - save the account
     * @param userDto with the details of the account.
     * @throws InvalidDataException if user already exists(username already exists) or their data is invalid.
     */
    public void register(UserDto userDto) throws InvalidDataException {
        if (isValidUser(userDto)) {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            UserDbo userDbo = converterService.convertToUserDbo(userDto);
            try {
                userRepository.saveAndFlush(userDbo);
            } catch (DataAccessException ex) {
                throw new InvalidDataException("User already exists");
            }
        } else {
            throw new InvalidDataException("No username or password");
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
     * @throws InvalidDataException if password or username is incorrect or when their data isn't valid.
     */
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

    /**
     * Initial validation of the users details.
     * Checks if the username or password of the sent UserDto object isn't empty.
     * @param userDto with the accounts details.
     * @return true if the details aren't empty.
     */
    private boolean isValidUser(UserDto userDto) {
        return (!userDto.getUsername().isBlank() && !userDto.getPassword().isBlank());
    }
}
