package com.game.mj.engine;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TreeMapVo implements Serializable {
    private static final long serialVersionUID = 9020456772767226820L;
    private RootNode rootNode;
    private Map<Long,RuleNodeComposite> ruleNodeMap;
    private Map<Long,Leaf> resultMap =new HashMap<>();

    public Map<Long, Leaf> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<Long, Leaf> resultMap) {
        this.resultMap = resultMap;
    }

    public RootNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(RootNode rootNode) {
        this.rootNode = rootNode;
    }

    public Map<Long, RuleNodeComposite> getRuleNodeMap() {
        return ruleNodeMap;
    }

    public void setRuleNodeMap(Map<Long, RuleNodeComposite> ruleNodeMap) {
        this.ruleNodeMap = ruleNodeMap;
    }
}
