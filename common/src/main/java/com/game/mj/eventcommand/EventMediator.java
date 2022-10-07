package com.game.mj.eventcommand;

/**
 * @author zheng
 */
public interface EventMediator {
    void process(IEvent iEvent,LockQueueMediator mediator);
    void rollback(byte[] data);
}
