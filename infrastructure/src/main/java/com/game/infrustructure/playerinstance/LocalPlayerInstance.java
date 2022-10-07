package com.game.infrustructure.playerinstance;

import com.game.domain.playerinstance.PlayerInstance;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zheng
 */
@Service
@Primary
public class LocalPlayerInstance extends AbstractPlayerInstance {
    private Map<Long,Map<Integer,Integer>> playerUseServiceMap = new ConcurrentHashMap<>();

    @Override
    public Integer selectModuleServer(Long playerId, Integer moduleId) {
        Map<Integer, Integer> map = playerUseServiceMap.get(playerId);
        if (map == null){
            return null;
        }
        return map.get(moduleId);
    }

    @Override
    public void registerInfo(Long playerId, Integer moduleId, Integer serverId) {
        Map<Integer,Integer> moduleServer = playerUseServiceMap.get(playerId);
        if (moduleServer == null){
            moduleServer = new ConcurrentHashMap<>();
        }
        moduleServer.put(moduleId,serverId);
        playerUseServiceMap.put(playerId,moduleServer);
    }


    @Override
    public void saveData(Long playerId, Map<Integer, Integer> moduleServer) {
        playerUseServiceMap.put(playerId,moduleServer);
    }
}
