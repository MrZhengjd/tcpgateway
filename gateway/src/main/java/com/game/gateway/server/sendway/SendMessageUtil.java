package com.game.gateway.server.sendway;


import com.game.mj.model.GameMessage;
import com.game.mj.model.PlayerChannel;
import com.game.network.cache.ChannelMap;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zheng
 */
//@Log4j2
public class SendMessageUtil {
    private static Logger logger = LoggerFactory.getLogger(SendMessageUtil.class);
    public static void sendMessage(GameMessage message, ChannelMap channelMap, Long playerId){
        PlayerChannel byPlayerId = channelMap.getByPlayerId(playerId);
        if (byPlayerId == null){
            logger.error("receive data error"+playerId);
            return;
        }

        Channel channel = byPlayerId.getChannel();
        channel.writeAndFlush(message);
    }
}
