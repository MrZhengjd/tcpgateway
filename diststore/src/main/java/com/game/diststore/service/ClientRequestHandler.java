package com.game.diststore.service;

import com.game.mj.cache.MasterInfo;
import com.game.mj.concurrent.NonResultLocalRunner;
import com.game.mj.concurrent.PromiseUtil;
import com.game.mj.eventcommand.LockQueueMediator;
import com.game.mj.model.*;
import com.game.mj.serialize.DataSerialize;
import com.game.mj.serialize.DataSerializeFactory;
import com.game.diststore.store.UnLockQueueOneInstance;
import com.game.diststore.util.BackupUtil;
import com.game.mj.messagedispatch.GameDispatchService;
import com.game.mj.messagedispatch.GameMessageListener;
import com.game.domain.model.msg.AskMasterResponse;
import com.game.domain.model.msg.ChangeRecorderResponse;
import com.game.domain.model.vo.ChangeVo;
import io.netty.util.concurrent.EventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zheng
 */
@GameDispatchService
@Service
public class ClientRequestHandler {
    private static Logger logger = LoggerFactory.getLogger(ClientRequestHandler.class);
    protected LockQueueMediator mediator = UnLockQueueOneInstance.getInstance().getMediator();
    private DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();
    @Autowired
    private EventHolder eventHolder;
    @Autowired
    private RecordComponent recordComponent;
    @Autowired
    private BackupUtil backupUtil;
    @GameMessageListener(value = ChangeRecorderResponse.class)
    public void handle(ChangeRecorderResponse changeRecorderRequest){
        ResponseVo deserialize = (ResponseVo) changeRecorderRequest.deserialzeToData(ResponseVo.class);
//        BaseChuPaiInfo deserialize = chuPaiMessage.getData();
        if (deserialize.getCode() != ResultStatus.SUCCESS.getValue()){
            logger.info("failed------------");
            return;
        }
        ChangeVo changeVo = (ChangeVo) deserialize.getData();
        EventExecutor executoByKey = eventHolder.getExecutoByKey(changeVo.getOperateId());
        if (executoByKey == null){
            throw new RuntimeException("empty executor -----------");
        }
        PromiseUtil.safeExecuteNonResult(executoByKey, new NonResultLocalRunner() {
            @Override
            public void task() {
                mediator.execute(eventHolder.getByKey(changeVo.getOperateId()));
               logger.info("welcome dispatch --------------------");
            }
        });

    }

    /**
     * 获取master 最新的operateid
     * @param copyMessageRequest
     */
    @GameMessageListener(value = AskCopyInfoResponse.class)
    public void handleAskCopyInfo(AskCopyInfoResponse copyMessageRequest){
        ResponseVo result = (ResponseVo) copyMessageRequest.deserialzeToData(ResponseVo.class);
//        System.out.println("here is the request ask for copyinfo");
        if (result.getCode() != ResultStatus.SUCCESS.getValue()){
            return;
        }
        CopyInfo copyInfo = (CopyInfo) result.getData();
        if (copyInfo == null){
            return;
        }
//        String host = copyInfo.getNodeInfo().substring(0,copyInfo.getNodeInfo().indexOf("-"));
//        int port = Integer.valueOf(copyInfo.getNodeInfo().substring(copyInfo.getNodeInfo().indexOf("-")+1));
//        InetSocketAddress address = new InetSocketAddress(host,port);
//        recordComponent.addAddressToNodeMap(address);
//        String key = TopicUtil.generateTopic(InfoConstant.SELECT,recordComponent.getSelectCount());
        backupUtil.saveNodeInfo(copyInfo.getKey(),copyInfo);
        recordComponent.countDown(copyInfo.getKey());

        logger.info("here is finish ask copy info");
    }
    @GameMessageListener(value = AskMasterResponse.class)
    public void handleAskMaster(AskMasterResponse changeRecorderRequest){
        ResponseVo deserialize = (ResponseVo) changeRecorderRequest.deserialzeToData(ResponseVo.class);
        if (deserialize.getCode() != ResultStatus.SUCCESS.getValue()){
            return;
        }
        NodeInfo master = (NodeInfo) deserialize.getData();
        MasterInfo.changeMaster(master);

    }

    @GameMessageListener(value = BandSupplyResponse.class)
    public void handleBandMessage(BandSupplyResponse bandSupplyResponse){
        ResponseVo deserialize = (ResponseVo) bandSupplyResponse.deserialzeToData(ResponseVo.class);
        if (deserialize.getCode() != ResultStatus.SUCCESS.getValue()){
            return;
        }
        MasterInfo.bandServer();

    }
}
