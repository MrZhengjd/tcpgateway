package com.game.mj.model;

import com.game.mj.store.BlockInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zheng
 */
@Getter
@Setter
public class TempBlockInfo {
    private Long key;
    private Integer writePosition;
    private Integer pageIndex;
    private static class Holder{
        private static TempBlockInfo INSTANCE = new TempBlockInfo();
    }
    public static TempBlockInfo getInstance(){
        return Holder.INSTANCE;
    }
    public static TempBlockInfo getByBlockInfo(BlockInfo blockInfo,Long lastOperateId){
        TempBlockInfo tempBlockInfo = getInstance();
        tempBlockInfo.setPageIndex(blockInfo.getPageIndex());
        tempBlockInfo.setWritePosition(blockInfo.getStartwritePosition());
        tempBlockInfo.setKey(lastOperateId);
        return tempBlockInfo;
    }
}
