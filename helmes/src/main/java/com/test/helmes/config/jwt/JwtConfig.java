package com.test.helmes.config.jwt;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtConfig {

    private String secret;
    private int durationMin;

    public int getDurationInMS() {
        return durationMin * 60 * 1000;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setDurationMin(int durationMin) {
        this.durationMin = durationMin;
    }

}
