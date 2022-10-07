package com.game.domain.flow.register;

import com.game.domain.flow.model.FlowNode;

import java.io.Serializable;
import java.util.List;

/**
 * @author zheng
 */
public class YmlFlowMap implements Serializable {
    private String id;

    private String name;
    private List<FlowNode> mapFlows;

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

    public List<FlowNode> getMapFlows() {
        return mapFlows;
    }

    public void setMapFlows(List<FlowNode> mapFlows) {
        this.mapFlows = mapFlows;
    }
}
