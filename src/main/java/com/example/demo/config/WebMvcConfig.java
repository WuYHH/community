package com.example.demo.config;

import com.example.demo.interceptor.FirstInterceptor;
import com.example.demo.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author wuyuhan
 * @date 2023/5/5 21:25
 */
/**
 * 配置类可以自动运行
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private FirstInterceptor firstInterceptor;

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(firstInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg")
                .addPathPatterns("/register", "/login");

        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

    }
}
