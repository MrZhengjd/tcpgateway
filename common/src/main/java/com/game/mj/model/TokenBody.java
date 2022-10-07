package com.game.mj.model;

/**
 * @author zheng
 */
public class TokenBody {
    public TokenBody(String openId, long userId, long playerId, String serverId, String[] param) {
        this.openId = openId;
        this.userId = userId;
        this.playerId = playerId;
        this.serverId = serverId;
        this.param = param;
    }

    private String openId;
    private long userId;
    private long playerId;
    private String serverId = "1";
    private String[] param;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String[] getParam() {
        return param;
    }

    public void setParam(String[] param) {
        this.param = param;
    }
}
