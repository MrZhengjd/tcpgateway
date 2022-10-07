package com.game.domain.playerinstance;

import com.game.domain.registerservice.RegisterService;

import java.util.Map;

/**
 * @author zheng
 */
public interface PlayerInstance {
    /**
     * 挑选模块集群里面的服务器id
     * @param playerId
     * @param moduleId
     * @return
     */
    Integer selectModuleServer(Long playerId,Integer moduleId);

    /**
     * 注册玩家集群机器信息
     * @param playerId
     * @param moduleId
     * @param serverId
     */
    void registerInfo(Long playerId,Integer moduleId,Integer serverId);

    /**
     * 获取module 对应的serverid
     * @param playerId
     * @return
     */
//    Map<Integer, Integer> getModuleServer(Long playerId);

    /**
     * 保存信息
     * @param playerId
     * @param moduleServer
     */
    void saveData(Long playerId,Map<Integer,Integer> moduleServer);

    /**
     * 获取玩家模块集群serverId
     * @param playerId
     * @param moduleId
     * @param registerService
     * @return
     */
    public Integer selectPlayerModule(Long playerId, Integer moduleId, RegisterService registerService);
}
