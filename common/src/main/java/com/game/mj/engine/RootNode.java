package com.game.mj.engine;

public class RootNode {
    private Long treeId;
    private Long nodeId;
    private String name;
    private SelectorEnum selectorEnum;

    public SelectorEnum getSelectorEnum() {
        return selectorEnum;
    }

    public void setSelectorEnum(SelectorEnum selectorEnum) {
        this.selectorEnum = selectorEnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }
}
