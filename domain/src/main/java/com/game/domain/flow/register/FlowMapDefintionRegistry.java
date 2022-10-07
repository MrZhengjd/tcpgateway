package com.game.domain.flow.register;

import com.game.domain.flow.model.FlowMap;

import java.util.Map;

/**
 * @author zheng
 */
public interface FlowMapDefintionRegistry {
    Map<String, FlowMap> registry()throws Exception;
}
