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
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

import java.util.Optional;

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
        // Reset mock interactions before each test
        Mockito.reset(userRepository, jwtTokenProvider, authenticationManager, passwordEncoder, converterService);
    }

    @Test
    public void testRegister_ValidUser() {
        // Arrange
        UserDto userDto = new UserDto("testUser", "testPassword", null);
        UserDbo userDbo = new UserDbo(1L, "testUser", "encodedPassword");

        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(converterService.convertToUserDbo(userDto)).thenReturn(userDbo);
        when(userRepository.saveAndFlush(userDbo)).thenReturn(userDbo);

        // Act
        assertDoesNotThrow(() -> userService.register(userDto));

        // Assert
    }

    @Test
    public void testRegister_UserAlreadyExists() {
        // Arrange
        UserDto userDto = new UserDto("existingUser", "testPassword", null);
        UserDbo userDbo = new UserDbo(100L, "existingUser", "testPassword");

        when(userRepository.saveAndFlush(any(UserDbo.class))).thenThrow(new DataAccessException("Already exists"){});
        when(converterService.convertToUserDbo(userDto)).thenReturn(userDbo);

        // Act and Assert
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> userService.register(userDto)
        );
        assertEquals("User already exists", exception.getMessage());
    }

    @Test
    public void testRegister_InvalidUser() {
        // Arrange
        UserDto userDto = new UserDto(" ", " ", null);

        // Act and Assert
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> userService.register(userDto)
        );
        assertEquals("No username or password", exception.getMessage());
    }

    @Test
    public void testLogin_ValidUser() throws InvalidDataException {
        // Arrange
        UserDto userDto = new UserDto("testUser", "testPassword", null);
        UserDbo userDbo = new UserDbo(1L, "testUser", "encodedPassword");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);  // Mocking the authenticate method
        doReturn("jwtToken").when(jwtTokenProvider).generateToken(any(String.class));
        // Act
        LoginResponseDto response = userService.login(userDto);

        // Assert
        assertNotNull(response);
        assertEquals(userDto.getUsername(), response.getUsername());
        assertEquals("jwtToken", response.getToken());
    }

    @Test
    public void testLogin_InvalidUser() {
        // Arrange
        UserDto userDto = new UserDto(" ", " ", null);

        // Act and Assert
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> userService.login(userDto)
        );
        assertEquals("Wrong username or password", exception.getMessage());
    }

    @Test
    public void testLogin_BadCredentials() {
        // Arrange
        UserDto userDto = new UserDto("testUser", "testPassword", null);


        doThrow(BadCredentialsException.class).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act and Assert
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> userService.login(userDto)
        );
        assertEquals("Bad credentials", exception.getMessage());
    }

    @Test
    public void testIsValidUser_ValidUser() {
        // Arrange
        UserDto userDto = new UserDto("testUser", "testPassword", null);

        // Act
        boolean isValid = userService.isValidUser(userDto);

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void testIsValidUser_InvalidUser() {
        // Arrange
        UserDto userDto = new UserDto(" ", " ", null);

        // Act
        boolean isValid = userService.isValidUser(userDto);

        // Assert
        assertFalse(isValid);
    }
}
