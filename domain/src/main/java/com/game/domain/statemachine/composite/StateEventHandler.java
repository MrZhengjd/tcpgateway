package com.game.domain.statemachine.composite;


import com.game.domain.statemachine.StateEvent;

/**
 * @author zheng
 */
public interface StateEventHandler {
    IStatus handleStateEvent(StateEvent event);
}
