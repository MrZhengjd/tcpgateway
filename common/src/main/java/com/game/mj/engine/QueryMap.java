package com.game.mj.engine;

import java.util.HashMap;
import java.util.Map;

public class QueryMap {
    private Long treeId;
    private Long startNodeId;

    public void setTreeId(Long treeId) {
        this.treeId = treeId;
    }

    public void setStartNodeId(Long startNodeId) {
        this.startNodeId = startNodeId;
    }

    public void setDecesionMap(Map<String, Object> decesionMap) {
        this.decesionMap = decesionMap;
    }

    public Long getTreeId() {
        return treeId;
    }

    public Long getStartNodeId() {
        return startNodeId;
    }

    private Map<String ,Object> decesionMap = new HashMap<>();



    public Map<String, Object> getDecesionMap() {
        return decesionMap;
    }
}
