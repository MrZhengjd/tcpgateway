package com.game.domain.flow.component;


import com.game.domain.flow.model.Flow;
import com.game.domain.flow.model.Request;
import com.game.domain.flow.model.Response;

public interface FlowParser {
    Response parseFlow(Flow flow, Request request);
}
