package com.game.domain.relation.room;


import com.game.domain.flow.model.GameFlow;

import java.util.Map;

/**
 * @author zheng
 */
public class GameRule {
    private int gameType;

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    private GameFlow gameFlow;
    private Map<String,Double> scoreMap;

    public GameFlow getGameFlow() {
        return gameFlow;
    }

    public void setGameFlow(GameFlow gameFlow) {
        this.gameFlow = gameFlow;
    }

    public Map<String, Double> getScoreMap() {
        return scoreMap;
    }

    public void setScoreMap(Map<String, Double> scoreMap) {
        this.scoreMap = scoreMap;
    }
}
