package com.tistory.puzzleleaf.rankofalcohol.contents.setting;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.contents.battle.BattleActivity;
import com.tistory.puzzleleaf.rankofalcohol.contents.setting.dialog.SettingInfoDialog;
import com.tistory.puzzleleaf.rankofalcohol.contents.setting.dialog.SettingMessageDialog;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbAuth;
import com.tistory.puzzleleaf.rankofalcohol.util.mode.ModePreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingActivity extends AppCompatActivity implements SettingMessageDialog.OnApplyListener{

    private static final int MODE_BASIC = 1;
    private static final int MODE_CHAT = 2;
    private static final int MODE_GAME = 3;
    private static final int MODE_DISPLAY = 4;


    @BindView(R.id.setting_mode_menu) LinearLayout settingModeMenu;
    @BindView(R.id.setting_mode_message) Switch settingModeMessage;
    @BindView(R.id.setting_mode_game) Switch settingModeGame;
    @BindView(R.id.setting_mode_display) Switch settingModeDisplay;

    private View.OnClickListener settingMessageCancelClick;
    private SettingMessageDialog settingMessageDialog;
    private SettingInfoDialog settingInfoDialog;
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

        settingMessageCancelClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingModeMessage.setChecked(false);
                settingMessageDialog.dismiss();
            }
        };
        settingMessageDialog = new SettingMessageDialog(this,settingMessageCancelClick,this);
        settingInfoDialog = new SettingInfoDialog(this);


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


    public void settingSelectSwitch(View v){
        Switch sw = (Switch) v;
        if(!sw.isChecked()){
            modePreference.saveModePreferences(MODE_BASIC);
        }else{
            switchReset();
            sw.setChecked(true);
            if(sw == settingModeMessage){
                settingMessageDialog.show();
            }else if(sw == settingModeGame){
                modePreference.saveModePreferences(MODE_GAME);
            }else if(sw == settingModeDisplay){
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

    @OnClick(R.id.setting_logout)
    public void settingLogout(){
        FbAuth.mAuth.signOut();
        finish();
    }

    @OnClick(R.id.setting_private_info)
    public void settingPrivateInfoClick(){
        Intent intent = new Intent(this,SettingPrivateInfoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.library_info)
    public void settingLibraryInfoClick(){
        Intent intent = new Intent(this, SettingLibraryActivity.class);
        startActivity(intent);
    }

    public void settingInfoAnimationDialog(View v){
        switch (v.getId()){
            case R.id.setting_basic_info:
                settingInfoDialog.setSettingInfoCase(1);
                break;
            case R.id.setting_message_info :
                settingInfoDialog.setSettingInfoCase(2);
                break;
            case R.id.setting_game_info :
                settingInfoDialog.setSettingInfoCase(3);
                break;
            case R.id.setting_display_info :
                settingInfoDialog.setSettingInfoCase(4);
                break;
        }
        settingInfoDialog.show();
    }

    @OnClick(R.id.setting_developer_message)
    public void settingDeveloperMessage(){
        Intent intent = new Intent(this,SettingDeveloperMessageActivity.class);
        startActivity(intent);
    }


    @Override
    public void onApplySelected(String ch) {
        modePreference.saveModePreferences(MODE_CHAT);
        modePreference.saveMessageModeCh(ch);
        settingModeMessage.setChecked(true);
        settingMessageDialog.dismiss();
    }
}
