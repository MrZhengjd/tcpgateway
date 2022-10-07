package com.game.consumemodel.consume;

import com.game.mj.constant.InfoConstant;
import com.game.mj.eventdispatch.DynamicRegisterGameService;
import com.game.mj.model.GameMessage;
import com.game.consumemodel.util.DtoMessageUtil;
//import com.game.domain.consume.ConsumerModel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author zheng
 */
@Component
public class OnOffLineCenter {
    @Autowired
    private ConsumerModel model;
    @Autowired
    private DynamicRegisterGameService dynamicRegisterGameService;
    @KafkaListener(topics = {"online" }, groupId = "test" )
    public void online(ConsumerRecord<String, byte[]> record, Acknowledgment acknowledgment) {
        GameMessage message = DtoMessageUtil.readMessageHeader(record.value(),dynamicRegisterGameService);
        model.consumeMessageToDispatch(message, InfoConstant.GATEWAY_LOGIC_TOPIC)
        .addListener(new GenericFutureListener<Future<Boolean>>() {
            @Override
            public void operationComplete(Future<Boolean> future) throws Exception {
                if (future.isSuccess()){
                    acknowledgment.acknowledge();
                }
            }
        });
    }
    @KafkaListener(topics = {"offline" }, groupId = "test" )
    public void offline(ConsumerRecord<String, byte[]> record, Acknowledgment acknowledgment) {
        GameMessage message = DtoMessageUtil.readMessageHeader(record.value(),dynamicRegisterGameService);
        model.consumeMessageToDispatch(message, InfoConstant.GATEWAY_LOGIC_TOPIC)
                .addListener(new GenericFutureListener<Future<Boolean>>() {
                    @Override
                    public void operationComplete(Future<Boolean> future) throws Exception {
                        if (future.isSuccess()){
                            acknowledgment.acknowledge();
                        }
                    }
                });
    }
}
