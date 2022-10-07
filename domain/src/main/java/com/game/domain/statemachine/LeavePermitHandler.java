package com.game.domain.statemachine;

import com.game.mj.eventdispatch.EventDispatchService;
import com.game.mj.eventdispatch.EventListenerAnnotation;
import com.game.domain.relation.room.JoinRoomEvent;
import com.game.domain.relation.room.RoomManager;
import com.game.domain.statemachine.composite.LeaveRequest;
import com.game.domain.statemachine.composite.LeaveRequestEngine;
import org.springframework.stereotype.Component;

/**
 * @author zheng
 */
@EventDispatchService
@Component
public class LeavePermitHandler {
    @EventListenerAnnotation(value =  LeavePermit.class)
    public void process(Object object, LeavePermit leavePermit){
        Engine engine = new Engine(leavePermit);
        engine.process();
    }
    @EventListenerAnnotation(value = LeaveRequest.class)
    public void processLeaveRequest(StateEvent event, LeaveRequest leaveRequest){
        LeaveRequestEngine engine = new LeaveRequestEngine(leaveRequest);
        engine.processEvent(event);
    }

    @EventListenerAnnotation(value = JoinRoomEvent.class)
    public void joinRoom(JoinRoomEvent joinRoomEvent, RoomManager roomManager){
        roomManager.addRole(joinRoomEvent.getPlayerRole());
//        room.playerJoinRoom(joinRoomEvent.getPlayerRole());
    }
}
