package com.game.domain.statemachine;

import com.game.mj.eventdispatch.Event;

import java.io.Serializable;

/**
 * @author zheng
 */
public class LeavePermit implements Serializable , Event {
    private LeavePermitType leavePermitType;
    private Status status;
    private String user;
    private StateEvent event;

    public LeavePermitType getLeavePermitType() {
        return leavePermitType;
    }

    public void setLeavePermitType(LeavePermitType leavePermitType) {
        this.leavePermitType = leavePermitType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public StateEvent getEvent() {
        return event;
    }

    public void setEvent(StateEvent event) {
        this.event = event;
    }
}
