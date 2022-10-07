package com.game.infrustructure.persistence.room;

import com.game.mj.constant.InfoConstant;
import com.game.mj.serialize.DataSerialize;
import com.game.mj.serialize.DataSerializeFactory;
import com.game.domain.relation.room.Room;
import com.game.domain.repository.room.RoomRepository;
import com.game.infrustructure.redis.JsonRedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zheng
 */
@Service
public class RedisRoomRepository implements RoomRepository {
    @Autowired
    private JsonRedisManager jsonRedisManager;
    private DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();
    @Override
    public Room findByNumber(Integer roomNumber) {
        return null;
    }

    @Override
    public Room findByPlayerId(Long playerId) {
        byte[] data = jsonRedisManager.getObjectHash1(InfoConstant.ROOM_PLAYER_ID, playerId.toString());
        if (data == null){
            return null;
        }
        return dataSerialize.deserialize(data,Room.class);
    }

    @Override
    public void saveRoomData(Room room) {

    }
}
