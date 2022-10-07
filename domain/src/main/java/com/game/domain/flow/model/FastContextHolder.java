package com.game.domain.flow.model;

import com.game.mj.util.TimeUtil;
import com.game.domain.flow.component.PlayerContext;
import io.netty.util.concurrent.FastThreadLocal;

public class FastContextHolder {
    private static final FastThreadLocal<PlayerContext> CONTEXT_THREAD_LOCAL = new FastThreadLocal<>();
    private static final FastThreadLocal<TimeUtil> TIME_UTIL_FAST_THREAD_LOCAL = new FastThreadLocal<>();
//    private static ReadWriteLockOperate readWriteLockOperate = new ReadWriteLockOperate();
    public static TimeUtil getTimeUtilContext(){

        TimeUtil timeUtil = TIME_UTIL_FAST_THREAD_LOCAL.get();
        if (timeUtil == null){
//            System.out.println("here is initial--------");
            timeUtil = new TimeUtil();
            setTimeUtilFastThreadLocal(timeUtil);
        }
        return timeUtil;
    }

    public static void setTimeUtilFastThreadLocal(TimeUtil timeUtil){

        TIME_UTIL_FAST_THREAD_LOCAL.set(timeUtil);
    }
    public static PlayerContext getRuntimeContext(){
        PlayerContext context = CONTEXT_THREAD_LOCAL.get();
        if (context == null){
            setCurrentContext(new PlayerContext());
        }

        return CONTEXT_THREAD_LOCAL.get();
    }
    public static void setSuccessResult(Object result){
        getRuntimeContext().getResponse().setStatus_code(200);
        getRuntimeContext().getResponse().setResult(result);
    }
    public static void setResponseResult(Object reuslt){
        getRuntimeContext().getResponse().setResult(reuslt);
    }

    /**
     * 设置结果集
     * @param result
     */
    public static void setResponse(Object result){
        PlayerContext context = getRuntimeContext();
        context.getResponse().setResult(result);
    }
    public static void setCurrentContext(PlayerContext context) {
        CONTEXT_THREAD_LOCAL.set(context);
    }
    public static void clear(){
        CONTEXT_THREAD_LOCAL.set(null);
        CONTEXT_THREAD_LOCAL.remove();
    }
}
