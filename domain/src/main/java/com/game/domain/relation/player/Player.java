package com.game.domain.relation.player;


import com.game.domain.relation.role.PlayerRole;

import java.io.Serializable;

/**
 * @author zheng
 * 这里是一个玩家操作一个角色
 */
public class Player implements Serializable {
    private Long playerId;
    private PlayerBaseInfo baseInfo;
    private Long gold;
    //一个玩家一次只能绑定一个角色进行游戏
    private PlayerRole playerRole;

    public Player(Long playerId, PlayerBaseInfo baseInfo, PlayerRole playerRole) {
        this.playerId = playerId;
        this.baseInfo = baseInfo;
        this.playerRole = playerRole;
        this.gold = 0l;
    }

    public PlayerRole getPlayerRole() {
        return playerRole;
    }

    public void setPlayerRole(PlayerRole playerRole) {
        this.playerRole = playerRole;
    }

    public Long getGold() {
        return gold;
    }

    public void setGold(Long gold) {
        this.gold = gold;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public PlayerBaseInfo getBaseInfo() {
        return baseInfo;
    }

    public void setBaseInfo(PlayerBaseInfo baseInfo) {
        this.baseInfo = baseInfo;
    }



    public Player(Long playerId, PlayerBaseInfo baseInfo) {
        this.playerId = playerId;
        this.baseInfo = baseInfo;

    }
}
