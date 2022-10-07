package com.game.gateway.server.handler;

import com.game.mj.concurrent.LocalRunner;
import com.game.mj.concurrent.PromiseUtil;
import com.game.mj.model.GameMessage;
import com.game.mj.model.TokenBody;
import com.game.mj.util.MessageKeyUtil;
import com.game.gateway.consume.PlayerInstanceModel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;

/**
 * @author zheng
 */
public class SendMessageUtil {
    /**
     * 获取玩家所在服务器后进行操作
     * @param ctx
     * @param moduleId
     * @param successHandler
     */
    public static void operateAfterSelectServer(TokenBody tokenBody, ChannelHandlerContext ctx, Integer moduleId, SuccessHandler successHandler, GameMessage message, PlayerInstanceModel playerInstanceModel){

        if (tokenBody == null) {// 如果首次通信，获取验证信息
            ConfirmHandler confirmHandler = (ConfirmHandler) ctx.channel().pipeline().get("confirmHandler");
            if (confirmHandler != null){
                tokenBody = confirmHandler.getTokenBody();

            }

        }
        message.getHeader().setPlayerId(tokenBody.getPlayerId());
        Object key = MessageKeyUtil.getMessageKey(message);
        Long playerId = message.getHeader().getPlayerId();
//        ctx.fireChannelRead(message);
        PromiseUtil.safeExecuteWithKey(key, new LocalRunner<Integer>() {
            @Override
            public void task(Promise promise, Integer object) {
                Integer server = playerInstanceModel.selectServerId(playerId, moduleId);
                promise.setSuccess(server);
//                if (server != null){
//                    promise.setSuccess(server);
//                }else {
//                    promise.setFailure(new RuntimeException("not found it"));
//                }
            }
        },null).addListener(new GenericFutureListener<Future<Integer>>() {
            @Override
            public void operationComplete(Future<Integer> future) throws Exception {
                if (future.isSuccess()){
                    successHandler.successHandler(future);
                }else {
                    System.out.println("cannot found the serverId -------");
                }
            }
        });

    }
}
