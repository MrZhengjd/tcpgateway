package com.game.mj.walstore;


import com.game.mj.eventcommand.LockQueueMediator;
import com.game.mj.eventcommand.UnLockQueueMediatorImpl;
//import com.game.common.model.msg.Message;
import com.game.mj.store.TempResult;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zheng
 */
@Slf4j
public class UnLockWALQueueInstance {
    private static Logger logger = LoggerFactory.getLogger(UnLockWALQueueInstance.class);
    private static ClassPathResource classPathResource = new ClassPathResource("data");
//    public static final String configPath0 = System.getProperty("user.dir")+File.separator+"src"+File.separator+"target"+File.separator;
    public static final Integer DEFAULT_POOL_COUNT = 4;
    public static final String DEFAULT_DATA_PATH = "data/";
    public static final String DATA_BACKUP_PATH = "/backup";
//    private static final BlockingQueue<String> QUEUE = new LinkedBlockingQueue<>();
//    private Map<String,ConsumerQueue> consumerQueueMap;
    private AtomicInteger seqId = new AtomicInteger(0);
    private UnLockWALQueue[] queuePools;
    private EventExecutor[] eventExecutors;
    private LockQueueMediator[] mediators;
    private Map<Integer,UnLockWALQueue> unLockWALQueueMap = new HashMap<>();
    private Map<EventExecutor,UnLockWALQueue> executorUnLockWALQueueMap = new HashMap<>();
    private Map<EventExecutor, LockQueueMediator> lockQueueMediatorMap = new HashMap<>();
    private Long count = 0l;
    private AtomicLong comming = new AtomicLong(0);
    private long successCount = 0;
    private AtomicLong readSucessCount = new AtomicLong(0);
    private TempResult last;
    private long fialedCount = 0;
    private volatile boolean inital = false;
    private int poolsCount = 0;

    public UnLockWALQueue[] getQueuePools() {
        return queuePools;
    }
    //    public Long getNullCount(){
//        return consumerQueue.getNutNullCount();
//    }
//    public Message read(){
//        try {
//            return consumerQueue.read();
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//
//    }


    public long getFialedCount() {
        return fialedCount;
    }


    public long getSuccessCount() {
        return successCount;
    }



    public static UnLockWALQueueInstance INSTANCE = null;
    private String filePath = null;






    public long getReadSucessCount() {
        return readSucessCount.get();
    }


    private static class ConsumerQueuePoolHolder{
        private static UnLockWALQueueInstance pool = new UnLockWALQueueInstance(DEFAULT_POOL_COUNT);
    }
    public UnLockWALQueue getByIndex(Integer index){
        if (!inital){
            init();
        }
        return unLockWALQueueMap.get(index);
    }
    public UnLockWALQueue getByEventExecutor(EventExecutor executor){
        if (!inital){
            init();
        }
        synchronized (DATA_BACKUP_PATH.intern()){
            UnLockWALQueue queue = null;
            if (!executorUnLockWALQueueMap.containsKey(executor)){
                queue =  chooseByHash(executor);
                executorUnLockWALQueueMap.put(executor,queue);
            }else {
                queue = executorUnLockWALQueueMap.get(executor);
            }
            return queue;
        }

    }

    public LockQueueMediator getMediatorByEventExecutor(EventExecutor executor){
        if (!inital){
            init();
        }
        synchronized (DATA_BACKUP_PATH.intern()){
            LockQueueMediator queue = null;
            if (!lockQueueMediatorMap.containsKey(executor)){
                queue =  chooseMediatorByHash(executor);
                lockQueueMediatorMap.put(executor,queue);
            }else {
                queue = lockQueueMediatorMap.get(executor);
            }
            return queue;
        }

    }
    public UnLockWALQueue chooseByHash(Object object){
        if (!inital){
            init();
        }
        int hashCode = object.hashCode();
        return this.queuePools[hashCode % this.queuePools.length ];
    }
    public EventExecutor getByHash(Object object){
        if (!inital){
            init();
        }
        int hashCode = object.hashCode();
        return this.eventExecutors[hashCode % this.eventExecutors.length ];
    }
    public LockQueueMediator chooseMediatorByHash(Object object){
        if (!inital){
            init();
        }
        int hashCode = object.hashCode();
        return this.mediators[hashCode % this.mediators.length ];
    }
    public static UnLockWALQueueInstance getInstance(){
        return ConsumerQueuePoolHolder.pool;
    }
    private UnLockWALQueueInstance(int poolsCount){
        this.poolsCount = poolsCount;
    }
    public void init(){
        if (queuePools == null){
            synchronized (DATA_BACKUP_PATH.intern()){
                if (!inital && queuePools == null){

                    queuePools = new UnLockWALQueue[poolsCount];
                    eventExecutors = new DefaultEventExecutor[poolsCount];
                    mediators = new UnLockQueueMediatorImpl[poolsCount];
//                    IGameEventExecutorGroup gameEventExecutorGroup = new IGameEventExecutorGroup(poolsCount,null);
                    for (int i = 0;i<poolsCount;i++){
                        this.filePath = classPathResource.getPath() + "/"+i;
                        File file = new File(filePath);
                        if (!file.exists()){
                            file.mkdirs();
                        }
                        if (!file.isDirectory() || !file.canRead()){
                            throw new IllegalArgumentException("arguement not wright-------------");
                        }
                        File backup = new File(this.filePath + DATA_BACKUP_PATH);
                        if (!backup.exists()){
                            backup.mkdirs();
                        }
                        if (!backup.isDirectory() || !backup.canRead()){
                            throw new IllegalArgumentException("arguement not wright-------------");
                        }
                        EventExecutor eventExecutor = new DefaultEventExecutor();
                        eventExecutors[i] = eventExecutor;
                        queuePools[i] = new UnLockWALQueue("queue",filePath,i,eventExecutor);
                        unLockWALQueueMap.put(i,queuePools[i]);
                        UnLockQueueMediatorImpl mediator = new UnLockQueueMediatorImpl(queuePools[i]);
//                        lockQueueMediatorMap.put(eventExecutors[i],mediator);
                        mediators[i] = mediator;
                    }
                    inital = true;
                }
            }
        }


    }



}
