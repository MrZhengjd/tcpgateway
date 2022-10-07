package com.game.domain.checkhu;


import com.game.domain.model.vo.CheckHuVo;
import com.game.domain.relation.pai.Pai;
import com.game.domain.relation.role.PlayerRole;
import com.game.domain.relation.room.RuleInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zheng
 */
@Getter
@Setter
public class DefaultCheckHu implements CheckHuModel {
    protected List<ExecuteCheckHu> beforeHus = new ArrayList<>();
    protected List<ExecuteCheckHu> checkHuses = new ArrayList<>();
    protected List<ExecuteCheckHu> afterHus = new ArrayList<>();
    @Override
    public void checkHu(PlayerRole playerRole, Pai pai, RuleInfo ruleInfo) {
        CheckHuVo checkHuVo = new CheckHuVo();
        executeCheckHuListRule(beforeHus, playerRole, pai, ruleInfo, checkHuVo, new CheckCondition() {
            @Override
            public boolean checkMeet() {
                return checkHuVo.isMeetCanHu();
            }
        });
        if (checkHuVo.isMeetBeforeHu()){
            executeCheckHuListRule(checkHuses,playerRole,pai,ruleInfo,checkHuVo);
        }
        if (checkHuVo.isMeetCanHu()){
            executeCheckHuListRule(afterHus,playerRole,pai,ruleInfo,checkHuVo);
        }
    }

    /**
     *
     * @param executeRules
     */
    private void executeCheckHuListRule(List<? extends ExecuteCheckHu> executeRules, PlayerRole playerRole, Pai pai, RuleInfo ruleInfo, CheckHuVo checkHuVo){
        if (executeRules != null && executeRules.size() > 0){
            for (ExecuteCheckHu executeRule : executeRules){
                executeRule.executeCheckHu(playerRole,pai,ruleInfo,checkHuVo);
            }
        }
    }

    /**
     *
     * @param executeRules
     */
    private void executeCheckHuListRule(List<? extends ExecuteCheckHu> executeRules, PlayerRole playerRole, Pai pai, RuleInfo ruleInfo, CheckHuVo checkHuVo, CheckCondition checkCondition){
        if (executeRules != null && executeRules.size() > 0){
            for (ExecuteCheckHu executeRule : executeRules){
                executeRule.executeCheckHu(playerRole,pai,ruleInfo,checkHuVo);
                if (!checkCondition.checkMeet()){
                    return;
                }
            }
        }
    }
    private interface CheckCondition{
        boolean checkMeet();
    }
}
