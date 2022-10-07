package com.game.domain.model.holder;

import com.game.mj.cache.Operation;
import com.game.mj.cache.ReadWriteLockOperate;
import com.game.mj.cache.ReturnOperate;
import com.game.domain.relation.room.Room;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
public class RoomMap {
    private static Map<Integer, Room> roomMap = new HashMap<>();
    private static ReadWriteLockOperate readWriteLockOperate = new ReadWriteLockOperate();
    public static void saveRoom(Room room){
        readWriteLockOperate.writeLockOperation(new Operation() {
            @Override
            public void operate() {
                roomMap.put(room.getRoomNum(),room);
            }
        });
    }

    public static Room getByRoomNumber(Integer roomNumber){
        return readWriteLockOperate.writeLockReturnOperation(new ReturnOperate<Room>() {
            @Override
            public Room operate() {
                return roomMap.get(roomNumber);
            }
        });
    }
}
