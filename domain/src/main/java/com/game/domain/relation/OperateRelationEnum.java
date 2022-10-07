package com.game.domain.relation;

/**
 * @author zheng
 */
public enum  OperateRelationEnum {
    PENGOPERATION(1013,"peng");

    OperateRelationEnum(Integer operate, String action) {
        this.operate = operate;
        this.action = action;
    }

    private Integer operate;
    private String action;

    public Integer getOperate() {
        return operate;
    }

    public void setOperate(Integer operate) {
        this.operate = operate;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
