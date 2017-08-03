package com.tistory.puzzleleaf.rankofalcohol.model;

/**
 * Created by cmtyx on 2017-08-03.
 */

public class ReviewObject {
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
}
