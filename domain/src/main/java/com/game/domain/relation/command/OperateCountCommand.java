package com.game.domain.relation.command;


import com.game.domain.relation.organ.OperateCountOrgan;

/**
 * @author zheng
 */
public class OperateCountCommand implements Command<OperateCountOrgan,Integer> {


    @Override
    public void execute(OperateCountOrgan organ, Integer data) {
        organ.setOperateCount(data);
    }
}
