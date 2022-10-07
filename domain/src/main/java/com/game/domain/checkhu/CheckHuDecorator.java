package com.game.domain.checkhu;


import com.game.domain.model.vo.CheckHuVo;
import com.game.domain.relation.pai.Pai;
import com.game.domain.relation.role.PlayerRole;
import com.game.domain.relation.room.RuleInfo;

/**
 * @author zheng
 */
public abstract class CheckHuDecorator implements ExecuteCheckHu {
    protected ExecuteCheckHu executeCheckHu;

    public ExecuteCheckHu getExecuteCheckHu() {
        return executeCheckHu;
    }

    public void setExecuteCheckHu(ExecuteCheckHu executeCheckHu) {
        this.executeCheckHu = executeCheckHu;
    }

    public CheckHuDecorator() {
    }

    public CheckHuDecorator(ExecuteCheckHu executeCheckHu) {
        this.executeCheckHu = executeCheckHu;
    }

    @Override
    public void executeCheckHu(PlayerRole playerRole, Pai pai, RuleInfo ruleInfo, CheckHuVo checkHuVo) {
        if (executeCheckHu != null){
            executeCheckHu.executeCheckHu(playerRole,pai,ruleInfo,checkHuVo);
        }
    }
}
