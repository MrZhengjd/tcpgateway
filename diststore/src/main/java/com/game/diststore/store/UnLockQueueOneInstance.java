package com.game.diststore.store;


import com.game.mj.cache.MasterInfo;
import com.game.mj.eventcommand.LockQueueMediator;
import com.game.mj.eventcommand.UnLockQueueMediatorImpl;
import com.game.mj.walstore.UnLockWALQueue;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

//import com.game.common.model.msg.Message;

/**
 * @author zheng
 */
@Slf4j
public class UnLockQueueOneInstance {
    private static Logger logger = LoggerFactory.getLogger(UnLockQueueOneInstance.class);
//    public static final String configPath0 = System.getProperty("user.dir")+File.separator+"diststore"+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator;
    private static ClassPathResource classPathResource = new ClassPathResource("data");
    public static final String DEFAULT_DATA_PATH = "data/";
    public static final String DATA_BACKUP_PATH = "/backup";
//    private static final BlockingQueue<String> QUEUE = new LinkedBlockingQueue<>();
//    private Map<String,ConsumerQueue> consumerQueueMap;
    private AtomicInteger seqId = new AtomicInteger(0);
//
    private UnLockWALQueue queue;
    private UnLockWALQueue indexQueue;
    private UnLockWALQueue masterChangeQueue;
    private LockQueueMediator mediator;
    private LockQueueMediator indexMediator;
    private LockQueueMediator masterChangeMediato;

    public LockQueueMediator getMasterChangeMediato() {
        synchronized (DATA_BACKUP_PATH.intern()){
            if (!inital){
                init();
            }
            return masterChangeMediato;
        }

    }

    private Long count = 0l;
    private AtomicLong comming = new AtomicLong(0);

    public UnLockWALQueue getIndexQueue() {
        return indexQueue;
    }

    /**
     *master改变的queue
     * @return
     */
    public UnLockWALQueue getMasterChangeQueue() {
        return masterChangeQueue;
    }
    private volatile boolean inital = false;

    public UnLockWALQueue getQueue() {
        return queue;
    }

    public LockQueueMediator getMediator() {
        synchronized (DATA_BACKUP_PATH.intern()){
            if (!inital){
                init();
            }
            return mediator;
        }

    }

    public static UnLockQueueOneInstance INSTANCE = null;
    private String filePath = null;






    private static class ConsumerQueuePoolHolder{
        private static UnLockQueueOneInstance pool = new UnLockQueueOneInstance();
    }



//
    public static UnLockQueueOneInstance getInstance(){
        return ConsumerQueuePoolHolder.pool;
    }

    public void init(){
        synchronized (DATA_BACKUP_PATH.intern()){
            if (!inital ){
                try {
                    EventExecutor eventExecutor = new DefaultEventExecutor();
                    EventExecutor index = new DefaultEventExecutor();
                    EventExecutor master = new DefaultEventExecutor();
                    UnLockWALQueue queue = new UnLockWALQueue(MasterInfo.getInfo()+"queue", classPathResource.getPath()+"/master", 0, eventExecutor);
                    this.queue = queue;
                    UnLockWALQueue indexQueue = new UnLockWALQueue(MasterInfo.getInfo()+"index", classPathResource.getPath()+"/index", 0, index);
                    this.masterChangeQueue = new UnLockWALQueue(MasterInfo.getInfo()+"master", classPathResource.getPath()+"/master", 0, master);
                    UnLockQueueMediatorImpl mediator = new UnLockQueueMediatorImpl(queue,indexQueue);
                    this.mediator = mediator;
                    UnLockQueueMediatorImpl masterChangeMediator = new UnLockQueueMediatorImpl(queue,indexQueue,masterChangeQueue);
                    this.masterChangeMediato = masterChangeMediator;
                    inital = true;
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }


    }



}
