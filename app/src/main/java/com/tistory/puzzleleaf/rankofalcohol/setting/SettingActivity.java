package com.tistory.puzzleleaf.rankofalcohol.setting;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tistory.puzzleleaf.rankofalcohol.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.setting_mode_menu) LinearLayout settingModeMenu;
    @BindView(R.id.setting_code) TextView settingCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.setting_back)
    public void settingBackClick(){
        onBackPressed();
    }

    @OnClick(R.id.setting_mode)
    public void settingModeClick(){
        if(settingModeMenu.getVisibility() == View.GONE){
            settingModeMenu.setVisibility(View.VISIBLE);
        }else{
            settingModeMenu.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.setting_code_copy)
    public void settingCodeCopy(){
        ClipboardManager clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("code",settingCode.getText());
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(this,"복사되었습니다.",Toast.LENGTH_SHORT).show();
    }

}
