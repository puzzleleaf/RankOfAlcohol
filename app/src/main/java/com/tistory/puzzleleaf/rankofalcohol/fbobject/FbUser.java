package com.tistory.puzzleleaf.rankofalcohol.fbobject;

/**
 * Created by cmtyx on 2017-07-27.
 */

public class FbUser {
    private float hMany;
    private String gender;

    public FbUser(){}

    public FbUser(float hMany, String gender) {
        this.hMany = hMany;
        this.gender = gender;
    }

    public float gethMany() {
        return hMany;
    }

    public void sethMany(float hMany) {
        this.hMany = hMany;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
