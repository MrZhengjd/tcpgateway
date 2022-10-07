package com.game.mj.eventcommand;

import com.game.mj.model.TempBlockInfo;
import com.game.mj.walstore.UnLockWALQueue;
import io.netty.util.concurrent.Promise;
import sun.misc.Contended;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author zheng
 */
public abstract class LockQueueMediator {
    public LockQueueMediator( UnLockWALQueue dataUnLockQueue) {
        this.holdMap =  new TreeMap<>();
        this.dataUnLockQueue = dataUnLockQueue;
    }

    public LockQueueMediator(UnLockWALQueue dataUnLockQueue, UnLockWALQueue keyUnLockQueue, UnLockWALQueue masterChangeQueue) {
        this.holdMap =  new TreeMap<>();
        this.dataUnLockQueue = dataUnLockQueue;
        this.keyUnLockQueue = keyUnLockQueue;
        this.masterChangeQueue = masterChangeQueue;
    }
    @Contended
    protected volatile boolean rollback = false;

    @Contended
    protected volatile boolean finishrollback = false;
    protected SortedMap<Long,IEvent> holdMap ;
    protected UnLockWALQueue dataUnLockQueue;
    protected UnLockWALQueue keyUnLockQueue;
    protected UnLockWALQueue masterChangeQueue;
    @Contended
    protected volatile long latestOperateId;
    @Contended
    protected volatile boolean loadKey;
    public SortedMap<Long, IEvent> getHoldMap() {
        return holdMap;
    }
    public long getLatestOperateId() {
        return latestOperateId;
    }
    protected static SortedMap<Long, TempBlockInfo> localKeyMap = new TreeMap<>();
    public void setLatestOperateId(Long latestOperateId) {
        this.latestOperateId = latestOperateId;
    }
    public void setHoldMap(SortedMap<Long, IEvent> holdMap) {
        this.holdMap = holdMap;
    }
    protected Map<Integer,Integer> loadDataMap = new HashMap<>();
    public UnLockWALQueue getDataUnLockQueue() {
        return dataUnLockQueue;
    }

    public void setDataUnLockQueue(UnLockWALQueue dataUnLockQueue) {
        this.dataUnLockQueue = dataUnLockQueue;
        holdMap = new TreeMap<>();
    }
    public void clear(){
        holdMap.clear();
        rollback = false;
    }
    public abstract void execute(IEvent event);
    public abstract void rollback();
    //获取key对应的event
    public abstract Promise<IEvent> takeFromLastId(Long key);

    public boolean getFinishrollback() {
        return finishrollback;
    }

    /**
     * 获得和key最近都ievent
     * 如果存在key对应的ievent 直接返回
     * 不存在就返回比key的下一个key对应的ievent
     * @param key
     * @return
     */
    public abstract Promise<IEvent> getNextEvent(long key,long endKey);
}
