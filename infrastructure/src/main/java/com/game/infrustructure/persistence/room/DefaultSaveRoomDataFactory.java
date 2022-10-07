package com.game.infrustructure.persistence.room;


import com.game.domain.relation.operate.SaveRoomData;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
public class DefaultSaveRoomDataFactory {
    private static final String REDIS= "redis";
    private DefaultSaveRoomDataFactory() {
        init();
    }

    private void init(){
        saveRoomDataMap.put(REDIS,new DefaultSaveRoomData());
    }
    private  Map<String ,SaveRoomData> saveRoomDataMap = new HashMap<>();
    public static DefaultSaveRoomDataFactory getInstance(){
        return Holder.factory;
    }
    private static class Holder{
        private static DefaultSaveRoomDataFactory factory = new DefaultSaveRoomDataFactory();
    }
    public SaveRoomData getDefaultSaveRedis(){
        return saveRoomDataMap.get(REDIS);
    }
}
