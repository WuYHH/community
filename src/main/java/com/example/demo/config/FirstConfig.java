package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * @author wuyuhan
 * @date 2023/4/20 16:38
 */
@Configuration
public class FirstConfig {

    // 装配外部的bean
    @Bean
    public SimpleDateFormat simpleDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
