package com.test.helmes.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;


@Service
public class JwtTokenProvider {
    private final JwtConfig jwtConfig;


    @Autowired
    public JwtTokenProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }


    public boolean isValid(String token, String user) {
        return getUsernameFormToken(token).equals(user) && !hasExpired(token);


    }

    public JwtConfig getJwtConfig() {
        return jwtConfig;
    }

    public String generateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .addClaims(new HashMap<>())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getDurationInMS()))
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret())
                .compact();
    }

    public String getUsernameFormToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtConfig.getSecret())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean hasExpired(String token) {
        Date date =  Jwts.parser()
                .setSigningKey(jwtConfig.getSecret())
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return date.before(new Date());
    }


    public static void main(String[] args) {
        String jwt = Jwts.builder()
                .setSubject("test")
                .addClaims(new HashMap<>())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60*1000))
                .signWith(SignatureAlgorithm.HS512, "secret")
                .compact();
        System.out.println(jwt);
        JwtConfig jwtConfig1 = new JwtConfig();
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(jwtConfig1);

    }


}
