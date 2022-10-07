package com.game.waitstart.consumer;


import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.game.mj.constant.InfoConstant;
import com.game.mj.eventdispatch.DynamicRegisterGameService;
//import com.game.cserver.messagedispatch.GameMessageDispatchService;
import com.game.mj.model.GameMessage;
import com.game.consumemodel.consume.ConsumerModel;
import com.game.consumemodel.util.DtoMessageUtil;
import com.game.mj.messagedispatch.GameMessageDispatchService;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @author zheng
 */
@Component
@NacosPropertySource(dataId = "nacoswaitstart",autoRefreshed = true)
public class ConsumerAndSendMessage {
    private static Logger logger = LoggerFactory.getLogger(ConsumerAndSendMessage.class);
    @Autowired
    private DynamicRegisterGameService dynamicRegisterGameService;

    @Autowired
    private GameMessageDispatchService gameMessageDispatchService;
    @Autowired
    private ConsumerModel consumerModel;
    @KafkaListener(topics = {"${name:waitstart1}" }, groupId = "test1" )
    public void backupHandle(ConsumerRecord<String ,byte[]> record,Acknowledgment acknowledgment){
//
        GameMessage message = DtoMessageUtil.readMessageHeader(record.value(),dynamicRegisterGameService);
        if (message.getHeader().getServiceId() <= 0){
            acknowledgment.acknowledge();
            logger.info("service id is null --------");
            return;
        }
        consumerModel.consumeMessageToDispatch(message,InfoConstant.GATEWAY_LOGIC_TOPIC)
                .addListener(new GenericFutureListener<Future<Boolean>>() {
                    @Override
                    public void operationComplete(Future<Boolean> future) throws Exception {
                        if (future.isSuccess()){
                            acknowledgment.acknowledge();
                        }
                    }
                });
    }

    public static String generateTopic(String prefix,int serverId) {
        return prefix + "-" + serverId;
    }
//
}
