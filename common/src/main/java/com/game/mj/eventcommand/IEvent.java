package com.game.mj.eventcommand;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zheng
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public  class IEvent {
    //操作指令 1代表添加 2代表删除	3 代表修改
    protected byte type;
    protected Long eventId;
    protected int calledId;
    protected Object data;
    private boolean usedIndex;

    @Override
    public String toString() {
        return "type "+type +" eventId "+eventId +" data "+data + " calledId "+calledId;
    }
}
