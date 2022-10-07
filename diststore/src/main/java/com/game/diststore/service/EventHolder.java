package com.game.diststore.service;

import com.game.mj.cache.Operation;
import com.game.mj.cache.ReadWriteLockOperate;
import com.game.mj.cache.ReturnOperate;
import com.game.mj.eventcommand.IEvent;
import io.netty.util.concurrent.EventExecutor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
@Component
public class EventHolder {
    private Map<Long, IEvent> eventMap = new HashMap<>();
    private Map<Long, EventExecutor> executorMap = new HashMap<>();
    private ReadWriteLockOperate readWriteLockOperate = new ReadWriteLockOperate();
    public IEvent getByKey(Long key){
        return readWriteLockOperate.readLockReturnOperation(new ReturnOperate<IEvent>() {
            @Override
            public IEvent operate() {
                return eventMap.get(key);
            }
        });
    }
    public EventExecutor getExecutoByKey(Long key){
        return readWriteLockOperate.readLockReturnOperation(new ReturnOperate<EventExecutor>() {
            @Override
            public EventExecutor operate() {
                return executorMap.get(key);
            }
        });
    }
    public void saveIevent(Long key,IEvent iEvent){
        readWriteLockOperate.writeLockOperation(new Operation() {
            @Override
            public void operate() {
                eventMap.put(key,iEvent);
            }
        });
    }
    public void saveEventExecutor(Long key,EventExecutor executor){
        readWriteLockOperate.writeLockOperation(new Operation() {
            @Override
            public void operate() {
                executorMap.put(key,executor);
            }
        });
    }
    public void saveIeventExecutor(Long key,IEvent iEvent,EventExecutor executor){
        readWriteLockOperate.writeLockOperation(new Operation() {
            @Override
            public void operate() {
                eventMap.put(key,iEvent);
                executorMap.put(key,executor);
            }
        });
    }
}
