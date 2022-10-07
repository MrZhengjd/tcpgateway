package com.game.infrustructure.playerinstance;

import com.game.domain.playerinstance.PlayerInstance;
import com.game.domain.registerservice.RegisterService;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zheng
 */
public abstract class AbstractPlayerInstance implements PlayerInstance {

    @Override
   public Integer selectPlayerModule(Long playerId,Integer moduleId,RegisterService registerService){
       Integer serverId = selectModuleServer(playerId, moduleId);
       if (serverId == null || serverId < 0){
           saveData(playerId,new ConcurrentHashMap<>());
           return null;
       }
       if (!registerService.checkServerOnLine(moduleId,serverId)){
           return null;
       }
       return serverId;
   }
}
