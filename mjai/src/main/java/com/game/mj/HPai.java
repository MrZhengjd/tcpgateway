package com.game.mj;


/**
 * @author zheng
 */

public class HPai implements Comparable<HPai> {
    private int number;
    private Type type;
    @Override
    public int compareTo(HPai o) {
        if (o.getType() == this.type){
            return this.number - o.number;
        }else {
            return this.type.index - o.type.index;
        }


    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public HPai(int number, Type type) {
        this.number = number;
        this.type = type;
    }

    public HPai() {
    }
}
