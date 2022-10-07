package com.game.mj;

/**
 * @author zheng
 */
public enum Type {
    WANG(1),
    TONG(2),
    TIAO(3),
    FENG(4),
    HUA(5);
    public int index;

    Type(int index) {
        this.index = index;
    }

    public static Type checkType(int index){
        int tmp = index / 10;
        switch (tmp){
            case 1 :return WANG;
            case 2:return TONG;
            case 3:return TIAO;
            case 4:return FENG;
            case 5:return HUA;
            default:
                return null;
        }
    }
}
