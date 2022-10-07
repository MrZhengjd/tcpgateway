package com.game.domain.relation.role;

import java.util.Map;

/**
 * @author zheng
 */
public class Role {
    private Long id;

    //存储玩家可以操作的信息到map
    private Map<String ,Object> operateInfo;

    public Map<String, Object> getOperateInfo() {
        return operateInfo;
    }

    public void setOperateInfo(Map<String, Object> operateInfo) {
        this.operateInfo = operateInfo;
    }







    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
