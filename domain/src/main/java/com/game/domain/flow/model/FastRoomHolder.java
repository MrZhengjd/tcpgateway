package com.game.domain.flow.model;


import com.game.domain.relation.room.Room;
import com.game.domain.relation.room.RoomManager;
import io.netty.util.concurrent.FastThreadLocal;

public class FastRoomHolder {
    private static final FastThreadLocal<RoomManager> CONTEXT_THREAD_LOCAL = new FastThreadLocal<>();


    public static RoomManager getRuntimeContext(Room room){
        RoomManager context = CONTEXT_THREAD_LOCAL.get();
        if (context == null){
            if (room == null){
                return null;
            }
            RoomManager roomManager = new RoomManager(room);
            CONTEXT_THREAD_LOCAL.set(roomManager);
        }

        return CONTEXT_THREAD_LOCAL.get();
    }


    public static RoomManager getRuntimeContext(){
        return getRuntimeContext(null);
    }
    /**
     * 设置结果集
     * @param result
     */

    public static void setCurrentRoomManager(RoomManager context) {
        CONTEXT_THREAD_LOCAL.set(context);
    }
    public static void clear(){
        CONTEXT_THREAD_LOCAL.set(null);
        CONTEXT_THREAD_LOCAL.remove();
    }
}
