package com.game.mj.engine;

public abstract class RuleNode {
    protected Long treeId;
    protected Long nodeId;
    //叶子 1节点2
    protected int nodeType;
    // 0 所有的子项目里面挑选一个执行 1所有的子项目

//    protected boolean special;
    //规则字段
    protected String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getTreeId() {
        return treeId;
    }

    public void setTreeId(Long treeId) {
        this.treeId = treeId;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public int getNodeType() {
        return nodeType;
    }

//    public boolean isSpecial() {
//        return special;
//    }
//
//    public void setSpecial(boolean special) {
//        this.special = special;
//    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

}
