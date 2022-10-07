package com.game.mj.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zheng
 */
@Getter
@Setter
public class NodeInfo implements Serializable {
    private int nodeId;
    private String host;
    private int port;
//    private InetSocketAddress address;
    private boolean master;
    private Long latestOperateId;
    private Integer selectId;
//    private Long masterStartOperateId;

    public NodeInfo(String host,Integer port) {
        this.host = host;
        this.port = port;
        this.master = false;
        this.nodeId = 0;
    }

    @Override
    public String toString() {
        return "nodeId :"+ nodeId +" host "+ host +" port "+port +" isMaster "+master;
    }
}
