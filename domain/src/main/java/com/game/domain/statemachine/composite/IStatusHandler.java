package com.game.domain.statemachine.composite;

import com.game.domain.statemachine.Status;

/**
 * @author zheng
 */
public interface IStatusHandler {
    Status handleStatus(Status status);
}
