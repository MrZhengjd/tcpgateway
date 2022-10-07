package com.game.domain.statemachine;

import com.game.domain.statemachine.StateEvent;
import com.game.domain.statemachine.Status;

/**
 * @author zheng
 */
public interface StatusMachine {
    public Status getNextStatus(Status status, StateEvent event);
}
