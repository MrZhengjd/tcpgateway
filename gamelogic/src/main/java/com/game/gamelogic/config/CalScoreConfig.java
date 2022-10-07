package com.game.gamelogic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "game.calscore")
public class CalScoreConfig {
    /**
     * 服务器ID
     */
    private int serverId;
    private int port;
    private String name;
    private String clusterName;
    private String host;
    // 达到压缩的消息最小大小
    private Integer moduleId;
    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 单个用户的限流请允许的每秒请求数量
     */
    private double requestPerSecond = 10; 
    /**
     * 全局流量限制请允许每秒请求数量
     */
    private double globalRequestPerSecond=2000;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRequestPerSecond() {
        return requestPerSecond;
    }

    public void setRequestPerSecond(double requestPerSecond) {
        this.requestPerSecond = requestPerSecond;
    }

    public double getGlobalRequestPerSecond() {
        return globalRequestPerSecond;
    }

    public void setGlobalRequestPerSecond(double globalRequestPerSecond) {
        this.globalRequestPerSecond = globalRequestPerSecond;
    }

    public void setGlobalRequestPerSecond(int globalRequestPerSecond) {
        this.globalRequestPerSecond = globalRequestPerSecond;
    }




    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }



}
