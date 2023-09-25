package com.test.helmes.config.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;


@Lazy
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_ = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetails;

    @Lazy
    @Autowired
    public JwtRequestFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetails) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetails = userDetails;
    }

    /**
     * Provides the authentication token that tje user os authenticated with.
     * Creates a new token, adds the http request to it and then returns the newly generated token.
     */
    private UsernamePasswordAuthenticationToken buildAuthToken(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationToken;
    }

    /**
     * return the username inside a jwt.
     */
    private String getUsername(Optional<String> token) {
        try {
            return jwtTokenProvider.getUsernameFormToken(token.get());

        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * Return a token inside the http request.
     */
    public Optional<String> getToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        if (header == null || !header.startsWith(BEARER_)) {
            return Optional.empty();
        }
        return Optional.of(header.substring(BEARER_.length()));
    }

    /**
     * Main filtering method that is responsible for validating a request.
     * It checks it there is a username and if there is a token.
     *  - then gets the user and his token -> checks if the token is valid
     *      if yes then builds an authentication token and sets the
     */
    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
                                    jakarta.servlet.http.HttpServletResponse response,
                                    jakarta.servlet.FilterChain filterChain)
            throws jakarta.servlet.ServletException, IOException {
        Optional<String> token = getToken(request);
        if (token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        String username = getUsername(token);
        if (username == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails =  this.userDetails.loadUserByUsername(username);
            if (jwtTokenProvider.isValid(token.get(), userDetails.getUsername())) {
                // If token is valid, tell security that everything is ok
                UsernamePasswordAuthenticationToken authenticationToken = buildAuthToken(userDetails, request);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
