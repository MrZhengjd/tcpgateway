package com.game.infrustructure.persistence.room;

import com.game.mj.constant.InfoConstant;
import com.game.domain.relation.operate.SaveRoomData;
import com.game.domain.relation.room.Room;
import com.game.infrustructure.redis.JsonRedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zheng
 */
@Component
public class DefaultSaveRoomData implements SaveRoomData {
    @Autowired
    private JsonRedisManager jsonRedisManager;
    @Override
    public void saveRoomData(Room room) {
        jsonRedisManager.setObjectHash1(InfoConstant.GAME_ROOM,room.getRoomNum().toString(),room);
    }
}
