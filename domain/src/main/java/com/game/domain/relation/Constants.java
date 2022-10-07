package com.game.domain.relation;

/**
 * @author zheng
 */
public class Constants {
    public static final String OUTTER_ORGAN = "outterOrgan"; //果实
    public static final String OUTTED_ORGAN = "outtedOrgan"; //果实
    public static final String INNER_ORGAN = "innerOrgan"; //果实
    public static final String HAND_ORGAN = "handOrgan"; //果实
    public static final String PLAYER_ROOM = "playerRoom";
    public static final String ROOM_MAP = "roomMap";
    public static final String PLAYER_SERVER = "playerServer";

    public static final Integer SERVER_ID = 1;
    public static final int PARALLEL = Math.max(2, Runtime.getRuntime().availableProcessors()) * 2;
    public final static int HEAD_START = 0xabef0201;
    public final static int END_TAIL = 0xabef0221;
}
