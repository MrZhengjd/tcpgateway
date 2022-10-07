
import com.game.mj.cache.Operation;
import com.game.mj.cache.ReadWriteLockOperate;
import com.game.mj.cache.ReturnOperate;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zheng
 */
public class ChannelMap {
    private static Map<Integer , ChannelFuture> channelMap = new HashMap<>();
    private static AtomicInteger count = new AtomicInteger(0);
    private static ReadWriteLockOperate readWriteLockOperate = new ReadWriteLockOperate();
    public static ChannelFuture getByKey(String key){
        return readWriteLockOperate.readLockReturnOperation(new ReturnOperate<ChannelFuture>() {
            @Override
            public ChannelFuture operate() {
                return channelMap.get(key);
            }
        });
    }

//    public static void rotateSendMessage(Message message){
//        readWriteLockOperate.readLockOperation(new Operation() {
//            @Override
//            public void operate() {
//                for (Map.Entry<Integer , ChannelFuture> entry : channelMap.entrySet()){
////                    message.getHeader().setServiceId(entry.getKey());
//                    try {
////                        entry.getValue().channel().writeAndFlush(message).addListener(new GenericFutureListener<Future<? super Void>>() {
////                            @Override
////                            public void operationComplete(Future<? super Void> future) throws Exception {
//////                                if (!future.isSuccess()){
//////                                    System.out.println("fail to send message");
//////                                }
////
////                            }
////                        });
//                        batchWrite(entry.getValue().channel(),message);
//
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        });
//
//    }
//    public static void rotateSendMessage(GameMessage message){
//        readWriteLockOperate.readLockOperation(new Operation() {
//            @Override
//            public void operate() {
//                for (Map.Entry<Integer , ChannelFuture> entry : channelMap.entrySet()){
////                    message.getHeader().setServiceId(entry.getKey());
//                    try {
////                        entry.getValue().channel().writeAndFlush(message).addListener(new GenericFutureListener<Future<? super Void>>() {
////                            @Override
////                            public void operationComplete(Future<? super Void> future) throws Exception {
//////                                if (!future.isSuccess()){
//////                                    System.out.println("fail to send message");
//////                                }
////
////                            }
////                        });
//                        batchWrite(entry.getValue().channel(),message);
//
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        });
//
//    }
    public static void batchWrite(Channel channel, Object message){
        for (int i = 0;i< 50;i++){
            channel.write(message);
        }
        channel.flush();


    }
    public static void saveChannelFuture(Integer key, ChannelFuture channelFuture){
        readWriteLockOperate.writeLockOperation(new Operation() {
            @Override
            public void operate() {
                channelMap.put(key,channelFuture);
            }
        });
    }
}
