package com.tistory.puzzleleaf.rankofalcohol.contents.analysis;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbAuth;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbDataBase;
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
    private String analysisDataKey;
    private OnRegisterClickListener onRegisterClickListener;

     AnalysisRegisterDialog(@NonNull Context context, OnRegisterClickListener onRegisterClickListener) {
         super(context);
         this.onRegisterClickListener = onRegisterClickListener;
    }

    @BindView(R.id.analysis_register) TextView analysisRegister;
    @BindView(R.id.analysis_soju) EditText analysisSoju;
    @BindView(R.id.analysis_maekju) EditText analysisMaekju;
    @BindView(R.id.analysis_makgeolli) EditText analysisMakgeolli;
    @BindView(R.id.analysis_etc) EditText analysisEtc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_register_dialog);
        ButterKnife.bind(this);

        this.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataReset();
        dataLoad();
    }

    AnalysisValueObject analysisRegisterValue(){
        float soju = numberFormatExceptionPrevention(analysisSoju.getText().toString());
        float maekju = numberFormatExceptionPrevention(analysisMaekju.getText().toString())/500*(float)0.3;
        float makgeolli = numberFormatExceptionPrevention(analysisMakgeolli.getText().toString())*(float)0.6;
        float etc = numberFormatExceptionPrevention(analysisEtc.getText().toString())*(float)2;
        AnalysisValueObject analysisValueObject = new AnalysisValueObject(soju,maekju,makgeolli,etc);

        dataReset();
        return analysisValueObject;
    }

    void setDay(int day){
        this.day = day;
    }

    void setAnalysisDataKey(String analysisDataKey){
        this.analysisDataKey = analysisDataKey;
    }

    private void dataLoad(){
        FbDataBase.database.getReference()
                .child("Analysis")
                .child(FbAuth.getGoogleUserId())
                .child(analysisDataKey).child(String.valueOf(day))
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AnalysisValueObject analysisValueObject = dataSnapshot.getValue(AnalysisValueObject.class);
                if(analysisValueObject!=null){
                    dataLoadSet(analysisValueObject);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void dataLoadSet(AnalysisValueObject analysisValueObject){
        analysisMaekju.setText(dataLoadChange(analysisValueObject.getBeer(),2));
        analysisSoju.setText(dataLoadChange(analysisValueObject.getSoju(),1));
        analysisMakgeolli.setText(dataLoadChange(analysisValueObject.getMakgeolli(),3));
        analysisEtc.setText(dataLoadChange(analysisValueObject.getEtc(),4));
    }

    private String dataLoadChange(float val, int num){
        if(val==0){
            return "";
        }
        switch (num){
            case 1:
                //소주
                return String.format("%.2f",val);
            case 2:
                //맥주
                return String.format("%.2f",val/(float)0.3*500);
            case 3:
                //막걸리
                return String.format("%.2f",val/(float)0.6);
            default:
                //기타
                return String.format("%.2f",val/2);
        }
    }

    private void dataReset(){
        analysisMaekju.setText("");
        analysisSoju.setText("");
        analysisMakgeolli.setText("");
        analysisEtc.setText("");
    }


    private float numberFormatExceptionPrevention(String check){
        if(check.equals("") || check.equals(".")){
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
