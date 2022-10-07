package com.game.mj.concurrent;

import io.netty.util.concurrent.Promise;

/**
 * @author zheng
 */
public interface LocalRunner<T> {
//    void task(Promise<?> promise);
    void task(Promise<?> promise,T object);
}
