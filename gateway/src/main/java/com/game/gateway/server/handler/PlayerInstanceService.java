package com.game.gateway.server.handler;

import com.game.mj.concurrent.LocalRunner;
import com.game.mj.concurrent.PromiseUtil;

import com.game.mj.model.ServerVo;
import com.game.mj.serialize.DataSerialize;
import com.game.mj.serialize.DataSerializeFactory;
import com.game.infrustructure.redis.JsonRedisManager;
import io.netty.util.concurrent.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zheng
 */
@Component
public class PlayerInstanceService {
    public PlayerInstanceService() {
    }

    public JsonRedisManager getJsonRedisManager() {
        return jsonRedisManager;
    }

    public void setJsonRedisManager(JsonRedisManager jsonRedisManager) {
        this.jsonRedisManager = jsonRedisManager;
    }

    public RegisterServiceComponent getRegisterServiceComponent() {
        return registerServiceComponent;
    }

    public void setRegisterServiceComponent(RegisterServiceComponent registerServiceComponent) {
        this.registerServiceComponent = registerServiceComponent;
    }

    @Autowired
    private JsonRedisManager jsonRedisManager;
    private DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();
    @Autowired
    private RegisterServiceComponent registerServiceComponent;
    private Map<Long,Map<Integer,Integer>> playerUseServiceMap = new ConcurrentHashMap<>();
    public String getModuleName(int moduleId){
        return registerServiceComponent.getModuleName(moduleId);
    }

    public Promise<Integer> selectServerId(Long playerId,Integer moduleId){
        return PromiseUtil.safeExecuteWithKey(playerId, new LocalRunner() {
            @Override
            public void task(Promise promise, Object object) {
                Map<Integer, Integer> map = playerUseServiceMap.get(playerId);
                if (map == null) {
                    map = new ConcurrentHashMap<>();
                    playerUseServiceMap.put(playerId, map);
                }
                Integer serverId = null;
                if (moduleId != null) {
                    serverId = map.get(moduleId);
                }
                if (serverId != null) {
                    if (registerServiceComponent.isServerOnLine(moduleId, serverId)) {
                        promise.setSuccess(serverId);
                    } else {
                        serverId = null;
                    }
                }
                if (serverId == null) {
                    String key = getRedisKey(playerId);
                    byte[] data = jsonRedisManager.getObjectHash1(key, playerId.toString());
                    boolean flag = false;
                    if (data != null){
                        Integer tempServerId = dataSerialize.deserialize(data, Integer.class);
                        if (registerServiceComponent.isServerOnLine(moduleId,tempServerId)){
                            promise.setSuccess(tempServerId);
                            addLocalCache(playerId,moduleId,tempServerId);
                            flag = true;
                        }
                    }
                    if (data == null || !flag){
                        Integer serverId2 = selectServerIdAndSaveRedis(playerId,moduleId);
                        if (serverId2 == null){
                            promise.setFailure(new RuntimeException("don't have a wright server"));

                        }
                        addLocalCache(playerId,moduleId,serverId2);
                        promise.setSuccess(serverId2);
                    }

                }
            }
        },null);

    }

    private Integer selectServerIdAndSaveRedis(Long playerId, Integer moduleId) {
        ServerVo serverVo = registerServiceComponent.selectServerInfo(moduleId, playerId);
        if (serverVo == null){
            return null;
//           throw new RuntimeException("don't have an wright server");
        }
        String key = getRedisKey(playerId);
        jsonRedisManager.setObjectHash1(key,playerId.toString(),serverVo.getServerId());
        return serverVo.getServerId();
    }

    private void addLocalCache(Long playerId, Integer serviceId
            , Integer tempServerId) {
        Map<Integer, Integer> map = playerUseServiceMap.get(playerId);
        if (map == null){
            map = new HashMap<>();
        }
        map.put(serviceId,tempServerId);
    }

    private String getRedisKey(Long playerId) {
        return "play-instance-"+playerId;
    }
}
