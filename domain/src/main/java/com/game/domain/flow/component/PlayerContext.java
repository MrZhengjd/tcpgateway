package com.game.domain.flow.component;

import com.game.domain.flow.model.Response;
import com.game.domain.model.*;
/**
 * @author zheng
 */
public class PlayerContext {
    private PlayerRequest playerRequest;
    private Response response;

    public PlayerRequest getPlayerRequest() {
        return playerRequest;
    }

    public void setPlayerRequest(PlayerRequest playerRequest) {
        this.playerRequest = playerRequest;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
