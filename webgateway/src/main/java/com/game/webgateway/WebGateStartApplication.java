package com.game.webgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zheng
 */
@SpringBootApplication(scanBasePackages = "com.game")
public class WebGateStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebGateStartApplication.class);
    }
}
