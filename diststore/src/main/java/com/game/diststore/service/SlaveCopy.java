package com.game.diststore.service;

import com.game.mj.cache.MasterInfo;
import com.game.mj.cache.ReadWriteLockOperate;
import com.game.mj.constant.InfoConstant;
import com.game.mj.constant.RequestMessageType;
import com.game.mj.eventcommand.IEvent;
import com.game.mj.eventcommand.LockQueueMediator;
import com.game.mj.eventdispatch.DynamicRegisterGameService;
import com.game.mj.model.*;
import com.game.network.client.ConnectTools;
import com.game.diststore.model.CopyMessage;
import com.game.diststore.store.UnLockQueueOneInstance;
import com.game.diststore.util.BackupUtil;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.Contended;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zheng
 */
@Component
public class SlaveCopy {
    private static Logger logger = LoggerFactory.getLogger(SlaveCopy.class);
    @Contended
    private volatile boolean start;

    private AtomicLong lastCopyId = new AtomicLong(1);
    private EventExecutor eventExecutor = new DefaultEventExecutor();
    private EventExecutor eventExecutor1 = new DefaultEventExecutor();
    @Autowired
    private EventHolder eventHolder;
    protected LockQueueMediator mediator = UnLockQueueOneInstance.getInstance().getMediator();
    private ReadWriteLockOperate readWriteLockOperate = new ReadWriteLockOperate();
    @Autowired
    private DynamicRegisterGameService dynamicRegisterGameService;
    @Autowired
    private ConnectTools connectTools;
    @Autowired
    private BackupUtil backupUtil;
    @Contended
    private volatile boolean rollBack;

    public EventExecutor getEventExecutor() {
        return eventExecutor;
    }

    public EventExecutor getEventExecutor1() {
        return eventExecutor1;
    }

