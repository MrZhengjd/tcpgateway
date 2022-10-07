package com.game.index.gentable;

import java.util.HashMap;

/**
 * @author zheng
 */
public class GenTable {
    private static HashMap<Integer, Boolean>[] gui_tested = new HashMap[9];
    private static HashMap<Integer, Boolean>[] gui_eye_tested = new HashMap[9];

    void init_cache() {
        for (int i = 0; i < 9; i++) {
            gui_tested[i] = new HashMap<>();
            gui_eye_tested[i] = new HashMap<>();
        }
    }

    void genFengTable() {
        int[] cards = new int[34];
        for (int i = 0; i < 34; i++) {
            cards[i] = 0;
        }
        genFengAutoTableSub(cards, 1, false);
    }

    private void genFengAutoTableSub(int[] cards, int level, boolean eye) {
        //生成的是风牌的信息
        for (int i = 0; i < 7; i++) {
            if (cards[i] > 3) {
                continue;
            }
            cards[i] += 3;
            parseFengTable(cards, eye);
            if (level < 4) {
                genFengAutoTableSub(cards, level + 1, eye);
            }
            cards[i] -= 3;
        }
    }

    private void parseFengTable(int[] cards, boolean eye) {
        if (!checkFengAdd(cards, 0, eye)) {
            return;
        }
        parseFengTableSub(cards, 1, eye);
    }

    private void parseFengTableSub(int[] cards, int num, boolean eye) {
        //顺子开头只能是7开头
        for (int i = 0; i < 7; i++) {
            if (cards[i] == 0) {
                continue;
            }
            cards[i]--;
            if (!checkFengAdd(cards, num, eye)) {
                cards[i]++;
                continue;
            }
            if (num < 8) {
                parseFengTableSub(cards, num + 1, eye);
            }
            cards[i]++;
        }
    }

    private boolean checkFengAdd(int[] cards, int guiNum, boolean eye) {
        int key = 0;
        for (int i = 0; i < 7; i++) {
            key = key * 10 + cards[i];
        }
        if (key == 0) {
            return false;
        }
        HashMap<Integer, Boolean> map;
        if (!eye) {
            map = gui_tested[guiNum];
        } else {
            map = gui_eye_tested[guiNum];
        }
        if (map.containsKey(key)) {
            return false;
        }
        map.put(key, true);
        for (int i = 0; i < 7; i++) {
            if (cards[i] > 4) {
                return true;
            }
        }
        TableMgr.getInstance().add(key, guiNum, eye, false);
        return true;
    }

    void genFengEyeTable() {
        int[] cards = new int[34];
        for (int i = 0; i < 34; ++i) {
            cards[i] = 0;
        }
        for (int i = 0; i < 7; ++i) {
            cards[i] = 2;
            parseFengTable(cards, true);
            genFengAutoTableSub(cards, 1, true);
            cards[i] = 0;
        }
    }

    public static void main(String[] args) {
        GenTable table = new GenTable();
        table.init_cache();
        table.genFengTable();
        table.genFengEyeTable();
        TableMgr.getInstance().dump_feng_table();
    }

    public static void test1() {
        System.out.println("测试两种花色\n");
        int[] cards = {0, 0, 0, 1, 1, 1, 0, 2, 0, /* 桶 */ 0, 0, 0, 0, 0, 0, 0, 0, 0,
                /* 条 */ 0, 0, 0, 0, 0, 0, 0, 0, 0,/* 字 */ 0, 0, 0, 0, 0, 0, 0};

        gen_auto_table_sub(cards, 1);
        // for (int i = 0; i < 18; ++i) {
        // cards[i] = 2;
        // System.out.print("将 ");
        // System.out.println(i);
        // gen_auto_table_sub(cards, 1);
        // cards[i] = 0;
        // }
    }

    static void gen_auto_table_sub(int[] cards, int level) {
        for (int i = 0; i < 32; ++i) {
            int index = -1;
            if (i <= 17) {
                cards[i] += 3;
            } else if (i <= 24) {
                index = i - 18;
            } else {
                index = i - 16;
            }

            if (index >= 0) {
                cards[index] += 1;
                cards[index + 1] += 1;
                cards[index + 2] += 1;
            }

            if (level == 4) {
//                check_hu(cards, 18);
            } else {
                gen_auto_table_sub(cards, level + 1);
            }

            if (i <= 17) {
                cards[i] -= 3;
            } else {
                cards[index] -= 1;
                cards[index + 1] -= 1;
                cards[index + 2] -= 1;
            }
        }
    }
}
