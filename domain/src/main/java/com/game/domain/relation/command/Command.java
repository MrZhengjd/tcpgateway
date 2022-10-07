package com.game.domain.relation.command;


import com.game.domain.relation.organ.Organ;

/**
 * @author zheng
 */
public interface Command<T extends Organ,V> {
    void execute(T organ, V data);
}
