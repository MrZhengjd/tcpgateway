package com.game.domain.statemachine;

import com.game.domain.statemachine.StateEvent;
import com.game.domain.statemachine.Status;
import com.game.domain.statemachine.StatusMachine;

/**
 * @author zheng
 */
public class AnnualLeaveMachine implements StatusMachine {
    @Override
    public Status getNextStatus(Status status, StateEvent event) {
        switch (status){
            case PERMIT_SUBMIT:
                return  Status.LEADER_PERMIT;
            case LEADER_PERMIT:
                return getLeaderPermitStatus(event);
        }
        return  Status.PERMIT_FAIL;
    }

    private  Status getLeaderPermitStatus(StateEvent event) {
        switch (event){
            case AGREE:return  Status.LEADER_PERMIT_AGREE;
            case MODIFY:return  Status.LEADER_PERMIT_MODIFY;
            case DISAGREE:return Status.LEADER_PERMIT_DISAGREE;
        }
        throw new RuntimeException("cannot support this");
    }
}
