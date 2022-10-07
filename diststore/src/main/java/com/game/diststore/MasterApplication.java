package com.game.diststore;

import com.game.mj.cache.MasterInfo;
import com.game.mj.constant.InfoConstant;
import com.game.mj.constant.RequestMessageType;
import com.game.mj.eventcommand.IEvent;
import com.game.mj.eventcommand.LockQueueMediator;
import com.game.mj.eventdispatch.DynamicRegisterGameService;
import com.game.mj.model.*;
import com.game.mj.model.vo.BandServerVo;
import com.game.network.client.ConnectTools;
import com.game.diststore.role.Master;
import com.game.diststore.service.MasterWrite;
import com.game.diststore.service.RecordComponent;
import com.game.diststore.service.SlaveCopy;
import com.game.diststore.store.UnLockQueueOneInstance;
import com.game.network.server.SuccessHandle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author zheng
 */
@SpringBootApplication(scanBasePackages = "com.game")
public class MasterApplication {
    public static void main(String[] args) {
        MasterInfo.changeInfo("master",10026l);
        ConfigurableApplicationContext run = SpringApplication.run(RecorderApplication.class);
        Master recorder = run.getBean(Master.class);
        MasterWrite masterWrite = run.getBean(MasterWrite.class);
        ConnectTools connectTools = run.getBean(ConnectTools.class);

        DynamicRegisterGameService dynamicRegisterGameService = run.getBean(DynamicRegisterGameService.class);
        recorder.startGame(InfoConstant.LOCAL_HOST, InfoConstant.MASTER_PORT, new SuccessHandle() {
            @Override
            public void afterSueccess() {
//                UnLockQueueOneInstance.getInstance().getMediator().rollback();
                BandSupplyRequest gameMessage = (BandSupplyRequest) dynamicRegisterGameService.getRequestInstanceByMessageId(RequestMessageType.BAND_SERVICE);
//                gameMessage.setBandServerVo();
                gameMessage.setMessageData(new BandServerVo(InfoConstant.LOCAL_HOST,InfoConstant.MASTER_PORT));
                connectTools.writeToRecorder(gameMessage);
                SlaveCopy slaveCopy = run.getBean(SlaveCopy.class);
                slaveCopy.rollback();
                slaveCopy.startCopyData();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("after select master change record-----------");
                        NodeInfo masterInfo = MasterInfo.getMasterInfo();
                        IEvent changeVo = RecordComponent.getCreateEvent("welcome");

                        Integer node = 2;
                        if (masterInfo != null){
                            node = masterInfo.getSelectId();
                        }
                        changeVo.setCalledId(node);
                        LockQueueMediator mediator = UnLockQueueOneInstance.getInstance().getMediator();
                        masterWrite.writeData(mediator,changeVo,MasterInfo.getPlayerId());

                        System.out.println("send message0000000 success");
                    }
                }).start();

            }
        });


    }
}
