package com.game.consumemodel.statusmachine;

import com.game.domain.relation.room.Room;

/**
 * @author zheng
 */
public interface RoomStatusHandler {
   void handleStateEvent(Room room);
   void changeNextState(Room room);
}
