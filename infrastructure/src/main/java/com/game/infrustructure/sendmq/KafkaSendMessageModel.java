package com.game.infrustructure.sendmq;

import com.game.mj.model.GameMessage;
import com.game.mj.serialize.DataSerialize;
import com.game.mj.serialize.DataSerializeFactory;
import com.game.domain.consumer.SendMessageModel;
import com.game.domain.model.DtoMessage;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author zheng
 */
@Service
public class KafkaSendMessageModel implements SendMessageModel {
    private Logger logger = LoggerFactory.getLogger(KafkaSendMessageModel.class);
    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate;
    private DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();



    @Override
    public void sendMessageToMq(GameMessage data, String baseTopic, Object key) {
        if (data.getData() == null){
            logger.info("here is the error log for empty data "+data.getHeader());
        }
        byte[] value = DtoMessage.serializeData(data);// 向消息总线服务发布客户端请求消息。
//        byte[] value = dataSerialize.serialize(data);
//        String topic = TopicUtil.generateTopic(baseTopic,data.getHeader().getTraceId());
        ProducerRecord<String, byte[]> send = new ProducerRecord<String, byte[]>(baseTopic, String.valueOf(key), value);
        kafkaTemplate.send(send);
        logger.info("send message to topic "+baseTopic+ " successful");
    }
}
