package com.game.mj.util;

import com.game.mj.cache.Operation;
import com.game.mj.cache.ReadWriteLockOperate;
import sun.misc.Contended;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author zheng
 */
public class TimeUtil {
    private static ScheduledExecutorService executor ;
    @Contended
    private  static Long currentTime = 0l;
    private static ReadWriteLockOperate readWriteLockOperate = new ReadWriteLockOperate();
    @Contended
    private  static boolean start = false;
    public static void init(Object object){
        executor = Executors.newSingleThreadScheduledExecutor();
        currentTime=System.currentTimeMillis() ;

        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                currentTime+= 15;
            }
        },0l,15l, TimeUnit.MILLISECONDS);
    }
    public static Long getCurrentTime(){
        readWriteLockOperate.readLockOperation(new Operation() {
            @Override
            public void operate() {
                if (!start){
                    System.out.println("here is initial--------");
                    start = true;
                    init(1);
                }

            }
        });

        return currentTime;
    }
}
