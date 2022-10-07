package com.game.mj.messagedispatch;


import com.game.mj.model.GameMessage;

/**
 * @author zheng
 */
public interface BaseHandler {
    void handlerRequest(GameMessage gameMessage);
}
