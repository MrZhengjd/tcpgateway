package com.game.domain.checkhu;


import com.game.domain.relation.pai.Pai;
import com.game.domain.relation.role.PlayerRole;
import com.game.domain.relation.room.RuleInfo;

/**
 * @author zheng
 */
public interface CheckHuModel {
    void checkHu(PlayerRole playerRole, Pai pai, RuleInfo ruleInfo);
}
