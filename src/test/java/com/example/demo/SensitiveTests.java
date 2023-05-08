package com.example.demo;

import com.example.demo.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author wuyuhan
 * @date 2023/5/8 16:10
 */
@SpringBootTest
@ContextConfiguration(classes = Demo3Application.class)
public class SensitiveTests {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    void testFilter() {
        String words = sensitiveFilter.filter("llll, ⭐吸⭐毒⭐，哈哈哈哈");
        System.out.println(words);
    }
}
