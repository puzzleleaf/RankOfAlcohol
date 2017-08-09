package com.tistory.puzzleleaf.rankofalcohol.animation;

import android.content.Context;
import android.util.Log;

import com.tistory.puzzleleaf.rankofalcohol.util.mode.ModePreference;

/**
 * Created by cmtyx on 2017-08-09.
 */

public class DisplayMode {

    private String text;
    private int rColor;
    private int gColor;
    private int bColor;
    private ModePreference modePreference;

    DisplayMode(Context context){
        rColor = 255;
        gColor = 255;
        bColor = 255;
        text = "";
        modePreference = new ModePreference(context);
    }

    void displayDataLoad(){
        setDisplayText(modePreference.getDisplayModeTextPreference());
        setDisplayColor(modePreference.getDisplayModeColorPreference());
    }

    void setDisplayColor(String color){
        Log.d("qwe",color);
        if(color.length()==6) {
            rColor = Integer.parseInt(color.substring(0, 2), 16);
            gColor = Integer.parseInt(color.substring(2, 4), 16);
            bColor = Integer.parseInt(color.substring(4), 16);
        }else{
            rColor = 255;
            gColor = 255;
            bColor = 255;
        }
    }

    public String getText() {
        return text;
    }

    void setDisplayText(String text){
        this.text = text;
    }


    public int getrColor() {
        return rColor;
    }

    public int getgColor() {
        return gColor;
    }

    public int getbColor() {
        return bColor;
    }


}
