package com.test.helmes.servicetests;

import com.test.helmes.config.jwt.JwtTokenProvider;
import com.test.helmes.dbos.UserDbo;
import com.test.helmes.dtos.LoginResponseDto;
import com.test.helmes.dtos.UserDto;
import com.test.helmes.errors.InvalidDataException;
import com.test.helmes.repositories.UserRepository;
import com.test.helmes.services.ConverterService;
import com.test.helmes.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private ConverterService converterService;

    @BeforeEach
    public void setUp() {
        Mockito.reset(userRepository, jwtTokenProvider, authenticationManager, passwordEncoder, converterService);
    }

    /**
     * Test for registering a valid user.
     */
    @Test
    public void testRegisterValidUser() {
        UserDto userDto = new UserDto("testUser", "testPassword", null);
        UserDbo userDbo = new UserDbo(1L, "testUser", "encodedPassword");

        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(converterService.convertToUserDbo(userDto)).thenReturn(userDbo);
        when(userRepository.saveAndFlush(userDbo)).thenReturn(userDbo);

        assertDoesNotThrow(() -> userService.register(userDto));

    }

    /**
     * Test for registering a user that already exists.
     */
    @Test
    public void testRegisterUserAlreadyExists() {
        UserDto userDto = new UserDto("existingUser", "testPassword", null);
        UserDbo userDbo = new UserDbo(100L, "existingUser", "testPassword");

        when(userRepository.saveAndFlush(any(UserDbo.class))).thenThrow(new DataAccessException("Already exists"){});
        when(converterService.convertToUserDbo(userDto)).thenReturn(userDbo);

        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> userService.register(userDto)
        );
        assertEquals("User already exists", exception.getMessage());
    }

    /**
     * Test for registering an invalid user.
     */
    @Test
    public void testRegisterInvalidUser() {
        UserDto userDto = new UserDto(" ", " ", null);

        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> userService.register(userDto)
        );
        assertEquals("No username or password", exception.getMessage());
    }

    /**
     * This tests user login with valid credentials.
     */
    @Test
    public void testLoginValidUser() throws InvalidDataException {
        UserDto userDto = new UserDto("testUser", "testPassword", null);
        Authentication authentication = Mockito.mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);  // Mocking the authenticate method
        doReturn("jwtToken").when(jwtTokenProvider).generateToken(any(String.class));
        Mockito.when(authentication.getPrincipal()).thenReturn(userDto);
        LoginResponseDto response = userService.login(userDto);

        assertNotNull(response);
        assertEquals(userDto.getUsername(), response.getUsername());
        assertEquals("jwtToken", response.getToken());
    }

    /**
     * Test user login with invalid credentials.
     */
    @Test
    public void testLoginInvalidUser() {
        UserDto userDto = new UserDto(" ", " ", null);

        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> userService.login(userDto)
        );
        assertEquals("Wrong username or password", exception.getMessage());
    }

    /**
     * Test for user login with bad credentials.
     */
    @Test
    public void testLoginBadCredentials() {
        UserDto userDto = new UserDto("testUser", "testPassword", null);


        doThrow(BadCredentialsException.class).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> userService.login(userDto)
        );
        assertEquals("Bad credentials", exception.getMessage());
    }

    /**
     * Method tests checking the validity of a valid user.
     */
    @Test
    public void testIsValidUserValidUser() {
        UserDto userDto = new UserDto("testUser", "testPassword", null);

        boolean isValid = userService.isValidUser(userDto);

        assertTrue(isValid);
    }

    /**
     * Test checking the validity of an invalid user.
     */
    @Test
    public void testIsValidUserInvalidUser() {
        UserDto userDto = new UserDto(" ", " ", null);

        boolean isValid = userService.isValidUser(userDto);

        assertFalse(isValid);
    }
}
