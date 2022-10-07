package com.game.diststore;

import com.game.mj.cache.MasterInfo;
import com.game.mj.constant.InfoConstant;
import com.game.diststore.role.Recorder;
import com.game.diststore.service.RecordComponent;
import com.game.network.server.SuccessHandle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author zheng
 */
@SpringBootApplication(scanBasePackages = "com.game")
public class RecorderApplication {
    public static void main(String[] args) {
        MasterInfo.changeInfo("recorder",10025l);

        ConfigurableApplicationContext run = SpringApplication.run(RecorderApplication.class);
        Recorder recorder = run.getBean(Recorder.class);

        recorder.startGame(InfoConstant.LOCAL_HOST, InfoConstant.RECORDER_PORT, new SuccessHandle() {
            @Override
            public void afterSueccess() {
                RecordComponent recordComponent = run.getBean(RecordComponent.class);
                recordComponent.rollBack();
                recordComponent.init();
            }
        });

    }
}
