package com.game.domain.statemachine;

/**
 * @author zheng
 */
public enum Status {
    PERMIT_SUBMIT("permitSubmit","提交请假单"),

    LEADER_PERMIT("leaderPermitting","领导审批中"),
    LEADER_PERMIT_AGREE("leaderAgree","领导同意"),
    LEADER_PERMIT_DISAGREE("leaderDisAgree","领导不同意"),
    LEADER_PERMIT_MODIFY("leaderPermitModify","领导觉得需要修改"),

    HR_PERMIT("hrPermitting","hr审批中"),
    HR_PERMIT_AGREE("hrAgree","hr同意"),
    HR_PERMIT_DISAGREE("hrDisAgree","hr不同意"),
    HR_PERMIT_MODIFY("hrPermitModify","hr觉得需要修改"),

    CEO_PERMIT("ceoPermitting","ceo审批中"),
    CEO_PERMIT_AGREE("ceoAgree","ceo同意"),
    CEO_PERMIT_DISAGREE("ceoDisAgree","ceo不同意"),
    CEO_PERMIT_MODIFY("ceoPermitModify","ceo觉得需要修改"),
    PERMIT_SUCCESS("permitSuccess","请假成功"),
    PERMIT_FAIL("permitFail","请假失败");
    Status(String status, String memo) {
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
