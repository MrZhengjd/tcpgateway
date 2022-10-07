package com.game.gateway.server.sendway;

import com.game.mj.annotation.BandKey;
import com.game.mj.constant.InfoConstant;

import com.game.mj.model.GameMessage;
import com.game.network.cache.ChannelMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zheng
 */
@Component
@BandKey(key = InfoConstant.ONY_BY_MANY)
public class OneByManySendToPlayer implements SendToPlayer {
    private Logger logger = LoggerFactory.getLogger(OneByManySendToPlayer.class);
    @Override
    public void sendToPlayer(GameMessage message, ChannelMap channelMap) {
        List<Long> toPlayerIds = message.getHeader().getToPlayerIds();
        for (Long playerId : toPlayerIds){
            SendMessageUtil.sendMessage(message, channelMap,playerId);
        }
    }
}
