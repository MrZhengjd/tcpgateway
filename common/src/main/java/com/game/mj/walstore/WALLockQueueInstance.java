package com.game.mj.walstore;


//import com.game.common.model.msg.Message;
import com.game.mj.store.NamedThreadFactory;
import com.game.mj.store.TempResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zheng
 */
@Slf4j
public class WALLockQueueInstance {
    private static Logger logger = LoggerFactory.getLogger(WALLockQueueInstance.class);
//    public static final String configPath0 = System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator;
    private static ClassPathResource classPathResource = new ClassPathResource("data");
    public static final String DEFAULT_DATA_PATH = "data";
    public static final String DATA_BACKUP_PATH = "/backup";
//    private static final BlockingQueue<String> QUEUE = new LinkedBlockingQueue<>();
//    private Map<String,ConsumerQueue> consumerQueueMap;
    private AtomicInteger seqId = new AtomicInteger(0);
    private WALReadWriteQueue consumerQueue;
    private Long count = 0l;
    private AtomicLong comming = new AtomicLong(0);
    private long successCount = 0;
    private AtomicLong readSucessCount = new AtomicLong(0);
    private TempResult last;
    private long fialedCount = 0;
    private volatile boolean inital = false;
    public Long getComming(){
        return consumerQueue.getComming();
    }
    public Long getCount(){
        return consumerQueue.getReadingCount();
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
    public void writeMessgae(String message){

        consumerQueue.offerData1(message.getBytes());

    }

    public long getFialedCount() {
        return fialedCount;
    }

    public int readPage(){
        return consumerQueue.getCurrentReadPage();
    }
    public int writePage(){
        return consumerQueue.getCurrentWritePage();
    }

    public long getSuccessCount() {
        return successCount;
    }

    public WALReadWriteQueue getConsumerQueue() {
        return consumerQueue;
    }
//    public byte[] readyData(Message message) {
//        return consumerQueue.readyData(message);
//    }
    public void setConsumerQueue(WALReadWriteQueue consumerQueue) {
        this.consumerQueue = consumerQueue;
    }

    private ScheduledExecutorService executorService;
    public static WALLockQueueInstance INSTANCE = null;
    private String filePath = null;

//    public void writeMessage(Message message) {
////        System.out.println("here is seq id "+message.getHeader().getSeqId());
//        consumerQueue.write(message);
//    }

    public void writeMessge(byte[] datas){
        int write = consumerQueue.offerData1(datas);
//        if (write > 0){
//            successCount ++;
//        }else {
//            fialedCount ++;
//        }
//        comming.incrementAndGet();
    }



    public long getReadSucessCount() {
        return readSucessCount.get();
    }

//    public long getNotNullCount(){
//        return consumerQueue.getNutNullCount();
//    }
    public void read() {
//        Long start = System.currentTimeMillis();
//        byte[] datas = consumerQueue.poll();
        TempResult result = consumerQueue.poll2();
        if (result != null){
//            if (last == null){
//                last = result;
//                readSucessCount.getAndIncrement();
//            }else {
//                if (last.getReaderPosition() != result.getReaderPosition()){
//                    readSucessCount.getAndIncrement();
//                }
//                last = result;
//            }
//            Message message = consumerQueue.readDataToMessage(result);
//
//            if (message != null && !message.getBody().toString().equals("")){
//                readSucessCount.getAndIncrement();
//                logger.info(message.toString());
//            }

        }
//        Long endTime = System.currentTimeMillis();

//        count++;
//        Message message = consumerQueue.readDataToMessage(result);
//        if (message == null){
//            fialedCount++;
//        }
//        if (count< 100)
//        if ((endTime - start) > 10)
//        log.info("message "+message);
    }

    private static class ConsumerQueuePoolHolder{
        private static WALLockQueueInstance pool = new WALLockQueueInstance();
    }
    public synchronized static WALLockQueueInstance getInstance(){
        return ConsumerQueuePoolHolder.pool;
    }
    private WALLockQueueInstance(){
        this.filePath = classPathResource.getPath();
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

    }

    public void init(){
        synchronized (DATA_BACKUP_PATH.intern()){
            if (!inital){
                consumerQueue = new WALReadWriteQueue("first",filePath);

                executorService = Executors.newScheduledThreadPool(1,new NamedThreadFactory());
                executorService.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
                        consumerQueue.sync();
//                deleteBlockFile();
                    }


                },1000l,1000L, TimeUnit.MILLISECONDS);
                inital = true;
            }
        }

    }


}
