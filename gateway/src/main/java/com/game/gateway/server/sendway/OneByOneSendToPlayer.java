package com.game.gateway.server.sendway;

import com.game.mj.annotation.BandKey;
import com.game.mj.constant.InfoConstant;

import com.game.mj.model.GameMessage;
import com.game.network.cache.ChannelMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author zheng
 */
@Component
@BandKey(key = InfoConstant.ONY_BY_ONE)
public class OneByOneSendToPlayer implements SendToPlayer {
    private Logger logger = LoggerFactory.getLogger(OneByOneSendToPlayer.class);
    @Override
    public void sendToPlayer(GameMessage message, ChannelMap channelMap) {
       SendMessageUtil.sendMessage(message, channelMap,message.getHeader().getPlayerId());
    }
}
