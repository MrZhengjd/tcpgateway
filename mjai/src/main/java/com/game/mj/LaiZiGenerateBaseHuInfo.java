package com.game.mj;

import com.game.index.Constants;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

/**
 * @author zheng
 */
public class LaiZiGenerateBaseHuInfo {
    private static ClassPathResource classPathResource = new ClassPathResource("data");
    private static Map<Integer,Boolean> dataMap = new HashMap<>();
    private static Map<Integer,Map<Integer,Boolean>> eachDataMap = new HashMap<>();
    /**
     * 列举所有能胡的情况
     * n*111+m*3
     */
    private static void generateWithoutEye( int level,int last,int laiZiCount){
//        tmp.trim();
        int tmpLevel = level;
        int base = (int) Math.pow(10,level);
        if (last == - 99){
            last = 0;
        }
        List<Integer> leftLaiZiCount = new ArrayList<>();
//        String local = new String(tmp);
        for (int i = 0;i<5;i++){
            int tmpBase = i * base;
            int tmp = tmpBase + last;
            leftLaiZiCount.clear();
            leftLaiZiCount.add(laiZiCount);
            subGenerate(level,tmp,laiZiCount,leftLaiZiCount);
        }

    }
    private static void subGenerate(int level,int tmp,int laiZiCount,List<Integer> leftLaiZiCount){
        boolean b = checkTmp(tmp,leftLaiZiCount);
        if (b){
            dataMap.put(tmp,true);
            // System.out.println("here can use "+ tmp);
        }
        if (tmp < 4000000 && level < 8){
            generateWithoutEye(level +1,tmp,laiZiCount);
        }
    }
    public static void generateWithEye(int level,int last,int laiZiCount){
        int base = (int) Math.pow(10,level);
        if (last == - 99){
            last = 0;
        }
        List<Integer> leftLaiZiCount = new ArrayList<>();
//        String local = new String(tmp);
        for (int i = 0;i<5;i++){
            int tmpBase = i * base;
            int tmp = tmpBase + last;
            leftLaiZiCount.clear();
            leftLaiZiCount.add(laiZiCount);
            subGenerateWithEye(level,tmp,leftLaiZiCount,laiZiCount);


        }

    }
    private static void subGenerateWithEye(int level,int tmp,List<Integer> leftLaiZiCount,int laiZiCount){

        boolean b = checkTmpWithEye(tmp,leftLaiZiCount);
        if (b){
            dataMap.put(tmp,true);
            // System.out.println("here can use "+ tmp);
        }
        if (tmp < 4000000 && level < 8){
            generateWithEye(level +1,tmp,laiZiCount);
        }
    }
    private static boolean checkTmpWithEye(int tmp,List<Integer> left) {

        List<Integer> result = new ArrayList<>();
        if (!baseCheck(tmp,2,14,result,left)){
            return false;
        }
        Collections.reverse(result);
        List<Boolean> findJiang = new ArrayList<>();
        findJiang.add(false);
        return rotateCheckt(result,left,findJiang);
    }
    private static boolean baseCheck(int tmp,int left,int size,List<Integer> result,List<Integer> laiZiArray){
        if(tmp % 10 == 0){
            return false;
        }
        int total = sumDig(tmp,result,0) +laiZiArray.get(0);
        if (total == 0){
            return false;
        }
        boolean first = total % 3 == left && total <= size;
        if (!first){
            return false;
        }
        return true;
    }

    private static boolean checkTmp(int tmp ,List<Integer> leftLaiZiCount) {
        List<Integer> result = new ArrayList<Integer>();
        if (!baseCheck(tmp,0,12,result,leftLaiZiCount)){
            return false;
        }
        Collections.reverse(result);
        return rotateCheck(result,leftLaiZiCount);
    }

    private static boolean rotateCheck(List<Integer> result, List<Integer> leftLaiZiCount ){
        int total = 0;
        List<Integer> tmpResult = new ArrayList<>();

        for (int i = 0;i<result.size();i++){
            total += result.get(i);
            if (result.get(i) == 0){
                if (!checkData(total,tmpResult,leftLaiZiCount)){
                    return false;
                }

                total = 0;
                tmpResult.clear();
            }else {
                tmpResult.add(result.get(i));

            }
        }

        return  checkData(total,tmpResult,leftLaiZiCount);
    }
    private static boolean rotateCheckt(List<Integer> result, List<Integer> leftLaiZiCount,List<Boolean> findJiang ){
        int total = 0;
        List<Integer> tmpResult = new ArrayList<>();
        int beforeLaiZiCount = leftLaiZiCount.get(0);
        int beforeId = result.get(0);
        for (int i = 0;i<result.size();i++){
            if (!findJiang.get(0)){
                if (result.get(i) + leftLaiZiCount.get(0) >= 2){
                   int local =  result.get(0)-2;
                   if (local < 0){
                       local = 0;
                       leftLaiZiCount.set(0,leftLaiZiCount.get(0)+local);
                   }
                   result.set(i,local);
                   findJiang.set(0,true);
                }
            }
            total += result.get(i);
            if (result.get(i) == 0){

                if (!checkDataWithEye(total,tmpResult,leftLaiZiCount,findJiang)){
                    leftLaiZiCount.set(0,beforeLaiZiCount);
                    result.set(0,beforeId);
                    return false;
                }

                total = 0;
                tmpResult.clear();
            }else {
                tmpResult.add(result.get(i));

            }

        }

        return  checkDataWithEye(total,tmpResult,leftLaiZiCount,findJiang);
    }
    private static boolean checkData(int total,List<Integer> tmpResult,List<Integer> leftLaiZiCount){
        boolean first = total % 3 == 0 && total <= 12;
        boolean second = (total+leftLaiZiCount.get(0))% 3 == 0 &&  (total+leftLaiZiCount.get(0))<= 12;
        if (!(second || first)){
            return false;
        }
        if(tmpResult.size() > 0 && tmpResult.size() <= 3){
            Integer co = tmpResult.get(0);
            for(Integer tm : tmpResult){
                if(tm == co ){
                    continue;
                }
                if ( leftLaiZiCount.get(0) > 0 && Math.abs(tm - co) > leftLaiZiCount.get(0)){
                    return false;
                }
                leftLaiZiCount.set(0,leftLaiZiCount.get(0) -Math.abs(tm - co));

            }
        }else {
            if(tmpResult.size() > 3){
                for (int i = 0;i<3;i++){
                    tmpResult.set(i,tmpResult.get(i)-1);
                }
                if (!rotateCheck(tmpResult,leftLaiZiCount)){
                    return false;
                }
            }

        }
        if (tmpResult.size() == 1 && tmpResult.get(0) != 3){
            if (leftLaiZiCount.get(0) < 0){
                return false;
            }
            leftLaiZiCount.set(0,leftLaiZiCount.get(0) -3+tmpResult.get(0));
        }
        return true;
    }

