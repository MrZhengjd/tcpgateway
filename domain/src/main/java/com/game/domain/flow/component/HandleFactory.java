package com.game.domain.flow.component;

import java.util.HashMap;
import java.util.Map;

public class HandleFactory {
    private static Map<String , HandleRequest> handleRequestMap = new HashMap<>();
    static {
        handleRequestMap.put("order",new OrderHandler());
    }
    public static HandleFactory getInstance(){
        return HandleFactoryHolder.INSTANCE;
    }
    private HandleFactory(){}
    private static class HandleFactoryHolder{
        private static HandleFactory INSTANCE = new HandleFactory();
    }
    public HandleRequest getRequestByType(String type){
        return handleRequestMap.get(type);
    }

}
