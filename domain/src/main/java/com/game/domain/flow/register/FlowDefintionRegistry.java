package com.game.domain.flow.register;



import com.game.domain.flow.model.Flow;

import java.util.Map;

public interface FlowDefintionRegistry {
    Map<String, Flow> registry()throws Exception;
}
