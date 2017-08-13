package com.tistory.puzzleleaf.rankofalcohol.model;

/**
 * Created by cmtyx on 2017-08-13.
 */
//FireBase에서 Null 감지를 위해 사용하는 Object Class
public class AnalysisValueObject {
    float num;

    public AnalysisValueObject() {
    }

    public AnalysisValueObject(float num) {
        this.num = num;
    }

    public float getNum() {
        return num;
    }

    public void setNum(float num) {
        this.num = num;
    }
}
