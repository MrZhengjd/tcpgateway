package com.game.ai;

/**
 * @author zheng
 */
public class HuTableInfo {
    public byte needGhost;
    private boolean jiang;
    private byte[] hupai = new byte[9];

    @Override
    public String toString() {
        StringBuffer tmp = new StringBuffer();
        int index = 1;
        if (hupai == null){
            tmp.append("胡清") ;
        }else {
            for (byte i : hupai){
                if (i > 0){
                    tmp.append("胡").append(index);
                }
                index ++;
            }
        }
        return tmp.append("将").append(jiang ? "1":"0").append("鬼").append(needGhost).toString();
    }
}
