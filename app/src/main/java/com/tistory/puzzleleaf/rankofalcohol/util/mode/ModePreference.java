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

}
