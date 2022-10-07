

import com.game.mj.walstore.WALLockQueueInstance;
import io.netty.util.HashedWheelTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zheng
 */

public class TestConsumerQueue1 {
    private static Logger logger = LoggerFactory.getLogger(TestConsumerQueue1.class);
//    @Test
    public void testConsumerQueue(){
        WALLockQueueInstance instance = WALLockQueueInstance.getInstance();
//        ConsumerQueue consumerQueue = instance.getConsumerQueue();
//        ConsumerQueue copy = instance.getConsumerQueue();
//        int a = BlockLock.getMod(2);
//        System.out.println("he "+a);
        ExecutorService service = Executors.newCachedThreadPool();
        int thread = 200;
        HashedWheelTimer timer = new HashedWheelTimer();
        AtomicInteger seq = new AtomicInteger(0);
        CountDownLatch tastCount = new CountDownLatch(thread);
//        AtomicLong count = new AtomicLong(0);
//        AtomicLong use = new AtomicLong(0);
        CountDownLatch latch = new CountDownLatch(1);
//        Message message = new Message();
////        message.setHeader(Header.rpcResponseHeader());
////        message.setBody("welcome --------");
////
//////                    System.out.println("here is test -----------------");
//////        message.getHeader().setSeqId(seq.getAndIncrement());
////        instance.writeMessage(message);
//        instance.read();
        System.out.println("hereli       000000000000000");
//        for (int tem = 0 ;tem < 400;tem ++){
////            Message message = new Message();
////            message.setHeader(Header.rpcResponseHeader());
//            message.setBody("welcome --------");
////                    System.out.println("here is test -----------------");
////            message.getHeader().setSeqId(seq.getAndIncrement());
//            instance.writeMessage(message);
////            count.incrementAndGet();
//        }
        for (int i = 0;i< 401;i++){

            instance.read();
        }

////

////
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        try {
//            countDownLatch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        logger.info("here is start ----------------------");



//        timer.stop();
//        System.out.println("here is finish--------------");
//        for (int i = 0 ;i< 30;i++){
//            instance.read();
//        }
//        System.out.println("here is finish ---------data ------------"+consumerQueue.size());
//        int writeIndex = consumerQueue.getWriterIndex().getWriterPosition();
//        System.out.println("here is use index "+writeIndex + " is less than "+ (writeIndex < QueueBlock.BLOCK_SIZE) + " write index "+writeIndex);
//        Message tepm = instance.read();
//        System.out.println("message "+ tepm == null ? "" : tepm.toString());
        System.out.println("here is count "+instance.getComming() + " use count " + instance.getCount() + " and success count "+instance.getSuccessCount() + " and failed count "+instance.getFialedCount()  + " instance "+instance.readPage()+ " write "+instance.writePage()+ " read success count "+instance.getReadSucessCount() );
    }

}
