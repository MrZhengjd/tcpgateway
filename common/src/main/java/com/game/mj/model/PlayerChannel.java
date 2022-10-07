package com.game.mj.model;

import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.Serializable;

/**
 * @author zheng
 */
public class PlayerChannel implements Serializable {
    private Long playerId;
    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public PlayerChannel(Long playerId, Channel channel) {
        this.playerId = playerId;
        this.channel = channel;
    }

    public PlayerChannel() {
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public void writeMessage(GameMessage gameMessage){
        channel.writeAndFlush(gameMessage).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()){
//                    System.out.println("seccess send message ");
                }
            }
        });
    }
}
