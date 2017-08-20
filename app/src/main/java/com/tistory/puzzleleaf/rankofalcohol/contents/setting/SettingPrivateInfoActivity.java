package com.tistory.puzzleleaf.rankofalcohol.contents.setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tistory.puzzleleaf.rankofalcohol.R;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingPrivateInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_private_info);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.setting_private_back)
    public void settingPrivateBackClick(){
        finish();
    }



}
