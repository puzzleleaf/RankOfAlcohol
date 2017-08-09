package com.tistory.puzzleleaf.rankofalcohol.util.mode;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by cmtyx on 2017-08-08.
 */

public class ModePreference {
    private Context context;

    public ModePreference(Context context){
        this.context = context;
    }

    //기본모드 - 1
    //메시지모드 - 2
    //미니게임 모드 - 3
    //전광판 모드 - 4

    public int getModePreferences(){
        SharedPreferences pref = context.getSharedPreferences("mode", MODE_PRIVATE);
        return pref.getInt("mode", 1);
    }

    public void saveModePreferences(int modeNum){
        SharedPreferences pref = context.getSharedPreferences("mode", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("mode", modeNum);
        editor.commit();
    }

    public void saveDisplayModeColorPreference(String color){
        SharedPreferences pref = context.getSharedPreferences("display", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("display", color);
        editor.commit();
    }

    public String getDisplayModeColorPreference(){
        SharedPreferences pref = context.getSharedPreferences("display",MODE_PRIVATE);
        return pref.getString("display","ffffff");
    }

    public void saveDisplayModeTextPreference(String text){
        SharedPreferences pref = context.getSharedPreferences("displayText", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("displayText", text);
        editor.commit();
    }

    public String getDisplayModeTextPreference(){
        SharedPreferences pref = context.getSharedPreferences("displayText",MODE_PRIVATE);
        return pref.getString("displayText","달과 별 그리고 술");
    }



}
