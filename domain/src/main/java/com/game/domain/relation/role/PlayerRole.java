package com.game.domain.relation.role;

import com.game.mj.eventdispatch.Event;
import com.game.domain.relation.organ.Organ;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;


/**
 * @author zheng
 */
public class PlayerRole extends BaseRole implements Cloneable, Observer {
    private Long playerId;

    private int playerIndex;
    private Map<String ,Object> checkInfo = new HashMap<>();

    public Map<String, Object> getCheckInfo() {
        return checkInfo;
    }

    public void setCheckInfo(Map<String, Object> checkInfo) {
        this.checkInfo = checkInfo;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    private double currentRoundScore;
    private double totalRoundScore;

    public double getCurrentRoundScore() {
        return currentRoundScore;
    }

    public void setCurrentRoundScore(double currentRoundScore) {
        this.currentRoundScore = currentRoundScore;
    }

    public double getTotalRoundScore() {
        return totalRoundScore;
    }

    public void setTotalRoundScore(double totalRoundScore) {
        this.totalRoundScore = totalRoundScore;
    }

    @Override
    public void update(Observable o, Object arg) {
        setCurrentRoundScore((Double) arg);
    }


    public PlayerRole(Map<String, Organ> organMap) {

        super(organMap);
    }

    public static PlayerRole getInstance(){
        return Holder.instance;
    }
    private static class Holder{
        private static PlayerRole instance = new PlayerRole(new HashMap<>());
    }

//    private PlayerRole buildWithOrganFromInstance(Organ... organs){
//        PlayerRole instance = getInstance();
//        Map<String, Organ> organMap = instance.organMap;
//        for (Organ organ : organs){
//            addOrgan(organMap,organ);
//        }
//        return instance;
//    }




    public void callEvent(Event event,Object object){
//        EventAnnotationManager.getInstance().sendEvent(event,object);
    }


}