    private static boolean checkDataWithEye(int total,List<Integer> tmpResult,List<Integer> leftLaiZiCount,List<Boolean> findJiang){
        boolean first = total % 3 == 2 && total <= 14;
        boolean second = (total+leftLaiZiCount.get(0))% 3 == 2 &&  (total+leftLaiZiCount.get(0))<= 14;
        if (!(second || first)){
            return false;
        }
        if(tmpResult.size() > 0 && tmpResult.size() <= 3){
            Integer co = tmpResult.get(0);
            for(Integer tm : tmpResult){
                if(tm == co ){
                    continue;
                }

                if (leftLaiZiCount.get(0) > 0 && Math.abs(tm - co) > leftLaiZiCount.get(0)){
                    return false;
                }
                leftLaiZiCount.set(0,leftLaiZiCount.get(0) -Math.abs(tm - co));

            }
        }else {
            if(tmpResult.size() > 3){
                for (int i = 0;i<3;i++){
                    tmpResult.set(i,tmpResult.get(i)-1);
                }
                if (!rotateCheckt(tmpResult,leftLaiZiCount,findJiang)){
                    return false;
                }
            }

        }
        if (tmpResult.size() == 1 && tmpResult.get(0) != 3){
            if (leftLaiZiCount.get(0) < 0){
                return false;
            }
            leftLaiZiCount.set(0,leftLaiZiCount.get(0) -3+tmpResult.get(0));
        }
        return true;
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
    private static void baseGenerate(String name,int laiZiCount){
        generateWithoutEye(0,-99,laiZiCount);
        dump(classPathResource.getPath()+name);
        eachDataMap.put(laiZiCount,new HashMap<>(dataMap));
        dataMap.clear();
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
//        for (int i = 0;i<1;i++){
//            baseGenerate("/testeye"+i+".txt",i);
//        }
//
//
//        List<LPai> holdPais = new ArrayList<>();
//        holdPais.add(LPai.generatePai(11));
//        holdPais.add(LPai.generatePai(11));
//        holdPais.add(LPai.generatePai(11));
//
//        holdPais.add(LPai.generatePai(14));
//        holdPais.add(LPai.generatePai(14));
//        holdPais.add(LPai.generatePai(14));
//
//        holdPais.add(LPai.generatePai(16));
//        holdPais.add(LPai.generatePai(16));
//        holdPais.add(LPai.generatePai(16));
//        LPai lPai = LPai.generatePai(18);
//        lPai.changeAttribute(Constants.GHOST,true);
//        holdPais.add(lPai);
//        holdPais.add(LPai.generatePai(18));
//        holdPais.add(LPai.generatePai(18));
//
//        long timeMillis = System.currentTimeMillis();
//        System.out.println("start time "+timeMillis);
//        String key = "30030302";
//        showHuPaiPath(key);
//        System.out.println(System.currentTimeMillis()-timeMillis);
        List<Integer> leftLaiZiCount = new ArrayList<>();
        int tmp = 11011241;
        leftLaiZiCount.add(0);
        boolean b = checkTmpWithEye(tmp,leftLaiZiCount);
        System.out.println("result "+b);
    }
    public static int  getLaiZiCount(List<LPai> list){
        if (list == null || list.isEmpty()){
            return 0;
        }
        int laiZiCount = 0;
        Iterator<LPai> iterator = list.iterator();
        while (iterator.hasNext()){
            LPai next = iterator.next();
            if (next.containKey(Constants.GHOST)){
                laiZiCount ++;
                iterator.remove();
            }
        }
        return laiZiCount;

    }

    private static void showHuPaiPath(String key) {

        if (key == null || key.isEmpty()){
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        List<Integer> eachCollections = new ArrayList<>();
        for (int i = 0;i< key.length();i++){
            int tmpIndex = i +1;
            if (tmpIndex > key.length()){
                break;
            }
            stringBuffer.append(key.substring(i,i+1));
        }
        Map<Integer, Boolean> integerBooleanMap = eachDataMap.get(1);
        if (integerBooleanMap.containsKey(Integer.parseInt(stringBuffer.toString()))){
            System.out.println(stringBuffer.toString());
        }

//        rotateCheckHuKey(eachCollections);
//        System.out.println(eachCollections);
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
