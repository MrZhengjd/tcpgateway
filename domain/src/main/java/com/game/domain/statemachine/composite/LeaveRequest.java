package com.game.domain.statemachine.composite;

import com.game.mj.eventdispatch.Event;
import com.game.domain.statemachine.DefaultLeaveTypeHandler;
import com.game.domain.statemachine.LeavePermitType;
import com.game.domain.statemachine.StateEvent;

import java.io.Serializable;

/**
 * @author zheng
 */
public class LeaveRequest implements Serializable, Event {
    private IStatus status;
    private LeavePermitType leavePermitType;
    private StateEvent event;
    private Rank rank;

    @Override
    public String toString() {
        return "status "+status +" leavePemitType "+leavePermitType +" rank "+rank;
    }

    public LeaveRequest() {
        this.rank = DefaultLeaveTypeHandler.getByRan(1);
        this.status = IStatus.PERMIT;
        this.leavePermitType = LeavePermitType.ANNUAL_LEAVE;
    }

    public LeaveRequest(LeavePermitType leavePermitType) {
        this.leavePermitType = leavePermitType;
        this.rank = DefaultLeaveTypeHandler.getByRan(1);
        this.status = IStatus.PERMIT;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public IStatus getStatus() {
        return status;
    }

    public void setStatus(IStatus status) {
        this.status = status;
    }

    public LeavePermitType getLeavePermitType() {
        return leavePermitType;
    }

    public void setLeavePermitType(LeavePermitType leavePermitType) {
        this.leavePermitType = leavePermitType;
    }

    public StateEvent getEvent() {
        return event;
    }

    public void setEvent(StateEvent event) {
        this.event = event;
    }
}
