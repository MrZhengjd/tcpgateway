package com.game.mj.model;

import com.game.mj.eventdispatch.Event;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.InetSocketAddress;

/**
 * @author zheng
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OnlineEvent implements Event {
    private InetSocketAddress address;
    private Channel channel;
}
