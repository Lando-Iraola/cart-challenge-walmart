package com.test_shop.cart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class H2Config implements WebMvcConfigurer {
    // This ensures Spring Boot 4 doesn't treat /h2-console as a static file request
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/h2-console/**");
    }
}