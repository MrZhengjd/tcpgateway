package com.game.consumemodel.statusmachine;


import com.game.domain.relation.room.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
@Component
public class StateHandlerProxy {
    @Autowired
    private ApplicationContext applicationContext;

    private Map<Integer,RoomStatusHandler> handlerMap = new HashMap<>();
    @PostConstruct
    public void init(){
        Map<String, RoomStatusHandler> beansOfType = applicationContext.getBeansOfType(RoomStatusHandler.class);
        for (Map.Entry<String, RoomStatusHandler> entry : beansOfType.entrySet()){
            StatusId annotation = entry.getValue().getClass().getAnnotation(StatusId.class);
            if (annotation != null){
                handlerMap.put(annotation.statusId(),entry.getValue());
            }
        }
    }

    public RoomStatusHandler getByStatus(Integer status){
        return handlerMap.get(status);
    }

    /**
     * 每个状态有个对应的处理器
     * @param room
     */
    public void handleRoomState(Room room){
        RoomStatusHandler handler = getByStatus(room.getGameStatus());
        if (handler == null){
            return;
        }
        handler.handleStateEvent(room);
    }
}
