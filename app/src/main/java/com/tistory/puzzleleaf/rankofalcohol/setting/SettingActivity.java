package com.tistory.puzzleleaf.rankofalcohol.setting;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    private static final int MODE_GAME = 3;
    private static final int MODE_DISPLAY = 4;


    @BindView(R.id.setting_mode_menu) LinearLayout settingModeMenu;
    @BindView(R.id.setting_code) TextView settingCode;
    @BindView(R.id.setting_mode_message) Switch settingModeMessage;
    @BindView(R.id.setting_mode_game) Switch settingModeGame;
    @BindView(R.id.setting_mode_display) Switch settingModeDisplay;

    private ModePreference modePreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        preferenceInit();
        switchInit();
    }

    private void preferenceInit(){
        modePreference = new ModePreference(this);
    }

    private void switchInit(){
        int check = modePreference.getModePreferences();
        switch (check){
            case MODE_CHAT:
                settingModeMessage.setChecked(true);
                break;
            case MODE_GAME:
                settingModeGame.setChecked(true);
                break;
            case MODE_DISPLAY:
                settingModeDisplay.setChecked(true);
                break;
        }
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
        Toast.makeText(this,"복사 되었습니다.",Toast.LENGTH_SHORT).show();
    }


    public void settingSelectSwitch(View v){
        Switch sw = (Switch) v;
        if(!sw.isChecked()){
            modePreference.saveModePreferences(MODE_BASIC);
        }else{
            switchReset();
            sw.setChecked(true);
            if(sw == settingModeMessage){
                modePreference.saveModePreferences(MODE_CHAT);
            }else if(sw == settingModeGame){
                modePreference.saveModePreferences(MODE_GAME);
            }else{
                modePreference.saveModePreferences(MODE_DISPLAY);
                Intent intent = new Intent(this,SettingModeDisplay.class);
                startActivity(intent);
            }
        }
    }

    private void switchReset(){
        settingModeMessage.setChecked(false);
        settingModeDisplay.setChecked(false);
        settingModeGame.setChecked(false);
        modePreference.saveModePreferences(MODE_BASIC);
    }


}
