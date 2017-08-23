package com.tistory.puzzleleaf.rankofalcohol.util.progress;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.tistory.puzzleleaf.rankofalcohol.R;


public class Loading extends Dialog {

    public Loading(Context context,String select)
    {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCanceledOnTouchOutside(false);
        loadingModeInit(select);
    }

    private void loadingModeInit(String mode){
        switch (mode){
            case "rank" :
                setContentView(R.layout.loading_rank);
                break;
            case "analysis":
                setContentView(R.layout.loading_analysis);
                break;
            case "gallery":
                setContentView(R.layout.loading_gallery);
                break;
            case "login":
                setContentView(R.layout.loading_login);
                break;
            default:
                setContentView(R.layout.loading_write);
        }

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

}
