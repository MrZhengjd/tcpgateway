package com.game.mj.concurrent;

import java.util.concurrent.*;

/**
 * @author zheng
 */
public class IThreadPool {
    private static ThreadPoolExecutor executor ;
    public static IThreadPool getInstance(){
        return Holder.instance;
    }
    private IThreadPool(){
        executor = new ThreadPoolExecutor(4,4,2000l, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>());
    }
    private static class Holder{
        private static IThreadPool instance = new IThreadPool();
    }
    public void executeRun(Runnable runnable){
        executor.execute(runnable);
    }
}
