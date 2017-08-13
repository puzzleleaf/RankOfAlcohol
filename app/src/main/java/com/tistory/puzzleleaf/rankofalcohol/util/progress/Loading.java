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
        if(select.equals("rank")) {
            setContentView(R.layout.loading_rank);
        }else if(select.equals("analysis")){
            setContentView(R.layout.loading_analysis);
        } else{
            setContentView(R.layout.loading_write);
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
