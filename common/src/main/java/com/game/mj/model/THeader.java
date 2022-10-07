package com.game.mj.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author zheng
 */
@Getter
@Setter
public class THeader implements Serializable {
    private int traceId;
    private int serverId;
    // 调用的服务id
    private int serviceId;
    // 通过moduleid来获得下游服务器的地址
    private int moduleId;
    //上游调用方id
    private int callAppId;
    private  byte type;//消息类型
    private String clientIp;
    //就是直接定义发送给某个player 一对一的发送
    private Long playerId;
    private boolean usedTest;
    // (serviceId - describe) 组成一个key，通过这个key 获取ListenerHandler
    private String describe;
    //添加这个是为了让房间的消息也变成SingleEventExecutor来处理 或者以后有什么拓展，
    //本来是一个玩家的请求序列化，现在是某个房间的请求序列化
    private Object attribute;
    // 0 是一对1发送消息 1是1对多发送消息
    private int sendWay;
    private List<Long> toPlayerIds;
    public THeader(byte type) {
        this.type = type;
    }
}
