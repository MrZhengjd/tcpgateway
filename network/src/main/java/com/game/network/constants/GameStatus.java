package com.game.network.constants;

/**
 * @author zheng
 */
public enum  GameStatus {
    GAME_READY(0),GAME_PLAYER(1),GAME_POINT(2),GAME_OVER(3);
    private int gameStatus;

    GameStatus(int gameStatus) {
        this.gameStatus = gameStatus;
    }

    public int getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(int gameStatus) {
        this.gameStatus = gameStatus;
    }
}
