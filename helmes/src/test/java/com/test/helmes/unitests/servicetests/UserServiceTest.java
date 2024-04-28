package com.test.helmes.unitests.servicetests;

import com.test.helmes.config.security.jwt.JwtTokenProvider;
import com.test.helmes.dbos.user.UserDbo;
import com.test.helmes.dtos.user.LoginResponseDto;
import com.test.helmes.dtos.user.UserDto;
import com.test.helmes.errors.Error;
import com.test.helmes.repositories.user.UserRepository;
import com.test.helmes.services.mappers.MapperService;
import com.test.helmes.services.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
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
    private MapperService mapperService;

    @BeforeEach
    public void setUp() {
        Mockito.reset(userRepository, jwtTokenProvider, authenticationManager, passwordEncoder, mapperService);
    }

    /**
     * Test for registering a valid user.
     */
    @Test
    public void testRegisterValidUser() {
        UserDto userDto = new UserDto("testUser", "testPassword", null);
        UserDbo userDbo = new UserDbo(1L, "testUser", "encodedPassword");

        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(mapperService.convertToUserDbo(userDto)).thenReturn(userDbo);
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
        when(mapperService.convertToUserDbo(userDto)).thenReturn(userDbo);

        Error error = assertThrows(
                Error.class,
                () -> userService.register(userDto)
        );
        System.out.println(error.getMessage());
        assertEquals("User already exists", error.getMessage());
    }


    /**
     * This tests user login with valid credentials.
     */
    @Test
    public void testLoginValidUser() throws Error {
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

}
