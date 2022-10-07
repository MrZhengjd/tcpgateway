package com.game.domain.flow.register;



import com.game.domain.flow.model.Flow;
import com.game.domain.flow.model.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowDefinitonFactory {
    private static Map<String, Flow> registryMap = new HashMap<>();
    private static Map<String, Node> nodeMap = new HashMap<>();

    public  Map<String, Node> getNodeMap() {
        return nodeMap;
    }

    private static List<FlowDefintionRegistry> registries = new ArrayList<>();
    static {
        registries.add(new YmlFlowDefintionRegistry());
        initFactory();

    }

    public static Map<String, Flow> getRegistryMap() {
        return registryMap;
    }
    public Flow getFlowByteId(String  id){
        return registryMap.get(id);
    }

    private FlowDefinitonFactory(){}
    public static FlowDefinitonFactory getInstance(){
        return FlowInstance.INSTANCE;
    }
    private static class FlowInstance{
        private static FlowDefinitonFactory INSTANCE = new FlowDefinitonFactory();
    }

    public static void initFactory(){
        registries.forEach(register->{
            try {
                registryMap.putAll(register.registry());
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}
