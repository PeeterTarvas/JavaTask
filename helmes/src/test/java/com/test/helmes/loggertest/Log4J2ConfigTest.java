package com.test.helmes.loggertest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class Log4J2ConfigTest {

    @Test
    public void testLoggingLevels() {
        log.trace("Trace message is logged!");
        log.debug("Debug message is logged!");
        log.info("Info message is logged!");
        log.warn("Warning message is logged!");
        try {
            throw new RuntimeException("Unknown error");
        } catch (Exception e) {
            log.error("Error message is logged!", e);
        }
    }
}
