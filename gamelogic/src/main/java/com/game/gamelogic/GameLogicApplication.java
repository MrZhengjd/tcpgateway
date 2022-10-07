package com.game.gamelogic;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * @author zheng
 */
@SpringBootApplication(scanBasePackages = "com.game")
@EnableKafka
public class GameLogicApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = new SpringApplicationBuilder(GameLogicApplication.class).run(args);

    }
}
