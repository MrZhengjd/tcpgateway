package com.game.mj.model;

import io.netty.channel.ChannelFuture;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sun.misc.Contended;

import java.util.concurrent.CountDownLatch;

/**
 * @author zheng
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChannelInfo {
    private ChannelFuture channelFuture;
    //0 还没建立链接  1 是已经建立链接还没认证
    // 2是认证后 可以发送消息了
    private Integer status;
    @Contended
    private volatile boolean sendAuth;
    private CountDownLatch latch = new CountDownLatch(1);
    private String host;
    private Integer port;
}
