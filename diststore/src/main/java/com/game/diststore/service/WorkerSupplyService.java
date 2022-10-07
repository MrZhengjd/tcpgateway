package com.game.diststore.service;


import com.game.mj.cache.FastChannelInfo;
import com.game.mj.cache.MasterInfo;
import com.game.mj.constant.InfoConstant;
import com.game.mj.constant.RequestMessageType;
import com.game.mj.eventcommand.IEvent;
import com.game.mj.eventcommand.LockQueueMediator;
import com.game.mj.eventdispatch.DynamicRegisterGameService;
import com.game.mj.model.*;
import com.game.mj.model.vo.MasterChangeVo;
import com.game.mj.serialize.DataSerialize;
import com.game.mj.serialize.DataSerializeFactory;
import com.game.network.client.ConnectTools;
import com.game.network.client.SendMessageSerialize;
import com.game.diststore.model.CopyMessage;
import com.game.diststore.store.UnLockQueueOneInstance;
import com.game.diststore.util.HandleUtil;
import com.game.mj.messagedispatch.GameDispatchService;
import com.game.mj.messagedispatch.GameMessageDispatch;
import com.game.mj.messagedispatch.GameMessageListener;
import com.game.network.cache.ChannelMap;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.Contended;

/**
 * 做为从机 也向recorder 提供服务
 * @author zheng
 */
@GameDispatchService
@Service
public class WorkerSupplyService {
    private static Logger logger = LoggerFactory.getLogger(WorkerSupplyService.class);
    @Autowired
    private ChannelMap channelMap;
    @Autowired
    private DynamicRegisterGameService dynamicRegisterGameService;
    @Autowired
    private RecordComponent recordComponent;
    @Autowired
    private ConnectTools connectTools;
    @Autowired
    private SlaveCopy slaveCopy;
    protected LockQueueMediator mediator = UnLockQueueOneInstance.getInstance().getMediator();
    private DataSerialize dataSerialize = DataSerializeFactory.getInstance().getDefaultDataSerialize();
    @Contended
    private volatile long copyMessageId = -8;
//    @Autowired
//    private SendMessageSerialize sendMessageSerialize;
    @GameMessageListener(value = AuthMessageResponse.class)
    public void handleAuthRequest(AuthMessageResponse response){

        ResponseVo result = (ResponseVo) response.deserialzeToData(ResponseVo.class);
        System.out.println("here is handle for auth "+result);
        if ( result!= null && result.getCode() == ResultStatus.SUCCESS.getValue()){
//            startMap.get(key).getLatch().countDown();
//            connectTools.getByKey((String) result.getData()).getLatch().countDown();
            SendMessageSerialize.changeInfoStatus((String)result.getData());
            SendMessageSerialize.sendMessageInSerialize((String)result.getData(),connectTools);
        }
    }
    /**
     * 获取master 最新的operateid
     * @param copyMessageRequest
     */
    @GameMessageListener(value = AskCopyInfoRequest.class)
    public void askMasterlatestOperateInfo(AskCopyInfoRequest copyMessageRequest){
        try {
            String key = (String) copyMessageRequest.deserialzeToData(String.class);
//        GameMessage response = dynamicRegisterGameService.getResponseInstanceByMessageId(copyMessageRequest.getHeader().getServiceId());
            Long latestOperateId = mediator.getLatestOperateId();

            CopyInfo copyInfo =new CopyInfo(latestOperateId, recordComponent.getCurrentNode(),FastChannelInfo.getChannelInfo(),key);
//
            HandleUtil.handleAndSendToRequestPlayer(copyMessageRequest,copyInfo,dynamicRegisterGameService,channelMap,true);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
//    @GameMessageListener(value = AskMasterChangeResponse.class)
//    public void handleMasterChange(AskMasterChangeResponse askMasterChangeResponse){
//        ResponseVo re = (ResponseVo) askMasterChangeResponse.deserialzeToData();
//        if (re.getCode() != ResultStatus.SUCCESS.getValue()){
//            return;
//        }
//        MasterChangeVo vo = (MasterChangeVo) re.getData();
//        if (vo == null){
//            return;
//        }
//        MasterInfo.clearMasterChange();
//        MasterInfo.saveMasterChangeVo(InfoConstant.MASTER,vo);
//    }
    @GameMessageDispatch(value = @HeaderAnno(serviceId = RequestMessageType.ASK_MASTER_CHANGE,messageType = MessageType.RPCRESPONSE))
    public void handleMasterChange1(DefaultGameMessage askMasterChangeResponse){
        ResponseVo re = (ResponseVo) askMasterChangeResponse.deserialzeToData(ResponseVo.class);
        if (re.getCode() != ResultStatus.SUCCESS.getValue()){
            return;
        }
        MasterChangeVo vo = (MasterChangeVo) re.getData();
        if (vo == null){
            return;
        }
        MasterInfo.clearMasterChange();
        MasterInfo.saveMasterChangeVo(InfoConstant.MASTER,vo);
    }
    @GameMessageListener(value = CopyMessageRequest.class)
    public void copyData(CopyMessageRequest copyMessageRequest){
//        Long copyId = (Long) copyMessageRequest.deserialzeToData();
        CopyMessage copyMessage = (CopyMessage) copyMessageRequest.deserialzeToData(CopyMessage.class);
        logger.info("copy message here "+copyMessage.getLastCopyId());
//        GameMessage response = dynamicRegisterGameService.getResponseByMessageIdSimple(copyMessageRequest.getHeader());
        mediator.getNextEvent(copyMessage.getLastCopyId(),copyMessage.getEndCopyId()).addListener(new GenericFutureListener<Future<? super IEvent>>() {
            @Override
            public void operationComplete(Future<? super IEvent> future) throws Exception {
                if (future.isSuccess() && future.get() != null){
                    HandleUtil.handleAndSendToRequestPlayer(copyMessageRequest,future.get(),dynamicRegisterGameService,channelMap,true);
                }else {
                    HandleUtil.handleAndSendToRequestPlayer(copyMessageRequest,"failed",dynamicRegisterGameService,channelMap,false);
                }

            }
        });


    }


    @GameMessageListener(value = CopyMessageResponse.class)
    public void copyMasterToLocal(CopyMessageResponse copyMessageRequest){

//        GameMessage response = dynamicRegisterGameService.getResponseByMessageIdSimple(copyMessageRequest.getHeader());
        try {
            logger.info("here is  copy id");
            ResponseVo result = (ResponseVo) copyMessageRequest.deserialzeToData(ResponseVo.class);
            if (result.getData() == null){
                logger.info("receive data is empty");
                return;
            }

            IEvent event = (IEvent) result.getData();
            if (event != null){
                logger.info("here is handle copy id"+event.getEventId());
                if(event.getEventId() <= copyMessageId){
                    return;
                }
                copyMessageId = event.getEventId();
            }else {
                logger.info("copy data is empty-------");
                return;
            }

            mediator.execute(event);
            slaveCopy.countDown(event);

            System.out.println("here is handle copy message -----"+event.getEventId());
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    /**
     * 从磁盘读取数据
     * @return
     */
    private IEvent readFromDisk() {
        return null;
    }
}
