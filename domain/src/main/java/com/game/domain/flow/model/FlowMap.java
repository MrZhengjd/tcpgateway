package com.game.domain.flow.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zheng
 */
public class FlowMap {
    private String  id;
    private String name;
    private Map<Integer , GameFlow> flowMap = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Integer, GameFlow> getFlowMap() {
        return flowMap;
    }

    public void setFlowMap(Map<Integer, GameFlow> flowMap) {
        this.flowMap = flowMap;
    }
}
