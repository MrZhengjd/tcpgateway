package com.game.domain.repository.room;

import com.game.domain.relation.room.Room;

/**
 * @author zheng
 */
public interface RoomRepository {
    Room findByNumber(Integer roomNumber);
    Room findByPlayerId(Long playerId);
    void saveRoomData(Room room);
}
