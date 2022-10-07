package com.game.domain.relation.room;


import com.game.domain.relation.organ.Organ;
import com.game.domain.relation.pai.Pai;
import com.game.domain.relation.pai.PaiManager;
import com.game.domain.relation.player.PlayerBaseInfo;
import com.game.domain.relation.role.PlayerRole;
import io.netty.util.concurrent.ScheduledFuture;
import sun.misc.Contended;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zheng
 */
public class Room implements Serializable {
    private Integer roomNum;
    private Integer gameStatus;//0准备    1 开始游戏      2结算阶段       3游戏结束
    private AtomicInteger continueCount = new AtomicInteger(0);
    private Integer dataCount = 0;
    private Long lastPlayingTime = 0l;
    private int playerNumber;
    @Contended
    private volatile boolean startSaveRoom = false;
    @Contended
    private volatile boolean startTuoGuan = false;
    //定时刷新数据到缓存
    private ScheduledFuture<?> scheduledFuture;

    public ScheduledFuture<?> getScheduledFuture() {
        return scheduledFuture;
    }

    public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

    public boolean getStartTuoGuan() {
        return startTuoGuan;
    }

    public void setStartTuoGuan(boolean startTuoGuan) {
        this.startTuoGuan = startTuoGuan;
    }

    public boolean getStartSaveRoom() {
        return startSaveRoom;
    }

    public void setStartSaveRoom(boolean startSaveRoom) {
        this.startSaveRoom = startSaveRoom;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public Long getLastPlayingTime() {
        return lastPlayingTime;
    }

    public void setLastPlayingTime(Long lastPlayingTime) {
        this.lastPlayingTime = lastPlayingTime;
    }

    public Integer getDataCount() {
        return dataCount;
    }

    public void setDataCount(Integer dataCount) {
        this.dataCount = dataCount;
    }

    public void getAndIncrement(){
        continueCount.getAndIncrement();
    }

    public Integer getCount(){
        return continueCount.get();
    }

    public Integer getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(Integer gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Integer getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(Integer roomNum) {
        this.roomNum = roomNum;
    }

    /**
     * 玩家的基础数据是固定不变的，所以可以分开存储
     * 后面可以根据需要调整
     */


    private Map<Long, PlayerBaseInfo> baseInfoMap = new HashMap<>();

    public Map<Long, PlayerBaseInfo> getBaseInfoMap() {
        return baseInfoMap;
    }

    public void setBaseInfoMap(Map<Long, PlayerBaseInfo> baseInfoMap) {
        this.baseInfoMap = baseInfoMap;
    }

    private Map<Long, PlayerRole> playerMap = new HashMap<>();
//    private Map<Long, RoleManager> roleManagerMap = new HashMap<>();
    private RuleInfo ruleInfo;
    private Integer gameType;
    private List<Pai> paiList = PaiManager.getTotalPaiList();

//    public Map<Long, RoleManager> getRoleManagerMap() {
//        return roleManagerMap;
//    }

    public PlayerRole getById(Long playerId){
        return playerMap.get(playerId);
    }
    public List<Pai> getPaiList() {
        return paiList;
    }

    public void setPaiList(List<Pai> paiList) {
        this.paiList = paiList;
    }

    //    private PaiManager paiManager;
    private GameRule gameRule;
    private Map<String , Organ> roomInfo = new HashMap<>();

    public Map<String, Organ> getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(Map<String, Organ> roomInfo) {
        this.roomInfo = roomInfo;
    }
    public GameRule getGameRule() {
        return gameRule;
    }

    public void setGameRule(GameRule gameRule) {
        this.gameRule = gameRule;
    }

//    public PaiManager getPaiManager() {
//        return paiManager;
//    }
//
//    public void setPaiManager(PaiManager paiManager) {
//        this.paiManager = paiManager;
//    }

    public Integer getGameType() {
        return gameType;
    }

    public void setGameType(Integer gameType) {
        this.gameType = gameType;
    }

    public RuleInfo getRuleInfo() {
        return ruleInfo;
    }

    public void setRuleInfo(RuleInfo ruleInfo) {
        this.ruleInfo = ruleInfo;
    }

    public Room(RuleInfo ruleInfo) {
        this.ruleInfo = ruleInfo;
    }

    public Room() {
    }

    private Long playingIndex;

    public Long getPlayingIndex() {
        return playingIndex;
    }

    public void setPlayingIndex(Long playingIndex) {
        this.playingIndex = playingIndex;
    }

//    public Map<Long, PlayerRole> getPlayerMap() {
//        return playerMap;
//    }
//
//    public void setPlayerMap(Map<Long, PlayerRole> playerMap) {
//        this.playerMap = playerMap;
//    }


    public void playerJoinRoom(PlayerRole playerRole){
        playerMap.put(playerRole.getPlayerId(),playerRole);
//        roleManagerMap.put(playerRole.getPlayerId(),new PlayerRoleManager(playerRole));
    }
//    public void roomNotifyObserver(Object o){
//        setChanged();
//
//        notifyObservers(o);
//    }

}
