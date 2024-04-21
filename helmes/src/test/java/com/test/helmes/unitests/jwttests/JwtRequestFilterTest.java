package com.test.helmes.unitests.jwttests;

import com.test.helmes.config.security.jwt.JwtRequestFilter;
import com.test.helmes.config.security.jwt.JwtTokenProvider;
import com.test.helmes.services.user.UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import jakarta.servlet.FilterChain;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtRequestFilterTest {

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserDetailsService userDetailsService;

    @Spy
    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * This tests the filtering of a valid token chain.
     */
    @Test
    public void testDoFilterInternalValidToken() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String validToken = "validToken";
        when(jwtRequestFilter.getToken(request)).thenReturn(Optional.of(validToken));
        when(jwtTokenProvider.getUsernameFormToken(validToken)).thenReturn("username");

        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        UserDetails userDetails = new User("username", "password", new ArrayList<>());
        when(userDetailsService.loadUserByUsername("username")).thenReturn(userDetails);
        when(jwtTokenProvider.isValid(validToken, userDetails.getUsername())).thenReturn(true);
        when(jwtRequestFilter.getUsername(any(Optional.class))).thenReturn(userDetails.getUsername());
        jwtRequestFilter.setUserDetails(userDetailsService);
        jwtRequestFilter.setJwtTokenProvider(jwtTokenProvider);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtTokenProvider).isValid(validToken, "username");
        verify(jwtRequestFilter).buildAuthToken(userDetails, request);
    }

    /**
     * This tests invalid token in the chain.
     */
    @Test
    public void testDoFilterInternalInvalidToken() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String invalidToken = "invalidToken";
        when(jwtRequestFilter.getToken(request)).thenReturn(Optional.of(invalidToken));
        when(jwtTokenProvider.getUsernameFormToken(invalidToken)).thenThrow(new RuntimeException());

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoMoreInteractions(userDetailsService);
    }

    /**
     * This tests the filter when there is no token provided.
     */
    @Test
    public void testDoFilterInternalNoToken() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(jwtRequestFilter.getToken(request)).thenReturn(Optional.empty());

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtRequestFilter).getToken(request);
        verifyNoMoreInteractions(jwtTokenProvider);
        verifyNoMoreInteractions(userDetailsService);
    }

    /**
     * Test the filter if there is no user found in the token.
     */
    @Test
    public void testDoFilterInternalUserNotFound() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String validToken = "validToken";
        doReturn(Optional.of(validToken)).when(jwtRequestFilter).getToken(request);
        when(jwtTokenProvider.getUsernameFormToken(validToken)).thenReturn("username");
        when(jwtRequestFilter.getUsername(any(Optional.class))).thenThrow(new UsernameNotFoundException("User not found"));

        jwtRequestFilter.setUserDetails(userDetailsService);
        assertThatThrownBy(() -> jwtRequestFilter.doFilterInternal(request, response, filterChain))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}