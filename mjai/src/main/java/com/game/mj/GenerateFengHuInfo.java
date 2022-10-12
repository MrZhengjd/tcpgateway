package com.game.mj;

import java.util.*;

/**
 * @author zheng
 */
public class GenerateFengHuInfo {
    //不待将牌的胡牌信息
//    private static Map<String, Object> huPaiInfo = new HashMap<>();
//    //带将牌的胡牌规则
//    private static Map<String, Object> huPaiEyeInfo = new HashMap<>();
//    //不待将牌的胡牌信息
//    private static Map<String, Object> fengHuPaiInfo = new HashMap<>();
//    //带将牌的胡牌规则
//    private static Map<String, Object> fengHuPaiEyeInfo = new HashMap<>();
//    private static StringBuilder builder = new StringBuilder();
//    private static StringBuffer stringBuffer = new StringBuffer();
//    private static Map<Integer,Boolean> holdMap = new HashMap<>();
//    static double key = 3*Math.pow(10,7);
    /**
     * 穷举胡牌信息
     * 在[0, 0, 0, 0, 0, 0, 0 , 0, 0] 9个空位用一个数字3或者 [1, 1, 1] 填, 每个空位不能超过4(大于4), 选择次数不能超过4(大于4), 找出所有情况
     */


    private static void generateFengWithoutEye(int level ,int use) {
        generateFengKeZi("",1,0);
//        generateFengSubWithoutEye(tmp, level, 1, use);

    }
    private static void generateFengWithEye(String tmp, int level, int count,int jiangCount){
        tmp.trim();
        int tmpCount = count;
        int tmpLevel = level;
        int tmpJiangCount = jiangCount;
        String local = new String(tmp);
        for (int i = 0;i<3;i++){

            if (!(tmp.equals("") ||tmp.length() <7)){
                tmp = local;
                continue;
            }
            if (tmp.startsWith("0") ){
                continue;
            }

            if (i == 1){
                tmp+="0";
            }else if (i == 0){
                tmp+="3";
                count++;
            }else {
                tmp+="2";
                if (level > 1)
                level = level-1;
                jiangCount++;
            }

            boolean b = generateFengSubWithEye(tmp, level, count,jiangCount);

            if (tmp.length() <7 || jiangCount > 1){
                int tmLevel = level;
                if (i ==0 ){
                    tmLevel = level+1;
                }

                generateFengWithEye(tmp,tmLevel,count,jiangCount);
//                tmp = tmp.substring(0,tmp.length()-1);
                count = tmpCount;
                level = tmpLevel;
                tmp = local;
                jiangCount = tmpJiangCount;
            }


        }
    }
    private static void generateFengKeZi(String tmp, int level, int count){
        tmp.trim();
        int tmpCount = count;
        int tmpLevel = level;
        String local = new String(tmp);
        for (int i = 0;i<2;i++){

            if (!(tmp.equals("")||tmp.startsWith("3") ||tmp.length() <6)){
                tmp = local;
                continue;
            }
            if (tmp.startsWith("0")){
                continue;
            }
            if (i == 1){
                tmp+="0";
            }else {
                tmp+="3";

                count++;
            }

            boolean b = generateFengSubWithoutEye(tmp, level, count);

            if (tmp.length() <7 ){
                int tmLevel = level;
                if (i ==0 ){
                    tmLevel = level+1;
                }
                generateFengKeZi(tmp,tmLevel,count);
//                tmp = tmp.substring(0,tmp.length()-1);
                count = tmpCount;
                level = tmpLevel;
                tmp = local;
            }


        }
    }
    private static boolean generateFengSubWithoutEye(String  tmp,int level,int count) {

//        String  tmp = "3";
        if (count > 4){
            return false;
        }
        if (count >= level || tmp.length()>=7){
            if (count < level || tmp.length()>7 || !tmp.endsWith("3")){
                return false;
            }
            System.out.println("tmp "+tmp);
            return true;
        }
        return false;

//        generateFengSubWithoutEye(tmp,level,count,count+1);
//        tmp=tmp.substring(0,tmp.length()-1);

    }
    private static boolean generateFengSubWithEye(String  tmp,int level,int count,int jiangCount) {

//        String  tmp = "3";
        if (count > 4 || jiangCount!= 1){
            return false;
        }
        if (count >= level || tmp.length()>=7){
            if (count < level || tmp.length()>7 || !(tmp.endsWith("3") || tmp.endsWith("2"))){
                return false;
            }
            System.out.println("tmp "+tmp);
            return true;
        }
        return false;

//        generateFengSubWithoutEye(tmp,level,count,count+1);
//        tmp=tmp.substring(0,tmp.length()-1);

    }
    public static void main(String[] args) {
//        generateFengWithoutEye(1,1);
        generateFengWithEye("",1,1,0);
    }
    private static void generateShunZi(int shunZiCount) {

        for (int i =0;i<shunZiCount;i++){

        }
    }

//    private static boolean checkExists(int b){
//        if (holdMap.containsKey(b)){
//            return false;
//        }
//        return true;
//    }

    private void dfsHu(Integer[] nums,int needCount,int count ){
        if (needCount > 3){
            return;
        }
        for (int i = 0;i <= 15;i++){
            count ++;
            if (i <= 8){
                if (nums[i] > 1){
                    continue;
                }
                nums[i]+= 3;
            }else {
                int index = i - 9;
                if (nums[index] >= 4 || nums[index+1] >= 4 || nums[index+2] >= 4){
                    continue;
                }
                nums[index] += 1;
                nums[index+1] += 1;
                nums[index+2] += 1;

            }
            int keyNum = 0;
            for (int j = 0;j<9;j++){
                keyNum = keyNum * 10 + nums[j];
            }
//            if (checkExists(keyNum) == false){
//
//            }
        }
    }
//    private static void init(){
//        stringBuffer.append("3");
//    }
}
