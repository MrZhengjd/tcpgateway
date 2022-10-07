package com.game.domain.checkhu;

import com.game.mj.constant.InfoConstant;

import com.game.domain.model.vo.CheckHuVo;
import com.game.domain.relation.organ.Organ;
import com.game.domain.relation.pai.Pai;
import com.game.domain.relation.role.PlayerRole;
import com.game.domain.relation.room.RuleInfo;
import org.springframework.stereotype.Service;

/**
 * @author zheng
 */
@Service
public class PingHuCheckAndModify implements CheckAndModify {
    @Override
    public void executeCheckHu(PlayerRole playerRole, Pai pai, RuleInfo ruleInfo, CheckHuVo checkHuVo) {
        Organ organ = playerRole.getOrganMap().get(InfoConstant.HU_PAI_TYPE_MAP);

    }
}
