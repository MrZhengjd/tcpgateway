package com.game.domain.registerservice;

import com.game.mj.model.ServerVo;

import java.util.List;

/**
 * @author zheng
 */
public interface RegisterService {
    /**
     * 判断服务器是否在线
     * @param moduleId
     * @param serverId
     * @return
     */
    boolean checkServerOnLine(Integer moduleId, Integer serverId);

    /**
     * 获取玩家所在服务器信息
     * @param moduleId
     * @param playerId
     * @return
     */
    ServerVo selectServerInfo(Integer moduleId, Long playerId);

    /**
     * 注册服务
     * ip 服务名称 id 集群名称等
     * @param host
     * @param port
     * @param clusterName
     * @param serviceName
     * @param serviceId
     * @param serverId
     */
    void registerServiceModule(String host, int port, String clusterName, String serviceName,Integer serviceId,Integer serverId);

    void refreshData(List<String> modules);

    /**
     * 根据id来获取name
     * @param moduleId
     * @return
     */
    String getModuleName(Integer moduleId);
}
