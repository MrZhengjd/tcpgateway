package com.game.domain.checkhu;

import com.game.mj.constant.InfoConstant;
import com.game.domain.model.vo.CheckHuVo;
import com.game.domain.relation.pai.Pai;
import com.game.domain.relation.role.PlayerRole;
import com.game.domain.relation.role.PlayerRoleManager;
import com.game.domain.relation.room.RuleInfo;

/**
 * @author zheng
 */
public class ClearColorDecoratorCheckHu extends CheckHuDecorator {
    @Override
    public void executeCheckHu(PlayerRole playerRole, Pai pai, RuleInfo ruleInfo, CheckHuVo checkHuVo) {
        PlayerRoleManager playerRoleManager = new PlayerRoleManager(playerRole);
        playerRoleManager.putHuPaiInfo(InfoConstant.CLEAR_COLOR,true);
    }
}
