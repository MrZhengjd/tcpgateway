package com.game.domain.flow.component;

import com.game.domain.flow.model.FastContextHolder;
import com.game.domain.flow.model.Node;
import com.game.domain.flow.model.Response;

/**
 * @author zheng
 */
public class EventExecNode implements ExecNode {
    @Override
    public Response executeNode(Node node) {
        PlayerContext runtimeContext = FastContextHolder.getRuntimeContext();
//        EventAnnotationManager.getInstance().sendPlayerEvent(node.getComponent(),node,  runtimeContext.getPlayerRequest());
        return runtimeContext.getResponse();
    }
}
