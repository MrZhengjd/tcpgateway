package com.game.domain.cache;

import com.game.domain.relation.vo.RoomServerVo;

/**
 * @author zheng
 */
public interface PlayerRoomService {
    RoomServerVo getByPlayerId(Long playerId);

    void putInfo(Long playerId, RoomServerVo roomServerVo);
}
