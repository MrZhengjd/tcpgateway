package com.game.mj.constant;

/**
 * @author zheng
 */
public interface RequestMessageType {
    public static final int ONLINE_MESSAGE = 1000000;
    public static final int OFFLINE_MESSAGE = 1000001;
    public static final int BACKTOROOM_MESSAGE = 1000002;
    public static final int CHUPAI_MESSAGE = 1000003;
    public static final int CHANGE_CORDER = 1000051;
    public static final int ASK_MASTER = 1000052;
    public static final int COPY_DATA = 1000053;
    public static final int ASK_COPY_ID = 1000054;
    public static final int BAND_SERVICE = 1000055;
    public static final int AUTH_REQUEST = 1000056;
    public static final int ASK_MASTER_CHANGE = 1000057;
    public static final int ASK_NEXT_OPERATEID = 1000058;
    public static boolean checkIsCopyOrAuth(Integer serviceId){
        return serviceId == ASK_COPY_ID || serviceId == AUTH_REQUEST;
    }
}
