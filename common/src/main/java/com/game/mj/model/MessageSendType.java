package com.game.mj.model;

public enum MessageSendType {
    /**
     * 0 是a发送消息 处理后返回给自己
     * 1 是a发送消息 处理后返回给房间里面的人
     * 2 是a发送消息 处理后返回给某个玩家
     * 3 是1发送消息 处理后返回给某个俱乐部的玩家
     * 4 是a发送消息 自己处理后不同的消息返回给房间里面不同的人
     */
    ONY_BY_ONE(0), ONE_BY_MANY(1),ON_TO_PLAYER(2),ON_TO_CLUB(3),ONE_BY_MANY_DIFFER(4);
    public int value;

    MessageSendType(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    public static MessageSendType fromName(int typeName) {
        for (MessageSendType type : MessageSendType.values()) {
            if (type.getValue() == typeName) {
                return type;
            }
        }
        return null;
    }
}
