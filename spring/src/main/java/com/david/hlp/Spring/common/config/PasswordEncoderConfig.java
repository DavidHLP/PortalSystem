package com.david.hlp.Spring.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.ComponentScan;
@Configuration
@ComponentScan("com.david.hlp.Spring.cloud.**")
public class PasswordEncoderConfig {
    @Bean(name = "BCryptPasswordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
