package com.tistory.puzzleleaf.rankofalcohol.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cmtyx on 2017-08-02.
 */

public class RankObject implements Parcelable {

    private String brandName ="";
    private double alcoholDegree =0.0;
    private String description= "";
    private String imgKey= "";
    //나의 키 값
    private String objectKey = "";

    //전체 리뷰 작성 수
    private int total = 0;
    private double score = 0;

    public RankObject() {

    }

    public RankObject(String brandName, double alcoholDegree, String description, String imgKey, String objectKey) {
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

    public double getScore() {
        return score;
    }

    public void setScore(double Score) {
        this.score = Score;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public RankObject(String brandName, double alcoholDegree, String description, String imgKey, String objectKey, int total, double score) {
        this.brandName = brandName;
        this.alcoholDegree = alcoholDegree;
        this.description = description;
        this.imgKey = imgKey;
        this.objectKey = objectKey;
        this.total = total;
        this.score = score;
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
        dest.writeDouble(score);
    }


    protected RankObject(Parcel in) {
        brandName = in.readString();
        alcoholDegree = in.readDouble();
        description = in.readString();
        imgKey = in.readString();
        objectKey = in.readString();
        total = in.readInt();
        score = in.readDouble();
    }

    public static final Creator<RankObject> CREATOR = new Creator<RankObject>() {
        @Override
        public RankObject createFromParcel(Parcel in) {
            return new RankObject(in);
        }

        @Override
        public RankObject[] newArray(int size) {
            return new RankObject[size];
        }
    };




}
