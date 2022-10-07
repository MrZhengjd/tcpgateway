package com.game.domain.statemachine.composite;

import com.game.domain.statemachine.DefaultLeaveTypeHandler;
import com.game.domain.statemachine.LeavePermitType;
import com.game.domain.statemachine.StateEvent;
import com.game.domain.statemachine.composite.*;
import com.game.domain.statemachine.composite.IStatus;
import com.game.domain.statemachine.composite.LeaveRequest;
import com.game.domain.statemachine.composite.LeaveTypeHandler;
import com.game.domain.statemachine.composite.Rank;
import com.game.domain.statemachine.composite.StateEventHandler;

/**
 * @author zheng
 */
public class LeaveRequestEngine {
    private LeaveRequest leaveRequest;

    public LeaveRequestEngine(LeaveRequest leaveRequest) {
        this.leaveRequest = leaveRequest;
    }
    public void processEvent(StateEvent event){
        leaveRequest.setEvent(event);
        StateEventHandler handler = new DefaultStateEventHandler();
        IStatus iStatus = handler.handleStateEvent(event);

        LeaveTypeHandler leaveTypeHandler = new DefaultLeaveTypeHandler();
        Rank rank = leaveTypeHandler.handleLeaveType(iStatus, leaveRequest);
        leaveRequest.setRank(rank);
        if (rank.getRank() == 4){
            iStatus = IStatus.PERMIT_SUCCESS;
        }else if (rank.getRank() == 5){
            iStatus = IStatus.PERMIT_FAIL;
        }
        leaveRequest.setStatus(iStatus);
        System.out.println(leaveRequest);
    }
}
