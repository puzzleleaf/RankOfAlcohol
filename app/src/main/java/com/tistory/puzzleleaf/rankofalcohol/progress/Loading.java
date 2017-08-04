package com.tistory.puzzleleaf.rankofalcohol.progress;

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
        if(select.equals("rank")) {
            setContentView(R.layout.loading_rank);
        }else{
            setContentView(R.layout.loading_write);
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
