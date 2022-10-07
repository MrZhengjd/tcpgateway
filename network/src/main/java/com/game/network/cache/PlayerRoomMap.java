package com.game.network.cache;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author zheng
 */
@Service
public class PlayerRoomMap {
    //玩家id对应房间id 这里不绑定房间对象
    private Map<Long, Integer> playerRoomMap = new HashMap<>();
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

}
