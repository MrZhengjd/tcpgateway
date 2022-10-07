package com.game.gateway.consume;

import com.game.mj.concurrent.IGameEventExecutorGroup;
import com.game.mj.concurrent.LocalRunner;
import com.game.mj.concurrent.PromiseUtil;
import com.game.mj.eventdispatch.DynamicRegisterGameService;
import com.game.mj.model.GameMessage;
import com.game.mj.model.MessageSendType;
import com.game.mj.util.MessageKeyUtil;
import com.game.domain.consumer.SendMessageModel;
import com.game.domain.find.FindPlayerId;
import com.game.mj.messagedispatch.GameMessageDispatchService;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

//import com.game.newwork.cache.ChannleMap;

/**
 * @author zheng
 */
@Component
public class ConsumerModel {
    @Autowired
    private SendMessageModel sendMessageModel;
    private static Logger logger = LoggerFactory.getLogger(ConsumerModel.class);
    private static IGameEventExecutorGroup gameEventExecutorGroup = IGameEventExecutorGroup.getInstance();
    @Autowired
    private DynamicRegisterGameService dynamicRegisterGameService;


    @Autowired
    private GameMessageDispatchService gameMessageDispatchService;






    /**
     * 处理完消息后一对一发送给当前的玩家
     * @param key
     * @param localRunner
     * @param message
     * @param baseTopic
     */
    public void oneByOneHandle(final Object key,LocalRunner localRunner,GameMessage message,String baseTopic){
        PromiseUtil.safeExecuteWithKey(key,localRunner,message).addListener(new GenericFutureListener<Future<GameMessage>>() {
            @Override
            public void operationComplete(Future<GameMessage> future) throws Exception {
                if (future.isSuccess()){
                    GameMessage data = future.getNow();
                    sendMessageModel.sendMessageToMq(data,baseTopic,key);
                }
            }
        });
    }



    /**
     * 准备消息发送给多个玩家
     * @param key
     * @param localRunner
     * @param message
     * @param baseTopic
     */
    public void oneByManyHandle(Object key, LocalRunner localRunner, GameMessage message, String baseTopic, FindPlayerId findPlayerId){
        PromiseUtil.safeExecuteWithKey(key,localRunner,message).addListener(new GenericFutureListener<Future<GameMessage>>() {
            @Override
            public void operationComplete(Future<GameMessage> future) throws Exception {
                if (future.isSuccess()){
                    GameMessage data = future.getNow();
                    List<Long> playerIds = findPlayerId.findPlayerIds(data.getHeader());
                    data.getHeader().setToPlayerIds(playerIds);
                    data.getHeader().setSendWay(MessageSendType.ONE_BY_MANY.value);
                    sendMessageModel.sendMessageToMq(data,baseTopic,key);
                }
            }
        });
    }






    /**
     * 消费消息并转发
     * @param record
     * @param baseTopic
     * @param acknowledgment
     */
    public Promise consumeMessageToDispatch(GameMessage message, String baseTopic){
        return PromiseUtil.safeExecuteWithKey(MessageKeyUtil.getMessageKey(message), new LocalRunner<GameMessage>() {
            @Override
            public void task(Promise promise, GameMessage object) {
                try {
                    gameMessageDispatchService.sendGameMessage(object);
                    promise.setSuccess(true);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, message);

    }

}
