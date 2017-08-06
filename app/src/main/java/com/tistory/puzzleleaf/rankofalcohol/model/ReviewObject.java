package com.tistory.puzzleleaf.rankofalcohol.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cmtyx on 2017-08-03.
 */

public class ReviewObject implements Parcelable {
    private String nickName;
    private int rating;
    private String howMany;
    private String contents1;
    private String contents2;

    private String date;

    private double userHowMany;
    private String reviewKey;
    private String userKey;

    public ReviewObject(){

    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getHowMany() {
        return howMany;
    }

    public void setHowMany(String howMany) {
        this.howMany = howMany;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getUserHowMany() {
        return userHowMany;
    }

    public void setUserHowMany(double userHowMany) {
        this.userHowMany = userHowMany;
    }

    public String getReviewKey() {
        return reviewKey;
    }

    public void setReviewKey(String reviewKey) {
        this.reviewKey = reviewKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getContents1() {
        return contents1;
    }

    public void setContents1(String contents1) {
        this.contents1 = contents1;
    }

    public String getContents2() {
        return contents2;
    }

    public void setContents2(String contents2) {
        this.contents2 = contents2;
    }

    public ReviewObject(String nickName, int rating, String howMany, String contents1, String contents2, String date, double userHowMany, String reviewKey, String userKey) {
        this.nickName = nickName;
        this.rating = rating;
        this.howMany = howMany;
        this.contents1 = contents1;
        this.contents2 = contents2;
        this.date = date;

        this.userHowMany = userHowMany;
        this.reviewKey = reviewKey;
        this.userKey = userKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nickName);
        dest.writeInt(rating);
        dest.writeString(howMany);
        dest.writeString(contents1);
        dest.writeString(contents2);
        dest.writeString(date);
        dest.writeDouble(userHowMany);
        dest.writeString(reviewKey);
        dest.writeString(userKey);
    }

    public static final Creator<ReviewObject> CREATOR = new Creator<ReviewObject>() {
        @Override
        public ReviewObject createFromParcel(Parcel in) {
            return new ReviewObject(in);
        }

        @Override
        public ReviewObject[] newArray(int size) {
            return new ReviewObject[size];
        }
    };

    protected ReviewObject(Parcel in) {
        nickName = in.readString();
        rating = in.readInt();
        howMany = in.readString();
        contents1 = in.readString();
        contents2 = in.readString();
        date = in.readString();
        userHowMany = in.readDouble();
        reviewKey = in.readString();
        userKey = in.readString();
    }

}
