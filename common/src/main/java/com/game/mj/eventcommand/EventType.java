package com.game.mj.eventcommand;

/**
 * @author zheng
 */
public enum EventType {
    DELETE((byte)2),CREATE((byte) 1),MODIFY((byte)3);
    private byte type;

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    EventType(byte type) {
        this.type = type;
    }
}
