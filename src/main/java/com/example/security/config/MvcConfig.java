package com.example.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/admin_home").setViewName("admin_home");
        registry.addViewController("/coach_home").setViewName("coach_home");
        registry.addViewController("/user_home").setViewName("user_home");
        registry.addViewController("/login").setViewName("login");
    }
}
