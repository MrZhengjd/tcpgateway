package com.game.domain.flow.register;

import com.alibaba.fastjson.JSONObject;
import com.game.domain.flow.model.*;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.*;

/**
 * @author zheng
 */
public class FlowMapDefinetionRegistry implements FlowMapDefintionRegistry {


    private final String CLASS_PATH_FLOW = "flow/*.flowmap.yml";
    @Override
    public Map<String, FlowMap> registry() throws Exception {
        return registryModel();
    }
    private Map<String, FlowMap> registryModel() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(CLASS_PATH_FLOW);
        Map<String, FlowMap> flowDefinationMap = new HashMap<String, FlowMap>();
        Arrays.stream(resources).forEach(resource -> {
            YamlMapFactoryBean yamlMapFactoryBean = new YamlMapFactoryBean();
            yamlMapFactoryBean.setResources(resources);
            yamlMapFactoryBean.afterPropertiesSet();
            Map<String,Object> objectMap = yamlMapFactoryBean.getObject();

            YmlFlowMap flow = JSONObject.parseObject(JSONObject.toJSONString(objectMap),YmlFlowMap.class);
            flowDefinationMap.put(flow.getId(),buildFromYmlMap(flow));
        });
        return flowDefinationMap;
    }
    private FlowMap buildFromYmlMap(YmlFlowMap ymlFlowMap){
        FlowMap flowMap = new FlowMap();
        flowMap.setId(ymlFlowMap.getId());
        flowMap.setName(ymlFlowMap.getName());
//        Map<String ,List<Node>> nodeMap = new HashMap<>();

        for (FlowNode flow:ymlFlowMap.getMapFlows() ){
            GameFlow gameFlow = new GameFlow();
            List<Node> nodeList = new ArrayList<>();
            for (TempNode node : flow.getNodes()){
                if (!FlowMapDefinitonFactory.getInstance().getNodeMap().containsKey(node.getNode().getId())){
                    FlowMapDefinitonFactory.getInstance().getNodeMap().put(node.getNode().getId(),node.getNode());
                }
                nodeList.add(node.getNode());
            }
//            nodeMap.put(flow.getName(),nodeList);
            gameFlow.setNodeList(nodeList);
            gameFlow.setName(flow.getName());
            gameFlow.setStart(flow.getStart());
            flowMap.getFlowMap().put(flow.getServiceId(),gameFlow);
        }



        return flowMap;
    }
    private Flow buildFlow(YmlFlow flow) {
        Flow result = new Flow();
        result.setId(flow.getId());
        result.setName(flow.getName());
        result.setInput(flow.getInput());
        result.setOutput(flow.getOutput());
        for (TempNode node : flow.getNodes()){
            if (!FlowDefinitonFactory.getInstance().getNodeMap().containsKey(node.getNode().getId())){
                FlowDefinitonFactory.getInstance().getNodeMap().put(node.getNode().getId(),node.getNode());
            }
        }
        result.setStartNodeId(flow.getStartNode());
        return result;
    }
}
