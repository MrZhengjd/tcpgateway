package com.game.mj.engine;

import java.util.HashMap;
import java.util.Map;

public class TreeContext {
    private static Map<Long,RuleContext> contextMap = new HashMap<>();
    private static Map<Long,TreeMapVo> treeMapVoMap = new HashMap<>();
    private TreeContext(){}
    private static class ContextHolder{
       public static TreeContext context = new TreeContext();
    }
    public static TreeContext getInstance(){
        return ContextHolder.context;
    }
    public void addData(TreeMapVo treeMapVo){
        RuleContext context = new RuleContext();
        if(treeMapVoMap.containsKey(treeMapVo.getRootNode().getTreeId())){
            return;
        }
        context.generateRuleMap(treeMapVo);
        contextMap.put(treeMapVo.getRootNode().getTreeId(),context);
        treeMapVoMap.put(treeMapVo.getRootNode().getTreeId(),treeMapVo);
    }

    public static TreeMapVo getTreeMapVoById(Long treeId){
        return treeMapVoMap.get(treeId);
    }
    public static RuleContext getContextById(Long treeId){
        return contextMap.get(treeId);
    }
}
