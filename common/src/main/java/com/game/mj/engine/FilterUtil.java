package com.game.mj.engine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class FilterUtil {
    private static Map<Integer,CalWay> calWayMap = new HashMap<>();
    static {
        calWayMap.put(EngineConstants.RuleLimitType.EQUAL, new CalWay() {
            @Override
            public int calMeet(Object matterValue, RuleNodeComposite composite) {
                return matterValue.equals(composite.getLimitVal()) ? 0 : 1;
            }
        });
        calWayMap.put(EngineConstants.RuleLimitType.LT, new CalWay() {
            @Override
            public int calMeet(Object matterValue, RuleNodeComposite composite) {
                return Double.parseDouble(matterValue.toString()) < Double.parseDouble(composite.getLimitVal().toString()) ? 0 : 1;
            }
        });
        calWayMap.put(EngineConstants.RuleLimitType.GE, new CalWay() {
            @Override
            public int calMeet(Object matterValue, RuleNodeComposite composite) {
                return Double.parseDouble(matterValue.toString()) >= Double.parseDouble(composite.getLimitVal().toString()) ? 0 : 1;
            }
        });
        calWayMap.put(EngineConstants.RuleLimitType.GT, new CalWay() {
            @Override
            public int calMeet(Object matterValue, RuleNodeComposite composite) {
                return Double.parseDouble(matterValue.toString()) > Double.parseDouble(composite.getLimitVal().toString()) ? 0 : 1;
            }
        });
        calWayMap.put(EngineConstants.RuleLimitType.LE, new CalWay() {
            @Override
            public int calMeet(Object matterValue, RuleNodeComposite composite) {
                return Double.parseDouble(matterValue.toString()) <= Double.parseDouble(composite.getLimitVal().toString()) ? 0 : 1;
            }
        });
        calWayMap.put(EngineConstants.RuleLimitType.ENUM, new CalWay() {
            @Override
            public int calMeet(Object matterValue, RuleNodeComposite composite) {
                return calEnum(matterValue,composite);
            }
        });
        calWayMap.put(EngineConstants.RuleLimitType.LEG, new CalWay() {
            @Override
            public int calMeet(Object matterValue, RuleNodeComposite composite) {
                return calLEG(matterValue,composite);
            }
        });
        calWayMap.put(EngineConstants.RuleLimitType.SEG, new CalWay() {
            @Override
            public int calMeet(Object matterValue, RuleNodeComposite composite) {
                return calSegment(matterValue,composite);
            }
        });
    }
    private interface CalWay{
        int calMeet(Object matterValue, RuleNodeComposite composite);
    }
    public static int decisionLogic(Object matterValue, RuleNodeComposite composite) {
        return calWayMap.get(composite.getCalType()).calMeet(matterValue,composite);

    }
    public static boolean checkMeet(Object matterValue,int calType,Double limitValue){
        switch (calType) {
            case EngineConstants.RuleLimitType.EQUAL:
                return matterValue.equals(limitValue) ? true : false;
            case EngineConstants.RuleLimitType.LT:
                return Double.parseDouble(matterValue.toString()) < limitValue ? true : false;
            case EngineConstants.RuleLimitType.GE:
                return Double.parseDouble(matterValue.toString()) >= limitValue ? true : false;

            default:
                return false;
        }
    }

    private static int calSegment(Object matterValue, RuleNodeComposite composite) {
        JSONArray result = JSONArray.parseArray(composite.getLimitVal().toString());

        for (int i = 0 ;i<result.size();i++){
            Segment segment = JSONObject.toJavaObject((JSON) result.get(i),Segment.class);
            boolean left = checkMeet(matterValue,segment.getLeftCal(),segment.getStart());
            boolean right = checkMeet(matterValue,segment.getRightCal(),segment.getEnd());
            if (left && right){
                return i;
            }
        }
        return 0;
    }

    private static int calLEG(Object matterValue, RuleNodeComposite composite) {
        Double base = Double.parseDouble(matterValue.toString());
        Double compare = Double.parseDouble(composite.getLimitVal().toString());
        if (base < compare) {return 0;}
        else if (base.equals(compare)){return 1;}
        else if (base > compare){return 2;}
        return 0;
    }


    private static int calEnum(Object matterValue, RuleNodeComposite composite) {
        JSONArray result = JSONArray.parseArray(composite.getLimitVal().toString());

        String matter =matterValue.toString();
        for (int i = 0 ;i<result.size();i++){
            if (matter.equals(result.get(i).toString())){
                return i;
            }
        }
        return 0;
    }
}
