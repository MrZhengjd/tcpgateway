package com.game.mj;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;

/**
 * @author zheng
 */
public class GenerateBaseHuInfo {
    private static ClassPathResource classPathResource = new ClassPathResource("data");
    private static Map<Integer,Boolean> dataMap = new HashMap<>();
    public static void generateWithEye(int level,int last){
        int base = (int) Math.pow(10,level);
        if (last == - 99){
            last = 0;
        }
//        String local = new String(tmp);
        for (int i = 0;i<5;i++){
            int tmpBase = i * base;
            int tmp = tmpBase + last;
            subGenerateWithEye(level,tmp);


        }

    }
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
            

            subGenerate(level,tmp);


        }

    }

    private static void subGenerate(int level,int tmp){
        boolean b = checkTmp(tmp);
        if (b){
            dataMap.put(tmp,true);
            // System.out.println("here can use "+ tmp);
        }
        if (tmp < 4000000 && level < 8){
            generateWithoutEye(level +1,tmp);
        }
    }

    private static void subGenerateWithEye(int level,int tmp){
        boolean b = checkTmpWithEye(tmp);
        if (b){
            dataMap.put(tmp,true);
            // System.out.println("here can use "+ tmp);
        }
        if (tmp < 4000000 && level < 8){
            generateWithEye(level +1,tmp);
        }
    }
    private static boolean checkTmp(int tmp) {
        List<Integer> result = new ArrayList<Integer>();
        if (!baseCheck(tmp,0,12,result)){
            return false;
        }
        Collections.reverse(result);
        return rotateCheck(result);
    }
    private static boolean baseCheck(int tmp,int left,int size,List<Integer> result){
        if(tmp % 10 == 0){
            return false;
        }

        int total = sumDig(tmp,result,0);
        if (total == 0){
            return false;
        }
        boolean first = total % 3 == left && total <= size;
        if (!first){
            return false;
        }
        return true;
    }
    private static boolean checkTmpWithEye(int tmp) {
        List<Integer> result = new ArrayList<Integer>();
        if (!baseCheck(tmp,2,14,result)){
            return false;
        }
        Collections.reverse(result);
        return rotateCheck(result);
    }
    private static boolean rotateCheck(List<Integer> result ){
        int total = 0;
        List<Integer> tmpResult = new ArrayList<>();
        for (int i = 0;i<result.size();i++){
            total += result.get(i);
            if (result.get(i) == 0){
                if (!checkData(total,tmpResult)){
                    return false;
                }
                total = 0;
                tmpResult.clear();
            }else {
                tmpResult.add(result.get(i));

            }
        }

        return  checkData(total,tmpResult);
    }
    private static boolean checkData(int total,List<Integer> tmpResult){
        boolean first = total % 3 == 0 && total <= 12;
        if (!first){
            return false;
        }
        if(tmpResult.size() > 0 && tmpResult.size() <= 3){
            Integer co = tmpResult.get(0);
            for(Integer tm : tmpResult){
                if(!tm.equals(co) ){
                    return false;
                }
            }
        }else {
            if(tmpResult.size() > 3){
                for (int i = 0;i<3;i++){
                    tmpResult.set(i,tmpResult.get(i)-1);
                }
                if (!rotateCheck(tmpResult)){
                    return false;
                }
            }

        }
        return true;
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


    public static int sumDig(int n,List<Integer> data,int index){
        int sum=0;
        if(n>=10){
            sum+=n%10;
            data.add(n % 10) ;
            sum+=sumDig(n/10,data,index +1);
        }
        else {
            data.add(n) ;
            sum+=n;
        }
        return sum;
    }


    public static void main(String[] args) {
//        System.out.println(sumDig(111));
        // List<Integer> data = new ArrayList<>();
        // data.add(2);
        // data.add(3);
        // data.add(3);
        // data.add(1);
        // boolean b = checkTmp(2331);
        // System.out.println("result "+b);
        generateWithoutEye(0,-99);
        dump(classPathResource.getPath()+"/test.txt");
        String key = "30030302";
        showHuPaiPath(key);
    }

    private static void showHuPaiPath(String key) {

        if (key == null || key.isEmpty()){
            return;
        }
        List<Integer> eachCollections = new ArrayList<>();
        for (int i = 0;i< key.length();i++){
            int tmpIndex = i +1;
            if (tmpIndex > key.length()){
                break;
            }
            eachCollections.add(Integer.parseInt(key.substring(i,i+1)));
        }
//        StringBuffer stringBuffer = new StringBuffer();
        rotateCheckHuKey(eachCollections);
        System.out.println(eachCollections);
    }
    private static void rotateCheckHuKey(List<Integer> source){
        int baseLength = source.size();
        int length =baseLength+2;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0;i< length;i++){
            if (i >= baseLength){
                if (i == baseLength){
                    source.add(0,1);
                    checkHu(source,0,stringBuilder);
                    source.remove(0);
                }else {
                    source.add(1);
                    checkHu(source,baseLength,stringBuilder);
                    source.remove(baseLength);
                }
            }else {
                source.set(i,source.get(i)+1);
                checkHu(source,i,stringBuilder);
                source.set(i,source.get(i)-1);
            }
        }

//        System.out.println(eachCollections);
//        System.out.println(generateKey(source,stringBuilder));
    }
    private static void checkHu(List<Integer> source,Integer key,StringBuilder stringBuilder){
        if (source.get(key) > 4){
            return;
        }
        Integer huKey = generateKey(source,stringBuilder);
        if (dataMap.containsKey(huKey)){
            System.out.println(huKey);
        }
    }
    private static Integer generateKey(List<Integer> collectDatas,StringBuilder stringBuilder){
        if (stringBuilder.length() > 0){
            stringBuilder.delete(0,stringBuilder.length());
        }

        for (Integer integer : collectDatas){
            stringBuilder.append(integer);
        }
        return Integer.valueOf(stringBuilder.toString());
    }

    public static void dump(String name){
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(name));
            Iterator<Map.Entry<Integer, Boolean>> iterator = dataMap.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<Integer, Boolean> next = iterator.next();
                bw.write(next.getKey()+"");
                bw.newLine();
                if (next.getKey()==30030303){
                    System.out.println("here is key ");
                }
            }
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
