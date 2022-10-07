package com.game.domain.statemachine.composite;


import com.game.domain.statemachine.Status;

/**
 * @author zheng
 */
public class DefaultStatesHandler implements IStatusHandler {
    @Override
    public Status handleStatus(Status status) {
        switch (status){
            case PERMIT_SUBMIT:
                return Status.LEADER_PERMIT;

        }
        return Status.PERMIT_FAIL;
    }

}
