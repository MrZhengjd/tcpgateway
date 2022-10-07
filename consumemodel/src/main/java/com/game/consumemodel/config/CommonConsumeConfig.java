package com.game.consumemodel.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "game.common.config")
public class CommonConsumeConfig {

    /**
     * 上线的发送方式选择
     */
    private int onlineSendWay;

    /**
     * 下线的发送方式选择
     */
    private int offlineSendWay;

    private int gameType;

    private int backRoomAppend ;

    private boolean backRoomOnUsed;
//    public int getOnlineSendWay() {
//        return onlineSendWay;
//    }
//
//    public void setOnlineSendWay(int onlineSendWay) {
//        this.onlineSendWay = onlineSendWay;
//    }
//
//    public int getOfflineSendWay() {
//        return offlineSendWay;
//    }
//
//    public void setOfflineSendWay(int offlineSendWay) {
//        this.offlineSendWay = offlineSendWay;
//    }
}
