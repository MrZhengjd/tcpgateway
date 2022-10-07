package com.game.mj.generator;

public interface IdGenerator {

    Long generateId();
    Long generateIdFromServerId(Integer serverId);
}
