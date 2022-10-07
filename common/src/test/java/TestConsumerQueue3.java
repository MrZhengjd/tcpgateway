//
//import com.game.common.concurrent.IGameEventExecutorGroup;
//import com.game.common.concurrent.LocalRunner;
//import com.game.common.concurrent.PromiseUtil;
//import com.game.common.eventcommand.*;
//import com.game.common.model.DtoMessage;
//import com.game.common.model.MessageType;
//import com.game.common.model.anno.DefaultGameMessage;
//import com.game.common.model.anno.GameMessage;
//import com.game.common.model.msg.THeader;
//import com.game.common.store.QueueBlock;
//import com.game.common.walstore.UnLockWALQueue;
//import com.game.common.walstore.UnLockWALQueueInstance;
//import io.netty.util.HashedWheelTimer;
//import io.netty.util.Timeout;
//import io.netty.util.TimerTask;
//import io.netty.util.concurrent.EventExecutor;
//import io.netty.util.concurrent.Future;
//import io.netty.util.concurrent.GenericFutureListener;
//import io.netty.util.concurrent.Promise;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.concurrent.atomic.AtomicLong;
//
///**
// * @author zheng
// */
//
//public class TestConsumerQueue3 {
//    private static Logger logger = LoggerFactory.getLogger(TestConsumerQueue3.class);
////    @Test
//    public void testConsumerQueue(){
//        UnLockWALQueueInstance instance = UnLockWALQueueInstance.getInstance();
//        instance.init();
////        WALLockQueueInstance instance = WALLockQueueInstance.getInstance();
////        ConsumerQueue consumerQueue = instance.getConsumerQueue();
////        ConsumerQueue copy = instance.getConsumerQueue();
////        int a = BlockLock.getMod(2);
////        System.out.println("he "+a);
////        ExecutorService service = Executors.newCachedThreadPool();
//        IGameEventExecutorGroup eventExecutors = IGameEventExecutorGroup.getInstance();
//        int thread = 32;
//        AtomicLong comming = new AtomicLong(0);
//
//        AtomicInteger seq = new AtomicInteger(0);
//        CountDownLatch tastCount = new CountDownLatch(thread);
////        AtomicLong count = new AtomicLong(0);
////        AtomicLong use = new AtomicLong(0);
//        CountDownLatch latch = new CountDownLatch(1);
////        Message message = new Message();
////        message.setHeader(Header.rpcResponseHeader());
////        GameMessage message = new DefaultGameMessage();
////        message.setHeader(new THeader(MessageType.PUSH.value));
////        message.setMessageData("welcome --------");
//
////                    System.out.println("here is test -----------------");
////        message.getHeader().setSeqId(seq.getAndIncrement());
////        instance.writeMessage(message);
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
//        byte[] data = DtoMessage.serializeData(message);
//        CountDownLatch start = new CountDownLatch(1);
//        List<LockQueueMediator> holds = new ArrayList<>();
//        for (int i = 0;i<thread ;i++){
//            final Integer key = i;
//            EventExecutor eventExecutor = eventExecutors.selectByHash(i);
//            PromiseUtil.safeExecute(eventExecutor, new LocalRunner<Void>() {
//                @Override
//                public  void task(Promise promise, Void object) {
//                    UnLockWALQueue unLockWALQueue = instance.getByEventExecutor(eventExecutor);
//
//                    LockQueueMediator mediator = instance.getMediatorByEventExecutor(eventExecutor);
//                    if (!holds.contains(mediator)){
//                        holds.add(mediator);
//                    }
//                    try {
//                        latch.await();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                    long testData = 0;
//                    IEvent iEvent = new IEvent();
//                    iEvent.setType(EventType.CREATE.getType());
//                    iEvent.setCalledId(21);
////                    instance.writeMessge(data);
//                    for (int tem = 0 ;tem < 100;tem ++){
//
//                        mediator.execute(iEvent);
//                    }
//
//                    promise.setSuccess(true);
//                    tastCount.countDown();
//                }
//
//
//            },null);
//
//
//        }
//
//////
////
//        long timeMillis = System.currentTimeMillis();
//        latch.countDown();
////        try {
////            Thread.sleep(1500);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
////        for (int i = 0; i< thread ;i ++){
////
////        }
////        try {
////            countDownLatch.await();
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
////        logger.info("here is start ----------------------");
//        HashedWheelTimer timer = new HashedWheelTimer();
//        timer.newTimeout(new TimerTask() {
//            @Override
//            public void run(Timeout timeout) throws Exception {
//                System.out.println("here is queue push data size "+ " and comming request " + " null count ");
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
//
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
//        Long useTime = System.currentTimeMillis() - timeMillis;
//        Long commingRe = 0l;
//        for (UnLockWALQueue queue : instance.getQueuePools()){
//            commingRe += queue.getComming();
//
//        }
////        eventExecutors.shutdownGracefully();
//        System.out.println("here is count "+ " use count " + " and success count "+commingRe + " and failed count "+instance.getFialedCount()  +  " read success count "+instance.getReadSucessCount() + " used time "+useTime);
////        rotateRunTask(timer,eventExecutors);
////        try {
////            Thread.sleep(500);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
//        for (LockQueueMediator mediator : holds){
////            final Integer key = i;
////            EventExecutor eventExecutor = eventExecutors.selectByHash(i);
////            LockQueueMediator mediator = instance.getMediatorByEventExecutor(eventExecutor);
//            System.out.println("hash "+mediator.getUnLockQueue().getExecutor().hashCode() + " and dir "+mediator.getUnLockQueue().getDir());
//            mediator.clear();
//
//        }
//        UnLockQueueMediatorImpl tem= null;
//        for (LockQueueMediator mediator : holds){
//            mediator.rollback();
////            logger.info("success log int "+mediator.getUnLockQueue().getExecutor().hashCode());
//
//        }
////        try {
////            UnLockQueueMediatorImpl.latch.await();
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
//
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("finish test ----------");
//    }
//    private void rotateRunTask(HashedWheelTimer timer,IGameEventExecutorGroup eventExecutors){
//        timer.newTimeout(new TimerTask() {
//            @Override
//            public void run(Timeout timeout) throws Exception {
//                if (QueueBlock.getReleaseCount() > 0){
//                    logger.info("still have task to run "+QueueBlock.getReleaseCount());
//                    rotateRunTask(timer,eventExecutors);
//                }else {
//                    System.out.println("here is finish count ");
//
//
//                }
//            }
//        },1100,TimeUnit.MILLISECONDS);
//    }
//
//}
