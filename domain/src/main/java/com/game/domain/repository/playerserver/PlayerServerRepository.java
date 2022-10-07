package com.game.domain.repository.playerserver;

/**
 * @author zheng
 */
public interface PlayerServerRepository {
    void savePlayerServer(String key,Long playerId,Integer serverId);
}
