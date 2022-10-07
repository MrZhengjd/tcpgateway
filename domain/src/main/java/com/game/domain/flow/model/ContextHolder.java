package com.game.domain.flow.model;

public class ContextHolder {
    private static final ThreadLocal<Context> CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    public static Context getRuntimeContext(){
        Context context = CONTEXT_THREAD_LOCAL.get();
        if (context == null){
            setCurrentContext(new Context());
        }
        return CONTEXT_THREAD_LOCAL.get();
    }

    public static void setCurrentContext(Context context) {
        CONTEXT_THREAD_LOCAL.set(context);
    }
    public static void clear(){
        CONTEXT_THREAD_LOCAL.set(null);
        CONTEXT_THREAD_LOCAL.remove();
    }
}
