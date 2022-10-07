package com.game.mj.util;

import com.game.mj.model.GameMessage;

/**
 * @author zheng
 */
public class MessageKeyUtil {
    public static Object getMessageKey(GameMessage message){
        Object key = message.getHeader().getPlayerId();
        if (message.getHeader().getAttribute() != null){
            key = message.getHeader().getAttribute();
        }
        if (key == null){
            key = 1;
        }
        return key;
    }

}
