package com.game.gateway.server.sendway;

import com.game.mj.annotation.BandKey;
import com.game.mj.constant.InfoConstant;
import com.game.mj.model.GameMessage;
import com.game.network.cache.ChannelMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
@Component
public class SendToPlayerProxy {
    private Map<String,SendToPlayer> sendToPlayerMap = new HashMap<>();
    @Autowired
    private ApplicationContext context;
    @PostConstruct
    public void init(){
        Map<String, SendToPlayer> beansOfType = context.getBeansOfType(SendToPlayer.class);
        for (SendToPlayer sendToPlayer : beansOfType.values()){
            BandKey annotation = sendToPlayer.getClass().getAnnotation(BandKey.class);
            if (annotation != null){
                sendToPlayerMap.put(InfoConstant.SEND_TO_PLAYER_PREFIX+annotation.key(),sendToPlayer);
            }
        }
    }
    public SendToPlayer getByKey(Integer key){
        return sendToPlayerMap.get(InfoConstant.SEND_TO_PLAYER_PREFIX+key);
    }
    public void sendMessage(Integer key, GameMessage message, ChannelMap channelMap){
        SendToPlayer sendToPlayer = getByKey(key);
        if (sendToPlayer != null){
            sendToPlayer.sendToPlayer(message, channelMap);
        }

    }
}
