package com.game.domain.flow.component;


import com.game.domain.flow.model.Node;
import com.game.domain.flow.model.Response;

public interface ExecNode {
    Response executeNode(Node node);

}
