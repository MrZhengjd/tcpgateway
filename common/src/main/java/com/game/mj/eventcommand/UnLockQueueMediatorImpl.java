package com.game.mj.eventcommand;

import com.game.mj.cache.MasterInfo;
import com.game.mj.concurrent.LocalRunner;
import com.game.mj.concurrent.PromiseUtil;
import com.game.mj.generator.IdGenerator;
import com.game.mj.generator.IdGeneratorFactory;
import com.game.mj.model.TempBlockInfo;
import com.game.mj.serialize.DataSerialize;
import com.game.mj.serialize.DataSerializeFactory;
import com.game.mj.store.BlockInfo;
import com.game.mj.store.QueueBlock;
import com.game.mj.store.TempResult;
import com.game.mj.walstore.UnLockWALQueue;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import lombok.extern.log4j.Log4j2;

import java.util.SortedMap;

/**
 * @author zheng
 */
@Log4j2
public class UnLockQueueMediatorImpl extends LockQueueMediator {
    IdGenerator idGenerator = IdGeneratorFactory.getDefaultGenerator();
    private static DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();
    private Long nowCopyId = -1l;

    public UnLockQueueMediatorImpl(UnLockWALQueue dataUnLockQueue, UnLockWALQueue keyUnLockQueue, UnLockWALQueue masterChangeQueue) {
        super(dataUnLockQueue, keyUnLockQueue, masterChangeQueue);
    }

    public UnLockQueueMediatorImpl(UnLockWALQueue unLockQueue) {
        super(unLockQueue);
    }
    public UnLockQueueMediatorImpl( UnLockWALQueue unLockQueue,UnLockWALQueue keyUnLockQueue) {
        super(unLockQueue);
        this.keyUnLockQueue = keyUnLockQueue;
    }

//    public static CountDownLatch latch = new CountDownLatch(4);




    @Override
    public void execute(IEvent event) {
        if (rollback){
            throw new RuntimeException("here is on rollback please wait");
        }
        PromiseUtil.safeExecute(dataUnLockQueue.getExecutor(), new LocalRunner() {
            @Override
            public void task(Promise promise, Object object) {
                if (rollback){
                    promise.setSuccess(true);
                    return;
                }
//                event.setData(new BaseData(event.getEventId(),"test-"+event.getEventId()));
                if (event.getEventId() == null || event.getEventId() < 0){
                    event.setEventId(idGenerator.generateIdFromServerId(event.getCalledId()));
                }
                if (event.getEventId() < latestOperateId){
                    promise.setSuccess(true);
                    return;
                }
                dataUnLockQueue.offerData(dataSerialize.serialize(event)).addListener(new GenericFutureListener<Future<BlockInfo>>() {
                    @Override
                    public void operationComplete(Future<BlockInfo> future) throws Exception {
                        if (future.isSuccess()){
                            BlockInfo o = future.get();
                            if (o != null){
                                holdMap.put(event.getEventId(),event);
                                latestOperateId = event.getEventId();
                                if (event.isUsedIndex()){
                                    writeToKey(o,latestOperateId);
                                }

//                log.info("here is write message succes "+unLockQueue.getExecutor().hashCode() );
                                promise.setSuccess(true);
                            }else {
                                promise.setFailure(new RuntimeException("add data failed"));
                            }

                        }
                    }
                });

            }
        } ,null);

    }

    /**
     * 写到key queue
     * @param blockInfo
     * @param lastOperateId
     */
    private void writeToKey(BlockInfo blockInfo,Long lastOperateId) {
        TempBlockInfo tempBlockInfo = TempBlockInfo.getByBlockInfo(blockInfo,lastOperateId);
        keyUnLockQueue.offerData(dataSerialize.serialize(tempBlockInfo)).addListener(new GenericFutureListener<Future<? super BlockInfo>>() {
            @Override
            public void operationComplete(Future<? super BlockInfo> future) throws Exception {
                if (future.isSuccess()){
                    localKeyMap.put(lastOperateId,tempBlockInfo);
                }
            }
        });
    }

    // 循环取出数据，直到取出的数据为空