    public void rollback(){
        mediator.rollback();
        if (mediator.getLatestOperateId() > this.lastCopyId.get()){
            this.lastCopyId.set(mediator.getLatestOperateId());
        }

        System.out.println("finish roll back"+this.lastCopyId.get());
    }
    public void startCopyData(){
        synchronized (InfoConstant.SLAVE_COPY.intern()){
            if (!start){
                start = true;
                eventExecutor.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
//                        backupCopy();
                        if (!mediator.getFinishrollback()){
                            return;
                        }
                        rotateMaterCopy(MasterInfo.needCopy(mediator.getLatestOperateId()));
                    }
                },0l,200l, TimeUnit.MILLISECONDS);
                eventExecutor1.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
//                        askForMaster();
                        askMasterChangeInfo();
                    }
                },0, 1, TimeUnit.SECONDS);

            }
        }

    }

    /**
     * 发送请求获取最新的master信息
     */
    private void askForMaster() {
        GameMessage gameMessage = dynamicRegisterGameService.getRequestInstanceByMessageId(RequestMessageType.ASK_MASTER);
        gameMessage.getHeader().setPlayerId(MasterInfo.getPlayerId());
        gameMessage.getHeader().setAttribute(InfoConstant.ASK_ROR_MASTER);
        connectTools.writeToRecorder(gameMessage);

    }


    public void countDown(IEvent iEvent){
        String key = InfoConstant.BACKUP_COPY+lastCopyId.get();
        CountDownLatch countDownLatch = backupUtil.getCountDownLatch(key);
        if (countDownLatch != null){
            countDownLatch.countDown();
            if (iEvent.getEventId() > this.lastCopyId.get()){
                this.lastCopyId.set(iEvent.getEventId());
            }

        }
    }

    /**
     * 请求获得master改变记录
     */
    private void askMasterChangeInfo(){
        if (MasterInfo.getBandInfo() != null){
            GameMessage askMaterChange = dynamicRegisterGameService.getDefaultRequest(RequestMessageType.ASK_MASTER_CHANGE);
            askMaterChange.setMessageData(lastCopyId.get());
            connectTools.writeMessage(InfoConstant.LOCAL_HOST,InfoConstant.RECORDER_PORT,askMaterChange);
        }

    }

    /**
     * 和masterchange 对比过后再去复制更新
     */
    private void compareAndCopy(){
        MasterCopyInfo copyInfo = MasterInfo.needCopy(lastCopyId.get());
        if (copyInfo == null) {
            return;
        }

//        MasterInfo.clearMasterChange();

    }
    /**
     * 一条一条的复制
     * 如何保证顺序性 这里是一条接着一条的
     */
    private void rotateMaterCopy(MasterCopyInfo copyInfo){
        try {
            if (!mediator.getFinishrollback()){
                return;
            }
            if (copyInfo == null){
//            logger.info("copy info is null====================");
                return;
            }
//            if (MasterInfo.checkIsMasterOrRecorder() ){
//                return;
//            }
            String nodeInfo = copyInfo.getHostName();
            String host = nodeInfo.substring(0,nodeInfo.indexOf("-"));
//            if (FastChannelInfo.getChannelInfo().equals(nodeInfo)){
//                return;
//            }
            Integer port = Integer.valueOf(nodeInfo.substring(nodeInfo.indexOf("-")+1));
//        NodeInfo masterInfo = MasterInfo.getMasterInfo();
            if (nodeInfo!= null && (lastCopyId == null ||lastCopyId.get() < copyInfo.getEndCopyId())){
                if (backupUtil.getByKey(InfoConstant.BACK_UP_COPY+lastCopyId)!= null){
                    return;
                }
                logger.info("last copy id "+lastCopyId.get());
                CountDownLatch latch = new CountDownLatch(1);
                backupUtil.saveLatchInfo(InfoConstant.BACKUP_COPY+lastCopyId.get(),latch);
                CopyMessageRequest copyMessage = (CopyMessageRequest) dynamicRegisterGameService.getRequestInstanceByMessageId(RequestMessageType.COPY_DATA);
                logger.info("copy id "+lastCopyId.get());
                CopyMessage info = new CopyMessage(lastCopyId.get(),copyInfo.getEndCopyId());
                copyMessage.setMessageData(info);
//                copyMessage.setLastCopyId(lastCopyId);
                copyMessage.getHeader().setAttribute(InfoConstant.BACK_UP_COPY);
                connectTools.writeMessage(host,port,copyMessage);

                try {
                    latch.await(10,TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
//                lastCopyId.set(mediator.getLatestOperateId());
//                System.out.println("finish copy data once -----------");

                if (lastCopyId.get() <copyInfo.getEndCopyId()){
                    System.out.println("rotate -------");
                    rotateMaterCopy(copyInfo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    /**
     * 一条一条的复制
     * 如何保证顺序性 这里是一条接着一条的
     */
    private void rotateCopy(){
        NodeInfo masterInfo = MasterInfo.getMasterInfo();
        if (masterInfo!= null && (lastCopyId == null ||lastCopyId.get() < masterInfo.getLatestOperateId())){
            CopyMessageRequest copyMessage = (CopyMessageRequest) dynamicRegisterGameService.getRequestInstanceByMessageId(RequestMessageType.COPY_DATA);
            copyMessage.setMessageData(lastCopyId.get());
            copyMessage.getHeader().setAttribute(InfoConstant.BACK_UP_COPY);
            connectTools.writeMessage(masterInfo.getHost(),masterInfo.getPort(),copyMessage);
            CountDownLatch latch = new CountDownLatch(1);
            backupUtil.saveLatchInfo(InfoConstant.BACKUP_COPY+lastCopyId.get(),latch);
            try {
                latch.await(10,TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
            lastCopyId.set(mediator.getLatestOperateId());
            System.out.println("finish copy data once -----------");
            if (lastCopyId.get() < masterInfo.getLatestOperateId()){
                rotateCopy();
            }
        }
    }
}
