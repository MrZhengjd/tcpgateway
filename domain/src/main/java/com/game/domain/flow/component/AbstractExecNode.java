package com.game.domain.flow.component;


import com.game.domain.flow.model.Node;

public abstract class AbstractExecNode implements ExecNode {


   public void start(Node node ){

       executeNode(node);
   }
}
