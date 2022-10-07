package com.game.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zheng
 */
@SpringBootApplication(scanBasePackages = "com.game")
public class AuthStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthStartApplication.class);
    }
}
