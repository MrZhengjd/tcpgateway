package com.game.domain.statemachine.composite;

import com.game.domain.statemachine.LeavePermitType;
import com.game.domain.statemachine.composite.IStatus;
import com.game.domain.statemachine.composite.LeaveRequest;
import com.game.domain.statemachine.composite.Rank;

/**
 * @author zheng
 */
public interface LeaveTypeHandler {
    Rank handleLeaveType(IStatus iStatus, LeaveRequest leaveRequest);
}
