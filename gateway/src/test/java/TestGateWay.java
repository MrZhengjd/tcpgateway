import com.game.mj.cache.MasterInfo;
import com.game.mj.constant.RequestMessageType;
import com.game.mj.eventdispatch.DynamicRegisterGameService;
import com.game.mj.messagedispatch.GameMessageDispatchService;
import com.game.mj.model.GameMessage;
import com.game.gateway.model.BaseChuPaiInfo;
import com.game.network.client.ClientChannelMap;
import com.game.network.client.ConnectTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author zheng
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ConnectTools.class,DynamicRegisterGameService.class, GameMessageDispatchService.class, ClientChannelMap.class})
public class TestGateWay {
    @Autowired
    private ConnectTools connectTools;
    @Autowired
    private DynamicRegisterGameService dynamicRegisterGameService;
    @Test
    public void testSendToGateWay(){
        BaseChuPaiInfo chuPaiInfo = new BaseChuPaiInfo();
        chuPaiInfo.setPaiId(23);
        String host = "127.0.0.1";
        int port = 12365;
        GameMessage gameMessage = dynamicRegisterGameService.getRequestInstanceByMessageId(RequestMessageType.OFFLINE_MESSAGE);
        gameMessage.getHeader().setModuleId(12);
        gameMessage.getHeader().setDescribe("323");
        gameMessage.setMessageData(chuPaiInfo);
        MasterInfo.changeInfo("gateway",10029l);
        connectTools.writeMessage(host,port,gameMessage);
        CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await(15000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
