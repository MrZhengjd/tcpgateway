package com.game.domain.flow.handler;

import com.game.domain.model.*;
import com.game.domain.flow.component.ExecNode;
import com.game.domain.flow.component.ExecNodeFactory;
import com.game.domain.flow.component.PlayerContext;
import com.game.domain.flow.model.FastContextHolder;
import com.game.domain.flow.model.GameFlow;
import com.game.domain.flow.model.Node;
import com.game.domain.flow.model.Response;

/**
 * @author zheng
 */
public class PlayerRequestHandler implements FlowHandler {


    @Override
    public Response handlePlayerRequest(PlayerRequest playerRequest, GameFlow gameFlow) {

//        Request request = ClassUtil.newInstance(flow.getInput(),Request.class);

        Response response = new Response();
        PlayerContext context = new PlayerContext();
        context.setPlayerRequest(playerRequest);
        context.setResponse(response);
        FastContextHolder.setCurrentContext(context);
        ExecNode parser ;
        for (Node node : gameFlow.getNodeList()){
            parser = ExecNodeFactory.getInstance().getNodeParserByType(node.getType());
            FastContextHolder.getRuntimeContext().setResponse(parser.executeNode(node));
        }
//        Node startNode = FlowMapDefinitonFactory.getInstance().getNodeMap().get(gameFlow.getStart());
//        FlowHander handed = new FlowHander();
//        return handed.handleEvent(startNode);
        return FastContextHolder.getRuntimeContext().getResponse();

    }
}
