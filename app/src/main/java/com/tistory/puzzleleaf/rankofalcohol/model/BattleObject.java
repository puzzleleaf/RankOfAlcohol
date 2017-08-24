package com.tistory.puzzleleaf.rankofalcohol.model;

/**
 * Created by cmtyx on 2017-08-24.
 */

public class BattleObject {
    private long outTime;
    private String name;

    public BattleObject(){

    }

    public BattleObject(String name) {
        this.outTime = 999999;
        this.name = name;
    }

    public long getOutTime() {
        return outTime;
    }

    public void setOutTime(long outTime) {
        this.outTime = outTime;
    }

    public String getName() {
        return name;
    }

}
