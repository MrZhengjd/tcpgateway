package com.game.domain.statemachine;

/**
 * @author zheng
 */
public enum LeavePermitType {
    ANNUAL_LEAVE("annual_leave","年假"),
    CASUAL_LEAVE("casual_leave","事假"),
    MEDICAL_LEAVE("medical_leave","病假"),
    MARRIAGE_LEAVE("marriage_leave","婚假");

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    LeavePermitType(String type, String memo) {
        this.type = type;
        this.memo = memo;
    }

    private String type;
    private String memo;
}
