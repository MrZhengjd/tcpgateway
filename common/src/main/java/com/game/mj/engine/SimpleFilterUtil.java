package com.game.mj.engine;

import com.alibaba.fastjson.JSONArray;

import java.util.HashMap;
import java.util.Map;


public class SimpleFilterUtil {
    private static Map<Integer,CalWay> calWayMap = new HashMap<>();
    static {
        calWayMap.put(EngineEnum.EQUAL.getValue(), new CalWay() {
            @Override
            public int calMeet(Object matterValue, Object composite) {
                return matterValue.equals(composite) ? 0 : 1;
            }
        });
        calWayMap.put(EngineEnum.LT.getValue(), new CalWay() {
            @Override
            public int calMeet(Object matterValue, Object composite) {
                return Double.parseDouble(matterValue.toString()) < Double.parseDouble(composite.toString()) ? 0 : 1;
            }
        });
        calWayMap.put(EngineEnum.GE.getValue(), new CalWay() {
            @Override
            public int calMeet(Object matterValue, Object composite) {
                return Double.parseDouble(matterValue.toString()) >= Double.parseDouble(composite.toString()) ? 0 : 1;
            }
        });
        calWayMap.put(EngineEnum.GT.getValue(), new CalWay() {
            @Override
            public int calMeet(Object matterValue, Object composite) {
                return Double.parseDouble(matterValue.toString()) > Double.parseDouble(composite.toString()) ? 0 : 1;
            }
        });
        calWayMap.put(EngineEnum.LE.getValue(), new CalWay() {
            @Override
            public int calMeet(Object matterValue, Object composite) {
                return Double.parseDouble(matterValue.toString()) <= Double.parseDouble(composite.toString()) ? 0 : 1;
            }
        });
        calWayMap.put(EngineEnum.ENUM.getValue(), new CalWay() {
            @Override
            public int calMeet(Object matterValue, Object composite) {
                return calEnum(matterValue,composite);
            }
        });
        calWayMap.put(EngineEnum.LEG.getValue(), new CalWay() {
            @Override
            public int calMeet(Object matterValue, Object composite) {
                return calLEG(matterValue,composite);
            }
        });
//        calWayMap.put(EngineEnum.SEG.getValue(), new CalWay() {
//            @Override
//            public int calMeet(Object matterValue, Object composite) {
//                return calSegment(matterValue,composite);
//            }
//        });
    }
    private interface CalWay{
        int calMeet(Object matterValue, Object object);
    }
//    public static int decisionLogic(PlayerRequest playerRequest, String name, Object compare, EngineEnum engineEnum) {
//       return decisionLogic(playerRequest.getRuleInfo(name),compare,engineEnum);
//
//    }
    public static int decisionLogic(Object matterValue, Object compare,EngineEnum engineEnum) {
        CalWay calWay = calWayMap.get(engineEnum.getValue());
        if (calWay == null){
            return 1;
        }
        return calWay.calMeet(matterValue,compare);

    }
//    public static boolean checkMeet(Object matterValue,int calType,Double limitValue){
//        switch (calType) {
//            case EngineEnum.EQUAL:
//                return matterValue.equals(limitValue) ? true : false;
//            case EngineConstants.RuleLimitType.LT:
//                return Double.parseDouble(matterValue.toString()) < limitValue ? true : false;
//            case EngineConstants.RuleLimitType.GE:
//                return Double.parseDouble(matterValue.toString()) >= limitValue ? true : false;
//
//            default:
//                return false;
//        }
//    }

//    private static int calSegment(Object matterValue, Object composite) {
//        JSONArray result = JSONArray.parseArray(composite.toString());
//
//        for (int i = 0 ;i<result.size();i++){
//            Segment segment = JSONObject.toJavaObject((JSON) result.get(i),Segment.class);
//            boolean left = checkMeet(matterValue,segment.getLeftCal(),segment.getStart());
//            boolean right = checkMeet(matterValue,segment.getRightCal(),segment.getEnd());
//            if (left && right){
//                return i;
//            }
//        }
//        return 0;
//    }

    private static int calLEG(Object matterValue, Object composite) {
        Double base = Double.parseDouble(matterValue.toString());
        Double compare = Double.parseDouble(composite.toString());
        if (base < compare) {return 0;}
        else if (base.equals(compare)){return 1;}
        else if (base > compare){return 2;}
        return 0;
    }


    private static int calEnum(Object matterValue, Object composite) {
        JSONArray result = JSONArray.parseArray(composite.toString());

        String matter =matterValue.toString();
        for (int i = 0 ;i<result.size();i++){
            if (matter.equals(result.get(i).toString())){
                return i;
            }
        }
        return 0;
    }
}
