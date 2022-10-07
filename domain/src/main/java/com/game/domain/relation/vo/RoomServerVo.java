package com.game.domain.relation.vo;

import java.io.Serializable;

/**
 * @author zheng
 */
public class RoomServerVo implements Serializable {
    private Integer roomNumber;
    private Integer serverId;

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public RoomServerVo() {
    }

    public static RoomServerVo getInstance(Integer roomNumber,Integer serverId){
        RoomServerVo instance = Holder.instance;
        instance.setRoomNumber(roomNumber);
        instance.setServerId(serverId);
        return instance;
    }
    public RoomServerVo(Integer roomNumber, Integer serverId) {
        this.roomNumber = roomNumber;
        this.serverId = serverId;
    }
    private static class Holder{
        private static RoomServerVo instance = new RoomServerVo();
    }
}
