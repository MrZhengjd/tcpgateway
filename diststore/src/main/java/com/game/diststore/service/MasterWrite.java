package com.game.diststore.service;

import com.game.mj.constant.RequestMessageType;
import com.game.mj.eventcommand.IEvent;
import com.game.mj.eventcommand.LockQueueMediator;
import com.game.mj.eventdispatch.DynamicRegisterGameService;
import com.game.mj.generator.IdGenerator;
import com.game.mj.generator.IdGeneratorFactory;
import com.game.network.client.ConnectTools;
import com.game.domain.model.msg.ChangeRecorderRequest;
import com.game.domain.model.vo.ChangeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zheng
 */
@Component
public class MasterWrite {
    private IdGenerator idGenerator = IdGeneratorFactory.getDefaultGenerator();
    @Autowired
    private ConnectTools connectTools;
    @Autowired
    private DynamicRegisterGameService dynamicRegisterGameService;
    @Autowired
    private EventHolder eventHolder;

    /**
     * 收到recorder 的确认消息后，写入消息到master
     * @param mediator
     * @param iEvent
     * @param playerId
     */
    public void writeData(LockQueueMediator mediator,IEvent iEvent,Long playerId){
        iEvent.setEventId(idGenerator.generateIdFromServerId(iEvent.getCalledId()));
        ChangeRecorderRequest recorderRequest = (ChangeRecorderRequest) dynamicRegisterGameService.getRequestInstanceByMessageId(RequestMessageType.CHANGE_CORDER);
        ChangeVo changeVo = new ChangeVo();
        changeVo.setNodeId(iEvent.getCalledId());
        changeVo.setOperateId(iEvent.getEventId());
        recorderRequest.getHeader().setPlayerId(playerId);
        recorderRequest.setMessageData(changeVo);
//        recorderRequest.deserialzeToData();
        connectTools.writeToRecorder(recorderRequest);
        iEvent.setUsedIndex(true);
        eventHolder.saveIeventExecutor(iEvent.getEventId(),iEvent,mediator.getDataUnLockQueue().getExecutor());

//        mediator.execute();
    }
}
