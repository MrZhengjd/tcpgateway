package com.game.domain.flow.component;


import com.game.domain.flow.model.*;
import com.game.domain.flow.register.FlowDefinitonFactory;

public class DefaultFlowParser implements FlowParser {
    @Override
    public Response parseFlow(Flow flow, Request request) {
        Context context = new Context();
//        Request request = ClassUtil.newInstance(flow.getInput(),Request.class);
        Response response = new Response();
        context.setRequest(request);
        context.setResponse(response);
        ContextHolder.setCurrentContext(context);
        Node startNode = FlowDefinitonFactory.getInstance().getNodeMap().get(flow.getStartNodeId());
        FlowHander handed = new FlowHander();
        return handed.handleNodeResult(startNode);


    }
}
