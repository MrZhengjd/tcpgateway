package com.game.infrustructure.playerinstance;

import com.game.mj.serialize.DataSerialize;
import com.game.mj.serialize.DataSerializeFactory;
import com.game.infrustructure.redis.JsonRedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author zheng
 */
@Service
public class RedisPlayerInstance extends AbstractPlayerInstance {
    @Autowired
    private JsonRedisManager jsonRedisManager;

    private DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();
    @Override
    public Integer selectModuleServer(Long playerId, Integer moduleId) {
        String key = getRedisKey(playerId);
        byte[] data = jsonRedisManager.getObjectHash1(key, playerId.toString());
        if (data == null){
            return null;
        }
        return dataSerialize.deserialize(data,Integer.class);

    }

    @Override
    public void registerInfo(Long playerId, Integer moduleId, Integer serverId) {
        String key = getRedisKey(playerId);
        byte[] objectHash1 = jsonRedisManager.getObjectHash1(key, playerId.toString());
        if (objectHash1 != null){
            Map<Integer,Integer> map = dataSerialize.deserialize(objectHash1,Map.class);
            map.put(moduleId,serverId);
            saveData(playerId,map);
        }

    }



    @Override
    public void saveData(Long playerId, Map<Integer, Integer> moduleServer) {
        if (moduleServer == null || moduleServer.isEmpty()){
            return;
        }
        String key = getRedisKey(playerId);
        jsonRedisManager.setObjectHash1(key,playerId.toString(),moduleServer);

    }
    private String getRedisKey(Long playerId) {
        return "play-instance-"+playerId;
    }
}
