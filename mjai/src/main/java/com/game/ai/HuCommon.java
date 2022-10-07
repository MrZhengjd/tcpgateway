package com.game.ai;

import java.util.HashSet;
import java.util.Iterator;

/**
 * @author zheng
 */
public class HuCommon {
    public static int N = 9;
    /**
     * 编码介绍
     * 456->111
     * 234->111
     * 222->3
     * 33->2
     * 数字不连续用0间隔
     * 234456->11211
     */


    public static void main(String[] args) {
        HashSet<Long> card = new HashSet<>();

        for (int i = 0; i <= 14; i++)
        {
            int[] num = new int[N];
            gen_card(card, num, 0, i);
        }
        int size = card.size();
        Iterator<Long> iterator = card.iterator();
        while (iterator.hasNext()){
            Long next = iterator.next();
            System.out.println("tem "+next);
        }
        System.out.println("-------");
        System.out.println("size "+size);
    }
    private static void gen_card(HashSet<Long> card, int num[], int index, int total)
    {
        if (index == N - 1)
        {
            if (total > 4)
            {
                return;
            }
            num[index] = total;

            long ret = 0;
            for (int c : num)
            {
                ret = ret * 10 + c;
            }
            card.add(ret);
            return;
        }
        for (int i = 0; i <= 4; i++)
        {
            if (i <= total)
            {
                num[index] = i;
            }
            else
            {
                num[index] = 0;
            }
            gen_card(card, num, index + 1, total - num[index]);
        }
    }
}
