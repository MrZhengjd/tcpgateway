package com.game.mj.concurrent;

import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Promise;

import java.util.concurrent.TimeUnit;

/**
 * @author zheng
 */
public class PromiseUtil {

    public static void timerTask(EventExecutor executor,Runnable runnable,Long time,TimeUnit timeUnit){
        executor.schedule(runnable,time,timeUnit);
    }

    public static void keyTimerTask(Object key,Runnable runnable,Long time,TimeUnit timeUnit){
        timerTask(IGameEventExecutorGroup.getInstance().selectByHash(key),runnable,time,timeUnit);
    }
    public static void safeExecuteNoResultByKey(Object key,NonResultLocalRunner localRunner){
        safeExecuteNonResult(IGameEventExecutorGroup.getInstance().selectByHash(key),localRunner);
    }
    public static void safeExecuteNonResult(EventExecutor executor,final NonResultLocalRunner localRunner) {
        Promise promise = new DefaultPromise(executor);
        if (executor.inEventLoop()) {
            runLocalWithoutPromise(localRunner);
            promise.setSuccess(true);
        } else {
            executor.execute(new Runnable() {
                @Override
                public void run() {

                    runLocalWithoutPromise(localRunner);
                    promise.setSuccess(true);
                }
            });

        }

//        executor.execute(task);
    }

    public static void safeExecuteNonResultWithoutExecutor(Object hash, NonResultLocalRunner localRunner) {
        EventExecutor executor = IGameEventExecutorGroup.getInstance().selectByHash(hash);
        safeExecuteNonResult(executor,localRunner);

//        executor.execute(task);
    }
    public static <T>Promise safeExecuteWithRollBack( EventExecutor executor, LocalRunner localRunner,RollBack rollBack,T t) {
        Promise promise =new DefaultPromise<>(executor);

        if (executor.inEventLoop()) {
            runLocalWithRollBack(localRunner,promise,rollBack,t);

        } else {
            executor.execute(new Runnable() {
                @Override
                public void run() {

                    runLocalWithRollBack(localRunner,promise,rollBack,t);
                }
            });


        }
        return promise;
//        executor.execute(task);
    }
    public static <T> Promise safeExecuteWithKey( Object key, LocalRunner localRunner,T t) {
        EventExecutor executor = IGameEventExecutorGroup.getInstance().selectByHash(key);
        return safeExecute(executor,localRunner,t);
    }
    public static <T> Promise safeExecute( EventExecutor executor, LocalRunner localRunner,T t) {
        Promise promise =new DefaultPromise<>(executor);

        if (executor.inEventLoop()) {
            runLocal(localRunner,promise,t);

        } else {
            executor.execute(new Runnable() {
                @Override
                public void run() {

                    runLocal(localRunner,promise,t);
                }
            });


        }
        return promise;
//        executor.execute(task);
    }

    /**
     * 需要做到幂等性
     * @param localRunner
     * @param promise
     */
    private static <T>void  runLocal(LocalRunner localRunner,Promise promise,T t){
        try {
            BackupTimeoutUtil.checkTimeout(promise,5000l,TimeUnit.MILLISECONDS);
            localRunner.task(promise,t);

        }catch (Throwable e){
            if (!promise.isDone()){
                promise.setFailure(e);
            }

            e.printStackTrace();
        }
    }

    /**
     * 需要做到幂等性
     * @param localRunner
     * @param promise
     * @param rollBack
     */
    private static <T>void runLocalWithRollBack(LocalRunner localRunner,Promise promise,RollBack rollBack,T t){
        try {
            BackupTimeoutUtil.checkTimeout(promise,5000l,TimeUnit.MILLISECONDS);
            localRunner.task(promise,t);

        }catch (Throwable e){
            if (!promise.isDone()){
                promise.setFailure(e);
            }else {
                rollBack.rollBack(t);
            }

            e.printStackTrace();
        }
    }
    private static void runLocalWithoutPromise(NonResultLocalRunner localRunner){
        try {
            localRunner.task();
        }catch (Exception e){

            e.printStackTrace();
        }
    }
}