    private void rotatePoll(){

        try {

            boolean needContinued = false;

            TempResult tempResult = dataUnLockQueue.poll2();
            if (tempResult.getState() == 2){
                byte[] datas = tempResult.getDatas();
                if (datas == null){
                    return;
                }
                IEvent desEvent = null;
                try {
                    desEvent = dataSerialize.deserialize(datas, IEvent.class);
                    if (desEvent == null){
                        return;
                    }
                    if (desEvent.getEventId() <= nowCopyId){
                        return;
                    }
                    if (desEvent.getType() != EventType.DELETE.getType()){
                        holdMap.put(desEvent.getEventId(),desEvent);
//                    log.info("remove event "+desEvent);
                    }else {
                        holdMap.remove(desEvent.getEventId(),desEvent);
//                    log.info("add event "+desEvent);
                    }
                    nowCopyId = desEvent.getEventId();
                    this.latestOperateId = desEvent.getEventId();
                    rotatePoll();


                }catch (Exception e){
                    e.printStackTrace();
                    log.error("here is log end "+tempResult);
                }

            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void rollback() {
//        System.out.println("there show hashcode -- "+this.getUnLockQueue().getExecutor().hashCode());
//        PromiseUtil.safeExecute(dataUnLockQueue.getExecutor(), new LocalRunner() {
//            @Override
//            public void task(Promise promise, Object object) {
//
//            }
//        },null);

        if (!rollback){
            rollback = true;
            rotatePoll();
//                    System.out.println("here use executor "+unLockQueue.getExecutor().hashCode());
            rollback = false;
            finishrollback = true;
//            promise.setSuccess(true);
        }


//

    }

    @Override
    public Promise<IEvent> takeFromLastId(Long key) {

        return PromiseUtil.safeExecute(dataUnLockQueue.getExecutor(), new LocalRunner<Long>() {
            @Override
            public void task(Promise promise, Long key) {
                if (holdMap != null && key != null && holdMap.containsKey(key)){
                    promise.setSuccess(holdMap.get(key));
                }else {
                    IEvent fromDisk = getFromDisk(key);
                    if (fromDisk == null){
                        promise.setSuccess(null);
                    }else {
                        promise.setSuccess(fromDisk);
                    }
                }
            }
        },key);

    }

    @Override
    public Promise<IEvent> getNextEvent(long key,long endKey) {
        return PromiseUtil.safeExecute(dataUnLockQueue.getExecutor(), new LocalRunner<Long>() {
            @Override
            public void task(Promise promise, Long key) {
                initData(key);
                if (holdMap.containsKey(key)){
                    key = key + 1;
                }
                IEvent e = null;
                if (key < endKey && endKey > 0){
                    e = getNextKey(key);
                }

                promise.setSuccess(e);
                if (e != null){
                    log.info("here is copy id to "+e.getEventId());
                }
            }
        },key);
    }

    private IEvent getNextKey(Long key) {
//        holdMap.subMap()
        Long endKey = idGenerator.generateIdFromServerId(MasterInfo.getServerInfo() == null ? 1 : MasterInfo.getServerInfo());
        return getFirstChangeInfo(holdMap.subMap( key,endKey));
    }
    /**
     * 获取尾部数据
     *
     * @param subMap
     * @return
     */
    private IEvent getFirstChangeInfo(SortedMap<Long, IEvent> subMap) {
        if (subMap == null || subMap.isEmpty()) {
            return null;
        }
        Long lastKey = subMap.firstKey();
        return subMap.get(lastKey);
    }
    /**
     * 获取尾部数据
     *
     * @param subMap
     * @return
     */
    public IEvent getLastChangeInfo(SortedMap<Long, IEvent> subMap) {
        if (subMap == null || subMap.isEmpty()) {
            return null;
        }
        Long lastKey = subMap.lastKey();
        return subMap.get(lastKey);
    }
    /**
     * 初始化
     * @param key
     */
    private void initData(Long key){
        if (keyUnLockQueue == null || key == null || localKeyMap == null){
            return ;
        }
        TempBlockInfo tempBlockInfo = localKeyMap.get(key);
        if (tempBlockInfo == null && !loadKey){
            readFromDisk(keyUnLockQueue.getReadBlockWithIndex(0),new KeyRotateOperate());
        }
        if (tempBlockInfo == null){
            return ;
        }
        if (!holdMap.containsKey(tempBlockInfo.getKey()) && !localKeyMap.containsKey(tempBlockInfo.getPageIndex())){
            initHoldMap(tempBlockInfo);
        }
    }
    /**
     * 从磁盘上获取数据
     * @param key
     * @return
     */
    private IEvent getFromDisk(Long key) {
        try {
            initData(key);
            TempBlockInfo tempBlockInfo = localKeyMap.get(key);
            if (tempBlockInfo == null){
                return null;
            }
            return holdMap.get(tempBlockInfo.getKey());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 从磁盘上读取内存到map里面
     * @param tempBlockInfo
     */
    private void initHoldMap(TempBlockInfo tempBlockInfo) {
        loadDataMap.put(tempBlockInfo.getPageIndex(),tempBlockInfo.getPageIndex());
        readFromDisk(dataUnLockQueue.getReadBlockWithIndex(tempBlockInfo.getPageIndex()), new RotateOperate() {
            @Override
            public boolean rotateOperate(byte[] data) {
                IEvent info = dataSerialize.deserialize(data,IEvent.class);
                if (info != null){
                    holdMap.put(info.getEventId(),info);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 从磁盘获取数据
     * @param queueBlock
     */
    private void readFromDisk(QueueBlock queueBlock,RotateOperate rotateOperate){
        queueBlock.load();
        queueBlock.initReaderIndex();
        loadKey = true;
        rotateRead(queueBlock,rotateOperate);

    }

    /**
     * 循环遍历获取blockInfo
     * @param queueBlock
     */
    private void rotateRead(QueueBlock queueBlock,RotateOperate rotateOperate){
        byte[] data = queueBlock.readWithIndex();
        if (data == null){
            return;
        }
        if (rotateOperate.rotateOperate(data)) {
            rotateRead(queueBlock,rotateOperate);
        }

    }
    private static class KeyRotateOperate implements RotateOperate{

        @Override
        public boolean rotateOperate(byte[] data) {
            TempBlockInfo info = dataSerialize.deserialize(data,TempBlockInfo.class);
            if (info != null){
                localKeyMap.put(info.getKey(),info);
                return true;
            }
            return false;
        }
    }
    private interface RotateOperate{
        boolean rotateOperate(byte[] data);
    }
}
