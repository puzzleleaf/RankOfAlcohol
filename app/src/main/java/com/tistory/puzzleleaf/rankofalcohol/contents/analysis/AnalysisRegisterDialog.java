package com.tistory.puzzleleaf.rankofalcohol.contents.analysis;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.TextView;

import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.model.AnalysisValueObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cmtyx on 2017-08-10.
 */

class AnalysisRegisterDialog extends Dialog{

    interface OnRegisterClickListener{
        void onRegisterClick(int day);
    }


    private int day;
    private OnRegisterClickListener onRegisterClickListener;

     AnalysisRegisterDialog(@NonNull Context context, OnRegisterClickListener onRegisterClickListener) {
         super(context);
         this.onRegisterClickListener = onRegisterClickListener;
    }

    @BindView(R.id.analysis_register) TextView analysisRegister;
    @BindView(R.id.analysis_soju) EditText analysisSoju;
    @BindView(R.id.analysis_maekju) EditText analysisMaekju;
    @BindView(R.id.analysis_makgeolli) EditText analysisMakgeolli;
    @BindView(R.id.analysis_yangju) EditText analysisYangju;
    @BindView(R.id.analysis_wine) EditText analysisWine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_register_dialog);
        ButterKnife.bind(this);

        this.setCanceledOnTouchOutside(false);
    }

    AnalysisValueObject analysisRegisterValue(){
        float soju = numberFormatExceptionPrevention(analysisSoju.getText().toString());
        float maekju = numberFormatExceptionPrevention(analysisMaekju.getText().toString())/500*(float)0.3;
        float makgeolli = numberFormatExceptionPrevention(analysisMakgeolli.getText().toString())*(float)0.6;
        float yangju = numberFormatExceptionPrevention(analysisYangju.getText().toString())*(float)2;
        float wine = numberFormatExceptionPrevention(analysisWine.getText().toString())*(float)1.3;
        AnalysisValueObject analysisValueObject = new AnalysisValueObject(soju,maekju,makgeolli,yangju+wine);

        dataReset();
        return analysisValueObject;
    }

    void setDay(int day){
        this.day = day;
    }

    private void dataReset(){
        analysisMaekju.setText("");
        analysisSoju.setText("");
        analysisMakgeolli.setText("");
        analysisYangju.setText("");
        analysisWine.setText("");
    }


    private float numberFormatExceptionPrevention(String check){
        if(check.equals("")){
            return 0;
        }else{
            return Float.parseFloat(check);
        }
    }

    @OnClick(R.id.analysis_register_cancel)
    public void analysisRegisterCancel(){
        onBackPressed();

    }
    @OnClick(R.id.analysis_register)
    public void analysisRegisterClick(){
        onRegisterClickListener.onRegisterClick(day);
        onBackPressed();
    }


}
