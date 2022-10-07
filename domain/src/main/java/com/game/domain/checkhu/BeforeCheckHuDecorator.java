package com.game.domain.checkhu;

import com.game.domain.model.vo.CheckHuVo;
import com.game.domain.relation.pai.Pai;
import com.game.domain.relation.role.PlayerRole;
import com.game.domain.relation.room.RuleInfo;

/**
 * @author zheng
 */
public class BeforeCheckHuDecorator extends CheckHuDecorator {

    @Override
    public void executeCheckHu(PlayerRole playerRole, Pai pai, RuleInfo ruleInfo, CheckHuVo checkHuVo) {
        super.executeCheckHu(playerRole, pai, ruleInfo, checkHuVo);
    }
}
