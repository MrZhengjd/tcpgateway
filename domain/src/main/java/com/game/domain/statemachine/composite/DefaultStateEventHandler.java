package com.game.domain.statemachine.composite;

import com.game.domain.statemachine.StateEvent;

/**
 * @author zheng
 */
public class DefaultStateEventHandler implements StateEventHandler {

    @Override
    public IStatus handleStateEvent(StateEvent event) {
        switch (event){
            case DISAGREE:
                return IStatus.PERMIT_DISAGREE;
            case MODIFY:
                return IStatus.LEADER_PERMIT_MODIFY;
            case AGREE:
                return IStatus.PERMIT_AGREE;
            default:
                throw new  RuntimeException("never support this ");
        }
    }
}
