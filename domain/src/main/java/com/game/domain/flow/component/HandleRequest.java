package com.game.domain.flow.component;


import com.game.domain.flow.model.Request;
import com.game.domain.flow.model.Response;

public interface HandleRequest {
    Response handleRequest(Request request);
}
