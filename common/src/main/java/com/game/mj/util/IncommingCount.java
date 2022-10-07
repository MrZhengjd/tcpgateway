package com.game.mj.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zheng
 */
public class IncommingCount {
    public static AtomicLong incomming = new AtomicLong(0);
    public static AtomicLong sending = new AtomicLong(0);
    public static AtomicLong decode = new AtomicLong(0);
    public static Long getDecode(){
        return decode.get();
    }
    public static Long getAndIncrementDecode(){
        return decode.getAndIncrement();
    }
    public static Long getSending(){
        return sending.get();
    }
    public static Long getAndIncrementSending(){
        return sending.getAndIncrement();
    }
    public static Long getAndIncrement(){
        return incomming.getAndIncrement();
    }
    public static Long getComming(){
        return incomming.get();
    }
}
