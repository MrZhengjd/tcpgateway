package com.game.domain.checkhu;


import com.game.domain.model.vo.CheckHuVo;
import com.game.domain.relation.pai.Pai;
import com.game.domain.relation.role.ExecuteRule;
import com.game.domain.relation.role.PlayerRole;
import com.game.domain.relation.room.RuleInfo;

/**
 * @author zheng
 */
public interface ExecuteCheckHu extends ExecuteRule {
    public void executeCheckHu(PlayerRole playerRole, Pai pai, RuleInfo ruleInfo, CheckHuVo checkHuVo);
}
