package com.game.ai;

import java.util.HashSet;
import java.util.List;

/**
 * @author zheng
 */
public class MaJiangDef {
    private static final String[] feng = new String[]
            {"东", "南", "西", "北", "中", "发", "白",};
    private static final String[] hua = new String[]{"春", "夏", "秋", "冬", "梅", "兰", "竹", "菊"};
    public static final int WAN1 = 11;
    public static final int WAN2 = 12;
    public static final int WAN3 = 13;
    public static final int WAN4 = 14;
    public static final int WAN5 = 15;
    public static final int WAN6 = 16;
    public static final int WAN7 = 17;
    public static final int WAN8 = 18;
    public static final int WAN9 = 19;

    public static final int TONG1 = 21;
    public static final int TONG2 = 22;
    public static final int TONG3 = 23;
    public static final int TONG4 = 24;
    public static final int TONG5 = 25;
    public static final int TONG6 = 26;
    public static final int TONG7 = 27;
    public static final int TONG8 = 28;
    public static final int TONG9 = 29;

    public static final int TIAO1 = 31;
    public static final int TIAO2 = 32;
    public static final int TIAO3 = 33;
    public static final int TIAO4 = 34;
    public static final int TIAO5 = 35;
    public static final int TIAO6 = 36;
    public static final int TIAO7 = 37;
    public static final int TIAO8 = 38;
    public static final int TIAO9 = 39;

    public static final int FENG_DONG = 41;
    public static final int FENG_NAN = 42;
    public static final int FENG_XI = 43;
    public static final int FENG_BEI = 44;

    public static final int JIAN_ZHONG = 45;
    public static final int JIAN_FA = 46;
    public static final int JIAN_BAI = 47;

    public static final int HUA_CHUN = 51;
    public static final int HUA_XIA = 52;
    public static final int HUA_QIU = 53;
    public static final int HUA_DONG = 54;
    public static final int HUA_MEI = 55;
    public static final int HUA_LAN = 56;
    public static final int HUA_ZHU = 57;
    public static final int HUA_JU = 58;

    public static final int MAX_NUM = 42;

    public static final int TYPE_WAN = 1;
    public static final int TYPE_TONG = 2;
    public static final int TYPE_TIAO = 3;
    public static final int TYPE_FENG = 4;
    //    public static final int TYPE_JIAN = 4;
    public static final int TYPE_HUA = 5;

    public static int toCard(int type, int index) {
        switch (type) {
            case TYPE_WAN:
                return TYPE_WAN * 10 + index;
            case TYPE_TONG:
                return TYPE_TONG * 10 + index;
            case TYPE_TIAO:
                return TYPE_TIAO * 10 + index;
            case TYPE_FENG:
                return TYPE_FENG * 10 + index;
            case TYPE_HUA:
                return TYPE_HUA * 10 + index;
        }
        return 0;
    }

    public static String cardsToString(List<Integer> card) {
        String ret = "";
        for (int c : card) {
            ret += cardToString(c) + ",";
        }
        return ret;
    }

    public static String cardsToString(HashSet<Integer> card) {
        String ret = "";
        for (int c : card) {
            ret += cardToString(c) + ",";
        }
        return ret;
    }

    public static String cardToString(int card) {
        int start = card / 10;
        int temp = card % 10;

        switch (start) {
            case TYPE_WAN:
                return temp + "万";
            case TYPE_TONG:
                return temp + "筒";
            case TYPE_TIAO:
                return temp + "条";
            case TYPE_FENG:
                return feng[temp-1];
            case TYPE_HUA:
                return hua[temp-1];
        }

        return "错误" + card;
    }

    public static int type(int card) {
        int tmp = card / 10;
        switch (tmp) {
            case TYPE_WAN:
                return TYPE_WAN;
            case TYPE_TONG:
                return TYPE_TONG;
            case TYPE_TIAO:
                return TYPE_TIAO;
            case TYPE_FENG:
                return TYPE_FENG;
            case TYPE_HUA:
                return TYPE_HUA;
            default:
                return 0;
        }

    }
}
