package com.game.domain.relation.room;

import com.game.mj.eventdispatch.Event;
import com.game.domain.relation.role.PlayerRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zheng
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JoinRoomEvent implements Event {
    private PlayerRole playerRole;
}
