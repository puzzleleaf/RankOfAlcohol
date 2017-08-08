package com.tistory.puzzleleaf.rankofalcohol.setting;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.util.mode.ModePreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingActivity extends AppCompatActivity {

    private static final int MODE_BASIC = 1;
    private static final int MODE_CHAT = 2;


    @BindView(R.id.setting_mode_menu) LinearLayout settingModeMenu;
    @BindView(R.id.setting_code) TextView settingCode;
    @BindView(R.id.setting_mode_message) Switch settingModeMessage;

    private ModePreference modePreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        modePreference = new ModePreference(this);
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

    @OnClick(R.id.setting_mode_message)
    public void settingModeMessage(){
        if(!settingModeMessage.isChecked()){
            modePreference.saveModePreferences(MODE_CHAT);
        }else{
            modePreference.saveModePreferences(MODE_BASIC);
        }
    }

}
