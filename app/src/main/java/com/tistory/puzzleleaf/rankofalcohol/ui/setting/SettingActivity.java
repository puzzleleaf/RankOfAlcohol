package com.tistory.puzzleleaf.rankofalcohol.ui.setting;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbAuth;
import com.tistory.puzzleleaf.rankofalcohol.service.ScreenLockService;
import com.tistory.puzzleleaf.rankofalcohol.util.mode.ModePreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingActivity extends AppCompatActivity{

    private static final int MODE_BASIC = 1;
    private static final int MODE_CHAT = 2;
    private static final int MODE_GAME = 3;
    private static final int MODE_DISPLAY = 4;
    private static final int MODE_SCREEN_LOCK = 5;


    @BindView(R.id.setting_mode_menu) LinearLayout settingModeMenu;
    @BindView(R.id.setting_code) TextView settingCode;
    @BindView(R.id.setting_mode_message) Switch settingModeMessage;
    @BindView(R.id.setting_mode_game) Switch settingModeGame;
    @BindView(R.id.setting_mode_display) Switch settingModeDisplay;
    @BindView(R.id.setting_mode_screen_lock) Switch settingModeScreenLock;

    private SettingMessageDialog settingMessageDialog;
    private ModePreference modePreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        dialogInit();
        preferenceInit();
        switchInit();
    }

    private void dialogInit(){
        settingMessageDialog = new SettingMessageDialog(this);

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
            case MODE_SCREEN_LOCK:
                settingModeScreenLock.setChecked(true);
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
            clearScreenLock(sw);
        }else{
            switchReset();
            sw.setChecked(true);
            if(sw == settingModeMessage){
                modePreference.saveModePreferences(MODE_CHAT);
                settingMessageDialog.show();
            }else if(sw == settingModeGame){
                modePreference.saveModePreferences(MODE_GAME);
            }else if(sw == settingModeDisplay){
                modePreference.saveModePreferences(MODE_DISPLAY);
                Intent intent = new Intent(this,SettingModeDisplay.class);
                startActivity(intent);
            }else{
                modePreference.saveScreenLockPreferences(true);
                setScreenLock();
            }
        }
    }

    private void setScreenLock(){
        startService(new Intent(this, ScreenLockService.class));
    }

    private void clearScreenLock(Switch sw){
        if(sw==settingModeScreenLock) {
            modePreference.saveScreenLockPreferences(false);
            stopService(new Intent(this, ScreenLockService.class));
        }
    }

    private void switchReset(){
        settingModeMessage.setChecked(false);
        settingModeDisplay.setChecked(false);
        settingModeGame.setChecked(false);
        modePreference.saveModePreferences(MODE_BASIC);
    }

    @OnClick(R.id.setting_logout)
    public void settingLogout(){
        FbAuth.mAuth.signOut();
        finish();
    }

}
