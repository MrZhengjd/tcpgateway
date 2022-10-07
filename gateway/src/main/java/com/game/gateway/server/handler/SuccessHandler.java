package com.game.gateway.server.handler;


import io.netty.util.concurrent.Future;

/**
 * @author zheng
 */
public interface SuccessHandler {
    void successHandler(Future<Integer> future);
}
