package com.game.waitstart;


import com.game.waitstart.config.NacosWaitStartConfig;
import com.game.waitstart.nameserver.RegisterGameService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * @author zheng
 */
@SpringBootApplication(scanBasePackages = "com.game")
@EnableKafka
public class WaitStartApplicatioin {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = new SpringApplicationBuilder(WaitStartApplicatioin.class).run(args);
//

        RegisterGameService service = run.getBean(RegisterGameService.class);
        service.registerService();
        NacosWaitStartConfig waitStartConfig = run.getBean(NacosWaitStartConfig.class);
        System.out.println(waitStartConfig.getName());

    }
}
