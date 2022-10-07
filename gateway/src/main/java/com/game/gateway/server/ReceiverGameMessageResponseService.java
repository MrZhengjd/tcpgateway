package com.game.gateway.server;

import com.game.mj.concurrent.NonResultLocalRunner;
import com.game.mj.concurrent.PromiseUtil;
import com.game.mj.constant.InfoConstant;
import com.game.mj.model.GameMessage;
import com.game.mj.util.MessageKeyUtil;
import com.game.gateway.server.sendway.SendToPlayerProxy;
import com.game.network.cache.ChannelMap;
//import com.game.common.model.anno.GameMessage;
import com.game.gateway.model.DtoMessage;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ReceiverGameMessageResponseService {
    @Resource
    private ChannelMap channelMap;
	private Logger logger = LoggerFactory.getLogger(ReceiverGameMessageResponseService.class);
//	@Autowired
//	private GatewayServerConfig gatewayServerConfig;
//    @Autowired
//    private ChannelService channelService;
    @Autowired
    private SendToPlayerProxy sendToPlayerProxy;
    @KafkaListener(topics = InfoConstant.GATEWAY_LOGIC_TOPIC+"-"+"${game.waitstart.serverId}", groupId = "test")
    public void receiver(ConsumerRecord<String, byte[]> record, Acknowledgment acknowledgment) {
//        logger.info("here is recie data ");
        GameMessage message = DtoMessage.readMessageHeader(record.value());


        if (message == null ||message.getHeader() == null ){
            logger.error("receive data error"+message);
            acknowledgment.acknowledge();
            return;

        }
        Object key = MessageKeyUtil.getMessageKey(message);
        PromiseUtil.safeExecuteNonResultWithoutExecutor(key, new NonResultLocalRunner() {
            @Override
            public void task() {
                sendToPlayerProxy.sendMessage(message.getHeader().getSendWay(),message, channelMap);
                logger.info("receive data success ");
                acknowledgment.acknowledge();
            }
        });

//
    }
}
