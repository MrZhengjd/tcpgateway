package com.game.domain.flow.register;

import com.alibaba.fastjson.JSONObject;
import com.game.domain.flow.model.Flow;
import com.game.domain.flow.model.TempNode;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class YmlFlowDefintionRegistry implements FlowDefintionRegistry {
    private final String CLASS_PATH_FLOW = "flow/*.flow.yml";

    @Override
    public Map<String, Flow> registry() throws Exception {
        return registryModel();
    }

    private Map<String, Flow> registryModel() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(CLASS_PATH_FLOW);
        Map<String, Flow> flowDefinationMap = new HashMap<String, Flow>();
        Arrays.stream(resources).forEach(resource -> {
            YamlMapFactoryBean yamlMapFactoryBean = new YamlMapFactoryBean();
            yamlMapFactoryBean.setResources(resources);
            yamlMapFactoryBean.afterPropertiesSet();
            Map<String,Object> objectMap = yamlMapFactoryBean.getObject();

            YmlFlow flow = JSONObject.parseObject(JSONObject.toJSONString(objectMap), YmlFlow.class);
            flowDefinationMap.put(flow.getId(),buildFlow(flow));
        });
        return flowDefinationMap;
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
