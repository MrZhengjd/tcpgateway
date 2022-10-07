package com.game.domain.flow.handler;

import com.game.domain.flow.model.GameFlow;
import com.game.domain.flow.model.Response;
import com.game.domain.model.*;
/**
 * @author zheng
 */
public interface FlowHandler {
    Response handlePlayerRequest(PlayerRequest playerRequest, GameFlow gameFlow);
}
