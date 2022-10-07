package com.game.consumemodel.model;

import com.game.domain.relation.role.PlayerRole;
import com.game.domain.relation.room.RoomManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zheng
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerRoomContext {
    private PlayerRole playerRole;
    private RoomManager roomManager;
}
