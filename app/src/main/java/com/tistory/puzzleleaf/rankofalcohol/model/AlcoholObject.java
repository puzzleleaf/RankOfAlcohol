package com.tistory.puzzleleaf.rankofalcohol.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cmtyx on 2017-08-02.
 */

public class AlcoholObject implements Parcelable {

    private String brandName ="";
    private double alcoholDegree =0.0;
    private String description= "";
    private String imgKey= "";
    //나의 키 값
    private String objectKey = "";

    //전체 리뷰 작성 수
    private int total = 0;
    private String sojuScoreKey = "";
    private String manScoreKey= "";
    private String womanScoreKey = "";

    public AlcoholObject() {

    }

    public AlcoholObject(String brandName, double alcoholDegree, String description, String imgKey, String objectKey) {
        this.brandName = brandName;
        this.alcoholDegree = alcoholDegree;
        this.description = description;
        this.imgKey = imgKey;
        this.objectKey = objectKey;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public double getAlcoholDegree() {
        return alcoholDegree;
    }

    public void setAlcoholDegree(double alcoholDegree) {
        this.alcoholDegree = alcoholDegree;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgKey() {
        return imgKey;
    }

    public void setImgKey(String imgKey) {
        this.imgKey = imgKey;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getSojuScoreKey() {
        return sojuScoreKey;
    }

    public void setSojuScoreKey(String sojuScoreKey) {
        this.sojuScoreKey = sojuScoreKey;
    }

    public String getManScoreKey() {
        return manScoreKey;
    }

    public void setManScoreKey(String manScoreKey) {
        this.manScoreKey = manScoreKey;
    }

    public String getWomanScoreKey() {
        return womanScoreKey;
    }

    public void setWomanScoreKey(String womanScoreKey) {
        this.womanScoreKey = womanScoreKey;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public AlcoholObject(String brandName, double alcoholDegree, String description, String imgKey, String objectKey, int total, String sojuScoreKey, String manScoreKey, String womanScoreKey) {
        this.brandName = brandName;
        this.alcoholDegree = alcoholDegree;
        this.description = description;
        this.imgKey = imgKey;
        this.objectKey = objectKey;
        this.total = total;
        this.sojuScoreKey = sojuScoreKey;
        this.manScoreKey = manScoreKey;
        this.womanScoreKey = womanScoreKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(brandName);
        dest.writeDouble(alcoholDegree);
        dest.writeString(description);
        dest.writeString(imgKey);
        dest.writeString(objectKey);
        dest.writeInt(total);
        dest.writeString(sojuScoreKey);
        dest.writeString(manScoreKey);
        dest.writeString(womanScoreKey);
    }

    private AlcoholObject(Parcel in) {
        brandName = in.readString();
        alcoholDegree = in.readDouble();
        description = in.readString();
        imgKey = in.readString();
        objectKey = in.readString();
        total = in.readInt();
        sojuScoreKey = in.readString();
        manScoreKey = in.readString();
        womanScoreKey = in.readString();
    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<AlcoholObject>(){

        @Override
        public AlcoholObject createFromParcel(Parcel source) {
            return new AlcoholObject(source);
        }

        @Override
        public AlcoholObject[] newArray(int size) {
            return new AlcoholObject[size];
        }
    };
}
