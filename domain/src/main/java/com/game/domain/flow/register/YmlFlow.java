package com.game.domain.flow.register;



import com.game.domain.flow.model.TempNode;

import java.io.Serializable;
import java.util.List;

public class YmlFlow implements Serializable {
    private String id;
    private String desc;
    private String name;
    private String input;
    private String output;

    private String startNode;
    private List<TempNode> nodes;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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




    public String getStartNode() {
        return startNode;
    }

    public void setStartNode(String startNode) {
        this.startNode = startNode;
    }

    public List<TempNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<TempNode> nodes) {
        this.nodes = nodes;
    }
}
