package com.game.domain.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zheng
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServerVo {

    private int moduleId;
    private int serverId;
    private String serviceName;
    private String host;
    private int port;
    private String module;


    public ServerVo(Integer moduleId, String serviceName, String host, int port) {
        this.moduleId = moduleId;
        this.serviceName = serviceName;
        this.host = host;
        this.port = port;
    }
}
