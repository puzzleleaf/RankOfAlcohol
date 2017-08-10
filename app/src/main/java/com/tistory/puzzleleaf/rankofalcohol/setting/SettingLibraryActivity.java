package com.tistory.puzzleleaf.rankofalcohol.setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbAuth;

import butterknife.ButterKnife;


public class SettingLibraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);


    }


}
