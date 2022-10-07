package com.game.gateway.server.sendway;

import com.game.mj.model.GameMessage;
import com.game.network.cache.ChannelMap;

/**
 * @author zheng
 */
public interface SendToPlayer {
    void sendToPlayer(GameMessage message, ChannelMap channelMap);
}
