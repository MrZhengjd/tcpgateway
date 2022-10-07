package com.game.mj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zheng
 */
public class GenerateBaseHuInfo {
    private static Map<Integer,Boolean> dataMap = new HashMap<>();
    /**
     * 列举所有能胡的情况
     * n*111+m*3
     */
    private static void generateWithoutEye( int level,int last){
//        tmp.trim();
        int tmpLevel = level;
        int base = (int) Math.pow(10,level);
        if (last == - 99){
            last = 0;
        }
//        String local = new String(tmp);
        for (int i = 0;i<5;i++){
            int tmpBase = i * base;
            int tmp = tmpBase + last;
            if (dataMap.containsKey(tmp)){
                continue;
            }

            subGenerate(level,tmp);


        }

    }
    private static void subGenerate(int level,int tmp){
        boolean b = checkTmp(tmp);
        if (b){
            dataMap.put(tmp,true);
            System.out.println("here can use "+ tmp);
        }
        if (tmp < 4000000 && level < 8){
            generateWithoutEye(level +1,tmp);
        }
    }
    private static boolean checkTmp(int tmp) {
        int[] data = new int[9];
        int total = sumDig(tmp,data,0);
        if (total == 0){
            return false;
        }
        boolean first = total % 3 == 0 && total <= 12;
        if (!first){
            return false;
        }
        List<Integer> result = new ArrayList<>();
        for (int i = 0;i<9;i++){
            Integer datum = data[i];
            if (datum == null){
                break;
            }
            result.add(datum);
        }
        total = 0;
        List<Integer> tmpResult = new ArrayList<>();
        for (int i = 0;i<result.size();i++){
            total += result.get(i);
            if (result.get(i) == 0){
                first = total % 3 == 0 && total <= 12;
                if (!first){
                    return false;
                }
                total = 0;
                tmpResult.clear();
            }else {
                tmpResult.add(result.get(i));
            }
        }
        return  true;
    }
    private static boolean checkTempResult(List<Integer> tmpResult){
        if (tmpResult == null || tmpResult.isEmpty() || tmpResult.size() < 2){
            return false;
        }
        Integer first = tmpResult.get(0);
        Integer second = tmpResult.get(1);
        if (tmpResult.size() == 2 && first == second && first == 3){
            return true;
        }
        return rotateCheck(tmpResult);
    }
    private static boolean rotateCheck(List<Integer> tmpResult){
        boolean haveCondition = false;
        for (Integer tmp : tmpResult){
            tmp = tmp -1;
            if (tmp == 0 ){}
        }
        return false;
    }

    public static int sumDig(int n,int[] data,int index){
        int sum=0;
        if(n>=10){
            sum+=n%10;
            data[index] = n % 10;
            sum+=sumDig(n/10,data,index +1);
        }
        else {
            sum+=n;
        }
        return sum;
    }


    public static void main(String[] args) {
//        System.out.println(sumDig(111));
        generateWithoutEye(0,-99);
    }
}
