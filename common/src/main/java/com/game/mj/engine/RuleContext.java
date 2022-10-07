package com.game.mj.engine;

import java.util.HashMap;
import java.util.Map;

public class RuleContext {
    private  Map<Long,RuleNode> context = new HashMap<>();

    public  RuleNode getNodeByNodeId(Long nodeId){
        return context.get(nodeId);
    }
    public  void generateRuleMap(TreeMapVo treeMapVo){
        for(Map.Entry<Long,RuleNodeComposite> entry : treeMapVo.getRuleNodeMap().entrySet()){
//            System.out.println("key "+entry.getKey() + " value "+entry.getValue().getDesc());
            if(entry.getValue() != null){
                context.put(entry.getKey(),entry.getValue());
            }

        }

        for(Map.Entry<Long,Leaf> entry : treeMapVo.getResultMap().entrySet()){
//            System.out.println("key "+entry.getKey() + " value "+entry.getValue().getDesc());
            if(entry.getValue() != null){
                context.put(entry.getKey(),entry.getValue());
            }

        }
    }
}
