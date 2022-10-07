package com.game.domain.flow.component;



import java.util.HashMap;
import java.util.Map;

public class ExecNodeFactory {
    private static Map<String , ExecNode> execNodeMap = new HashMap<>();

    private ExecNodeFactory(){}
    static {
        execNodeMap.put("method",new MethodExecNode());
        execNodeMap.put("event",new EventExecNode());
    }
    public static ExecNodeFactory getInstance(){

        return NodeHolder.INSTANCE;

//        return factory;
    }
    private static class NodeHolder{
        public static ExecNodeFactory INSTANCE = new ExecNodeFactory();



    }
    public ExecNode getNodeParserByType(String type){
        return execNodeMap.get(type);
    }
}
