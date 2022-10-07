package com.game.domain.relation.role;


/**
 * @author zheng
 * 一个玩法对应一个角色
 * 每个角色身上的构成都不一样
 */
public enum  GameRole {
    //默认玩法角色
    JIANG_NAN(1, PlayerRoleManager.defaultPlayerRole());
    private int gameType;
    private PlayerRole role;

    GameRole(int gameType, PlayerRole role) {
        this.gameType = gameType;
        this.role = role;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public PlayerRole getRole() {
        return role;
    }

    public void setRole(PlayerRole role) {
        this.role = role;
    }
}
