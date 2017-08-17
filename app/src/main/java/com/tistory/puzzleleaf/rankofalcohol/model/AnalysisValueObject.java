package com.tistory.puzzleleaf.rankofalcohol.model;

/**
 * Created by cmtyx on 2017-08-13.
 */
//FireBase에서 Null 감지를 위해 사용하는 Object Class
public class AnalysisValueObject {
    float soju;
    float beer;
    float makgeolli;
    float etc;
    float total;

    public AnalysisValueObject(){
        this.total = 0;
        this.soju = 0;
        this.beer = 0;
        this.makgeolli = 0;
        this.etc = 0;
    }

    public AnalysisValueObject(float soju, float beer, float makgeolli, float etc) {
        this.soju = soju;
        this.beer = beer;
        this.makgeolli = makgeolli;
        this.etc = etc;
        makeTotal();
    }

    public float getSoju() {
        return soju;
    }

    public void setSoju(float soju) {
        this.soju = soju;
    }

    public float getBeer() {
        return beer;
    }

    public void setBeer(float beer) {
        this.beer = beer;
    }

    public float getMakgeolli() {
        return makgeolli;
    }

    public void setMakgeolli(float makgeolli) {
        this.makgeolli = makgeolli;
    }

    public float getEtc() {
        return etc;
    }

    public void setEtc(float etc) {
        this.etc = etc;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public void makeTotal(){
        total = getSoju() + getBeer() + getMakgeolli() + getEtc();
    }
}
