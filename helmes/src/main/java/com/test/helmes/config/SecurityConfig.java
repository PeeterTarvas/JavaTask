package com.test.helmes.config;


import com.test.helmes.config.jwt.JwtRequestFilter;
import com.test.helmes.config.jwt.RestAuthenticationEntryPoint;
import com.test.helmes.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Class for setting the configuration for the back-end security.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true) // secureEnabled make spring use @Secured
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;
    private final UserDetailsService myUserDetailsService;

    @Autowired
    public SecurityConfig(JwtRequestFilter jwtRequestFilter, RestAuthenticationEntryPoint authenticationEntryPoint,
                          UserDetailsService myUserDetailsService) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.myUserDetailsService = myUserDetailsService;
    }

    /**
     * This method configures the security filter chain for your Spring Security configuration.
     * It defines how incoming HTTP requests should be handled in terms of authentication and authorization
     * with the jwtRequestFilter.
     * @param http The HttpSecurity object used to configure security filters.
     * @return A SecurityFilterChain that specifies the security rules for various endpoints.
     * @throws Exception If there is an error while configuring security.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/company/**").permitAll()
                        .requestMatchers("/user/**").permitAll()
                        .requestMatchers("/sector/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((e) -> e.authenticationEntryPoint(authenticationEntryPoint))
                .build();
    }

    /**
     * @return a password encoder that can be used to encrypt passwords with.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Adds UserDetailsService to the spring boot authentication manager.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.myUserDetailsService);
    }

    /**
     * Creates a authentication manager getter that can be accessed as a bean in other objects.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


}
