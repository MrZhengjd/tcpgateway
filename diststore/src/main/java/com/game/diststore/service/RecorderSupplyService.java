package com.game.diststore.service;


import com.game.mj.cache.ReadWriteLockOperate;
import com.game.mj.eventcommand.IEvent;
import com.game.mj.eventcommand.LockQueueMediator;
import com.game.mj.eventdispatch.DynamicRegisterGameService;
import com.game.mj.model.*;
import com.game.mj.model.vo.BandServerVo;
import com.game.mj.serialize.DataSerialize;
import com.game.mj.serialize.DataSerializeFactory;
import com.game.diststore.store.UnLockQueueOneInstance;
import com.game.diststore.util.BackupUtil;
import com.game.diststore.util.HandleUtil;
import com.game.mj.messagedispatch.GameDispatchService;
import com.game.mj.messagedispatch.GameMessageListener;
import com.game.domain.model.msg.AskMasterRequest;
import com.game.domain.model.msg.ChangeRecorderRequest;
import com.game.domain.model.vo.ChangeVo;
import com.game.network.cache.ChannelMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

/**
 * @author zheng
 */
@GameDispatchService
@Service
public class RecorderSupplyService {
    private static Logger logger = LoggerFactory.getLogger(RecorderSupplyService.class);
    @Autowired
    private ChannelMap channelMap;
    @Autowired
    private DynamicRegisterGameService dynamicRegisterGameService;
    @Autowired
    private RecordComponent recordComponent;
    private ReadWriteLockOperate readWriteLockOperate = new ReadWriteLockOperate();
    protected LockQueueMediator mediator = UnLockQueueOneInstance.getInstance().getMediator();
    private DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();
    @Autowired
    private BackupUtil backupUtil;
//    private ReadWriteLockOperate readWriteLockOperate = new ReadWriteLockOperate();
    @GameMessageListener(value = ChangeRecorderRequest.class)
    public void handle(ChangeRecorderRequest changeRecorderRequest){
        ChangeVo changeVo = (ChangeVo) changeRecorderRequest.deserialzeToData(ChangeVo.class);
        logger.info("here is the changevo comming");
        if (changeVo == null){
            return;
        }
//        BaseChuPaiInfo deserialize = chuPaiMessage.getData();
        logger.info("here is the request to chage record");
        boolean success = recordComponent.writeData(changeVo.getNodeId(), changeVo.getOperateId(), mediator);

//        GameMessage response = dynamicRegisterGameService.getResponseByMessageIdSimple(changeRecorderRequest.getHeader());
//        if (success){
//            response.setMessageData(ResponseVo.success(deserialize));
//        }else {
//            response.setMessageData(ResponseVo.fail("not success"));
//        }
//        response.getHeader().setPlayerId(changeRecorderRequest.getHeader().getPlayerId());
//        channelMap.writeResponse(response);
        HandleUtil.handleAndSendToRequestPlayer(changeRecorderRequest,changeVo,dynamicRegisterGameService,channelMap,success);
        if (success)
        logger.info("welcome dispatch --------------------"+(recordComponent.getMaster() == null? "null ": recordComponent.getMaster().getLatestOperateId()));
    }

    @GameMessageListener(value = AskMasterRequest.class)
    public void askMaster(AskMasterRequest askMasterRequest){
        GameMessage response = dynamicRegisterGameService.getResponseInstanceByMessageId(askMasterRequest.getHeader().getServiceId());
        NodeInfo master = recordComponent.getMaster();
//        if (master == null){
//            response.setMessageData(ResponseVo.fail("fail"));
//        }else {
//            response.setMessageData(ResponseVo.success(master));
////            MasterInfo.changeMaster(master);
//        }
//
//        response.getHeader().setPlayerId(askMasterRequest.getHeader().getPlayerId());
//        channelMap.writeResponse(response);
        HandleUtil.handleAndSendToRequestPlayer(askMasterRequest,master,dynamicRegisterGameService,channelMap,master == null);

    }

    @GameMessageListener(value = BandSupplyRequest.class)
    public void bandSupportService(BandSupplyRequest bandSupplyRequest){
        BandServerVo serverVo = (BandServerVo) bandSupplyRequest.deserialzeToData(BandServerVo.class);
        recordComponent.addAddressToNodeMap(new InetSocketAddress(serverVo.getHost(),serverVo.getPort()));
        HandleUtil.handleAndSendToRequestPlayer(bandSupplyRequest,"success",dynamicRegisterGameService,channelMap,true);
    }

    private IEvent readFromDisk() {
        return null;
    }


}
