package com.game.domain.model;



import com.game.mj.eventdispatch.Event;
import com.game.domain.relation.command.OperateCountCommand;
import com.game.domain.relation.organ.Organ;
import com.game.domain.relation.role.PlayerRole;
import com.game.domain.relation.room.Room;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * @author zheng
 */
public class PlayerRequest  extends Observable implements Event {
    private Integer requestType;

    public Integer getRequestType() {
        return requestType;
    }

    public void setRequestType(Integer requestType) {
        this.requestType = requestType;
    }

    private Map<String ,Object> requestMap = new HashMap<>();

    public Map<String, Object> getRequestMap() {
        return requestMap;
    }

    public void setRequestMap(Map<String, Object> requestMap) {
        this.requestMap = requestMap;
    }
    private PlayerRole playerRole;
    private Room room;

    public PlayerRole getPlayerRole() {
        return playerRole;
    }

    public void setPlayerRole(PlayerRole playerRole) {
        this.playerRole = playerRole;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

//    public <T>T getRuleInfo(String name){
//        return (T) room.getRuleInfo().getRuleInfo().get(name);
//    }

    public void organOperate(Class< ? extends Organ> organ, Object data){
        Organ dst = playerRole.getOrganMap().get(organ.getSimpleName());
//        playerRole.callEvent(dst,data);
    }
//    public void organOperateWithName(Class< ? extends Organ> organ,String name){
//        Organ dst = playerRole.getOrganMap().get(organ.getSimpleName());
//        EventAnnotationManager.getInstance().sendPlayerRoleEvent(dst,name);
//    }
//    public void organOperateWithName(Class< ? extends Organ> organ,String name,Object data){
//        Organ dst = playerRole.getOrganMap().get(organ.getSimpleName());
////        RequestOrgan requestOrgan = new RequestOrgan(data,dst);
//        EventAnnotationManager.getInstance().sendPlayerRoleWithArgue(dst,name,data);
//    }

    public void organCommandExecute(Class< ? extends Organ> organ,Object data){
        Organ dst = playerRole.getOrganMap().get(organ.getSimpleName());
//        RequestOrgan requestOrgan = new RequestOrgan(data,dst);
//        EventAnnotationManager.getInstance().sendPlayerRoleWithArgue(dst,name,data);
        OperateCountCommand chuPaiCommand = new OperateCountCommand();
//        chuPaiCommand.execute(dst,data);
    }
    public void notifyAllObservers(){
        setChanged();
        notifyObservers();
    }

}
