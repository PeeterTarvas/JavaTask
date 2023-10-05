package com.test.helmes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.sql.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Configurer class for configuring cors.
 */
@Configuration
public class CorsConfig  {

    /**
     * CORS must be processed before Spring Security, because the pre-flight request does not contain any cookies (that is, the JSESSIONID).
     * If the request does not contain any cookies and Spring Security is first,
     * the request determines that the user is not authenticated (since there are no cookies in the request) and rejects it.
     * @return a cors mapping to the front-end side for 4 main http methods.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200", "http://localhost:80", "http://localhost")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowCredentials(true)
                        .allowedHeaders("*")
                        .exposedHeaders("*");
            }
        };
    }
}