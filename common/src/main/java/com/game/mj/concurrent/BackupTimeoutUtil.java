package com.game.mj.concurrent;

import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Promise;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author zheng
 */
@Log4j2
public class BackupTimeoutUtil {
    private static EventExecutor executor = new DefaultEventExecutor();
    public static void checkTimeout (Promise promise, Long timeout, TimeUnit timeUnit )throws Exception{
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                if (!promise.isSuccess() ){
                    try {
                        promise.cancel(true);

                        promise.tryFailure(new TimeoutException("time out here "));
                        log.info("time out cancel -------"+promise.cause());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        },timeout,timeUnit);
    }
}
