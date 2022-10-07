package com.game.domain.relation.organ;


/**
 * @author zheng
 */
public class OperateCountOrgan implements Organ {
    private int operateCount;

    public int getOperateCount() {
        return operateCount;
    }

    public void setOperateCount(int operateCount) {
        this.operateCount = operateCount;
    }

    @Override
    public void reset() {
        operateCount = 0;
    }

    @Override
    public String toString() {
        return "operateCount "+operateCount;
    }
}
