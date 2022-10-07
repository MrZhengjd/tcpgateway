package com.game.domain.flow.model;

import java.io.Serializable;

public class Flow implements Serializable {
    private String  id;
    private String name;
    private String input;
    private String output;
    private String  startNodeId;
//    private Map<String,Node> nodeMap = new HashMap<>();

    public String getStartNodeId() {
        return startNodeId;
    }

    public void setStartNodeId(String startNodeId) {
        this.startNodeId = startNodeId;
    }

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

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

//    public Map<String, Node> getNodeMap() {
//        return nodeMap;
//    }
//
//    public void setNodeMap(Map<String, Node> nodeMap) {
//        this.nodeMap = nodeMap;
//    }
}
