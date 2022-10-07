package com.game.mj.eventdispatch;

import java.lang.reflect.Method;

/**
 * @author zheng
 */
public class ListenerHandler {
    private Object bean;
    private Method method;

    public ListenerHandler(Object bean, Method method) {
        super();
        this.bean = bean;
        this.method = method;
    }

    public Object getBean() {
        return bean;
    }

    public Method getMethod() {
        return method;
    }
}
