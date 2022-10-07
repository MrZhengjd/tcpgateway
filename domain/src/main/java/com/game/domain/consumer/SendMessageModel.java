package com.game.domain.consumer;

import com.game.mj.model.GameMessage;

/**
 * @author zheng
 */
public interface SendMessageModel {
    public void sendMessageToMq(GameMessage data, String baseTopic, Object key);


}
