package com.game.diststore.role;

import com.game.mj.concurrent.IGameEventExecutorGroup;
import com.game.mj.eventdispatch.DynamicRegisterGameService;
import com.game.mj.eventdispatch.EventAnnotationManager;
import com.game.diststore.server.handler.ConfirmHandler;
import com.game.diststore.server.handler.TGameDispatchHandler;
import com.game.network.coder.TMessageDecoderPro;
import com.game.network.coder.TMessageEncoderPro;
import com.game.network.server.handler.HeartbeatHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zheng
 */
@Component
public class Recorder extends BaseRole{

    @Autowired
    private EventAnnotationManager eventAnnotationManager;

    @Autowired
    private DynamicRegisterGameService dynamicRegisterGameService;
    private ChannelInitializer initializer(){
//        TGameDispatchHandler gameDispatchHandler = new TGameDispatchHandler(jsonRedisManager, channleMap, playerRoomService,dynamicRegisterGameService,playerInstanceService,kafkaTemplate);
        ChannelInitializer initializer = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast("encoder", new TMessageEncoderPro())
                        .addLast("decoder", new TMessageDecoderPro(dynamicRegisterGameService))
                        .addLast("confirmHandler",new ConfirmHandler(channelMap,eventAnnotationManager,dynamicRegisterGameService))
//                        .addLast("request limit",new RequestRateLimiterHandler(globalRateLimiter,100000))

                        .addLast(new IdleStateHandler(5, 5, 10))
                        .addLast("heartbeart handler",new HeartbeatHandler())
                        .addLast(IGameEventExecutorGroup.getInstance(), new TGameDispatchHandler(channelMap,dynamicRegisterGameService,dispatchService))
                ;
//

            }
        };
        return initializer;
    }


    @Override
    public ChannelInitializer generate() {
        return initializer();
    }
}
