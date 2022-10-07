package com.game.mj.bhengine;

/**
 * @author zheng
 */
public enum EngineEnum {
    //==
    EQUAL(1),
    //>
    GT(2),
    //<
    LT(3),
    //>=
    GE(4),
    //<=
    LE(5),
    ENUM(6),
    LEG(7),SEG(8)
    ;
    //    public static final int EQUAL = 1;    //等于
//    public static final int GT = 2;       //大于
//    public static final int LT = 3;       //小于
//    public static final int GE = 4;       //大于等于
//    public static final int LE = 5;       //小于等于
//    public static final int ENUM = 6;     //枚举
//    public static final int LEG = 7;     // 小于 等于 大于
//    public static final int SEG = 8;     //区间运算

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    EngineEnum(int value) {
        this.value = value;
    }
}
