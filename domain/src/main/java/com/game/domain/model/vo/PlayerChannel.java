package com.game.domain.model.vo;

import io.netty.channel.Channel;

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

    //    private Channel channel;

//    public PlayerChannel(Long playerId, Channel channel) {
//        this.playerId = playerId;
//        this.channel = channel;
//    }

    public PlayerChannel() {
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

//    public Channel getChannel() {
//        return channel;
//    }
//
//    public void setChannel(Channel channel) {
//        this.channel = channel;
//    }
}
