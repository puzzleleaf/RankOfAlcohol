package com.tistory.puzzleleaf.rankofalcohol.progress;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.tistory.puzzleleaf.rankofalcohol.R;


public class Loading extends Dialog {

    public Loading(Context context)
    {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_loading);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
