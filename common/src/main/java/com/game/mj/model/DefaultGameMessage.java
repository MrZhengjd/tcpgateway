package com.game.mj.model;


import com.game.mj.eventdispatch.Event;

/**
 * @author zheng
 */
public class DefaultGameMessage<T> extends AbstractGameMessage<T> implements Event {


    public DefaultGameMessage() {

        setHeader(new THeader(MessageType.RPCREQUEST.value));
    }
}
