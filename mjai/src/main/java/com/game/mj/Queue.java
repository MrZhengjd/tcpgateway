package com.game.mj;

/**
 * @author zheng
 */
public class Queue {
    int max = 8;
    int[] array = new int[max];
    static int count = 0;
    static int judgeCount = 0;

    private void check(int cn){
        if (cn == max){
            return;
        }
        for (int i = 0;i<max;i++){
            array[cn] = i;
            if (judge(cn)){
                check(cn+1);
            }
        }
    }

    private boolean judge(int cn) {
        judgeCount++;
        for (int i = 0;i<cn;i++){
            if (array[i] ==array[cn] || Math.abs(cn -1) == Math.abs(array[cn]-array[i])){
                return false;
            }
        }
        return true;
    }
}
