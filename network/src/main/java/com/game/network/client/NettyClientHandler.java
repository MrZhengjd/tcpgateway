package com.game.network.client;

import com.game.mj.concurrent.IGameEventExecutorGroup;
import com.game.mj.concurrent.NonResultLocalRunner;
import com.game.mj.concurrent.PromiseUtil;
import com.game.mj.messagedispatch.GameMessageDispatchService;
import com.game.mj.model.GameMessage;
import com.game.mj.util.MessageKeyUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;

public class NettyClientHandler extends SimpleChannelInboundHandler<GameMessage> {
    private GameMessageDispatchService dispatchService;
    private Boolean isOk = false;
    //利用写空闲发送心跳检测消息
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case WRITER_IDLE:{
//
//                    Header header = new Header("");
//                    Message message = new Message();
//                    ctx.writeAndFlush(message);
//                    ctx.writeAndFlush(pingMsg);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public NettyClientHandler(GameMessageDispatchService dispatchService) {
        this.dispatchService = dispatchService;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();


    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GameMessage msg) throws Exception {
//        System.out.println(msg);
//        System.out.println("welcom-----------");
        PromiseUtil.safeExecuteNonResult(IGameEventExecutorGroup.getInstance().selectByHash(MessageKeyUtil.getMessageKey(msg)), new NonResultLocalRunner() {
            @Override
            public void task() {
                try {
                    dispatchService.sendGameMessage(msg);

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    ReferenceCountUtil.release(msg);
                }

            }
        });



    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);

        Channel channel = ctx.channel();
        if(channel.isActive()){
            ctx.close();
        }


    }
}
