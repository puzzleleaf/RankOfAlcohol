package com.tistory.puzzleleaf.rankofalcohol.model;

/**
 * Created by cmtyx on 2017-08-03.
 */

public class RatingObject {
    private int total;
    private int num;

    public RatingObject(){

    }

    public RatingObject(int total, int num) {
        this.total = total;
        this.num = num;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
