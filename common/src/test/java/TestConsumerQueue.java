//
//import com.game.common.model.msg.Header;
//import com.game.common.model.msg.Message;
//import com.game.common.walstore.WALLockQueueInstance;
//import io.netty.util.HashedWheelTimer;
//import io.netty.util.Timeout;
//import io.netty.util.TimerTask;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicInteger;
//
///**
// * @author zheng
// */
////
//public class TestConsumerQueue {
//    private static Logger logger = LoggerFactory.getLogger(TestConsumerQueue.class);
////    @Test
//    public void testConsumerQueue(){
//        WALLockQueueInstance instance = WALLockQueueInstance.getInstance();
////        ConsumerQueue consumerQueue = instance.getConsumerQueue();
////        ConsumerQueue copy = instance.getConsumerQueue();
////        int a = BlockLock.getMod(2);
////        System.out.println("he "+a);
//        ExecutorService service = Executors.newCachedThreadPool();
//        int thread = 32;
//        HashedWheelTimer timer = new HashedWheelTimer();
//        AtomicInteger seq = new AtomicInteger(0);
//        CountDownLatch tastCount = new CountDownLatch(thread);
////        AtomicLong count = new AtomicLong(0);
////        AtomicLong use = new AtomicLong(0);
//        CountDownLatch latch = new CountDownLatch(1);
//        Message message = new Message();
//        message.setHeader(Header.rpcResponseHeader());
//        message.setBody("welcome --------");
//
////                    System.out.println("here is test -----------------");
////        message.getHeader().setSeqId(seq.getAndIncrement());
//        instance.writeMessage(message);
////        instance.read();
//        System.out.println("hereli       000000000000000");
////        for (int tem = 0 ;tem < 400;tem ++){
//////            Message message = new Message();
//////            message.setHeader(Header.rpcResponseHeader());
////            message.setBody("welcome --------");
//////                    System.out.println("here is test -----------------");
////            message.getHeader().setSeqId(seq.getAndIncrement());
////            instance.writeMessage(message);
//////            count.incrementAndGet();
////        }
////        for (int i = 0;i< 401;i++){
////
////            instance.read();
////        }
//        byte[] data = instance.readyData(message);
//        CountDownLatch start = new CountDownLatch(1);
//        for (int i = 0;i<thread ;i++){
//            service.execute(new Runnable() {
//                @Override
//                public void run() {
////                    message.getHeader().setSeqId(seq.getAndIncrement());
//
//                    try {
//                        latch.await();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//
////                    instance.writeMessge(data);
//                    for (int tem = 0 ;tem < 1000000;tem ++){
////                        Message message = new Message();
////                        message.setHeader(Header.rpcResponseHeader());
////                        message.setBody("welcome --------");
////                    System.out.println("here is test -----------------");
////                        message.getHeader().setSeqId(seq.getAndIncrement());
////
////                        instance.writeMessage(message);
////                        message.setBody(tem+ "hello");
//
//                        instance.writeMessge(data);
////                        instance.read();
////                        instance.writeMessgae("test");
////                        count.incrementAndGet();
//                    }
//                    tastCount.countDown();
//
//                }
//            });
//
//        }
//////
////        CountDownLatch countDownLatch = new CountDownLatch(1);
////        for (int t = 0 ;t < 500;t++){
////            service.execute(new Runnable() {
////                @Override
////                public void run() {
////                    try {
////                        latch.await();
//////                        Thread.sleep(100);
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
//////                    instance.read();
////                    for (int j = 0;j < 100000;j++){
//////                            try {
//////                                Thread.sleep(200);
//////                            } catch (InterruptedException e) {
//////                                e.printStackTrace();
//////                            }
////                        instance.read();
//////                            instance.read();
//////                            instance.writeMessage(message);
//////                        use.getAndIncrement();
////                    }
////                    tastCount.countDown();
//////                    if (use.get() == 1900){
//////                        countDownLatch.countDown();
//////                    }
////                }
////            });
////        }
//////
////        try {
////            Thread.sleep(1000);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
//        latch.countDown();
//
////        try {
////            countDownLatch.await();
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
////        logger.info("here is start ----------------------");
//        timer.newTimeout(new TimerTask() {
//            @Override
//            public void run(Timeout timeout) throws Exception {
//                System.out.println("here is queue push data size "+ " and comming request "+instance.getComming() + " and handle "+instance.getCount()+ " null count ");
//                start.countDown();
//            }
//        },1000, TimeUnit.MILLISECONDS);
//        timer.start();
//        try {
//            start.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            tastCount.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
////        timer.stop();
////        System.out.println("here is finish--------------");
////        for (int i = 0 ;i< 30;i++){
////            instance.read();
////        }
////        System.out.println("here is finish ---------data ------------"+consumerQueue.size());
////        int writeIndex = consumerQueue.getWriterIndex().getWriterPosition();
////        System.out.println("here is use index "+writeIndex + " is less than "+ (writeIndex < QueueBlock.BLOCK_SIZE) + " write index "+writeIndex);
////        Message tepm = instance.read();
////        System.out.println("message "+ tepm == null ? "" : tepm.toString());
//        System.out.println("here is count "+instance.getComming() + " use count " + instance.getCount() + " and success count "+instance.getSuccessCount() + " and failed count "+instance.getFialedCount()  + " instance "+instance.readPage()+ " write "+instance.writePage()+ " read success count "+instance.getReadSucessCount() );
//    }
//
//}
