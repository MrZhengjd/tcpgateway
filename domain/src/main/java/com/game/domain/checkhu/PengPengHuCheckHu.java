package com.game.domain.checkhu;

import com.game.mj.constant.InfoConstant;
import com.game.domain.model.vo.CheckHuVo;
import com.game.domain.relation.organ.InnerOrgan;
import com.game.domain.relation.pai.Pai;
import com.game.domain.relation.role.PlayerRole;
import com.game.domain.relation.role.PlayerRoleManager;
import com.game.domain.relation.room.RuleInfo;

/**
 * @author zheng
 */
public class PengPengHuCheckHu implements ExecuteCheckHu {
    @Override
    public void executeCheckHu(PlayerRole playerRole, Pai pai, RuleInfo ruleInfo, CheckHuVo checkHuVo) {
        PlayerRoleManager playerRoleManager = new PlayerRoleManager(playerRole);
        InnerOrgan inner = playerRoleManager.getOrganFromName(InnerOrgan.class);
        playerRoleManager.putHuPaiInfo(InfoConstant.PENG_PENG_HU,true);

    }
}
