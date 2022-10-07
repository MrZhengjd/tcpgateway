package com.game.domain.statemachine.composite;

/**
 * @author zheng
 */
public enum IStatus {
    PERMIT_SUBMIT("permitSubmit","提交请假单"),

    PERMIT("leaderPermitting","领导审批中"),
    PERMIT_AGREE("leaderAgree","领导同意"),
    PERMIT_DISAGREE("leaderDisAgree","领导不同意"),
    LEADER_PERMIT_MODIFY("leaderPermitModify","领导觉得需要修改"),
    PERMIT_SUCCESS("permitSuccess","请假成功"),
    PERMIT_FAIL("permitFail","请假失败");

    IStatus(String status, String memo) {
        this.status = status;
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    private String status;
    private String memo;
}
