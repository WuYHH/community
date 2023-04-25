package com.example.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author wuyuhan
 * @date 2023/4/25 16:38
 */
@SpringBootTest
@ContextConfiguration(classes = Demo3Application.class)
public class LogbackTests {
    private static final Logger logger = LoggerFactory.getLogger(LogbackTests.class);

    @Test
    void test() {
        System.out.println("print log");
        logger.debug("logger debug...");
        logger.info("logger info...");
        logger.error("logger error...");
        logger.warn("logger warn...");
    }
}
