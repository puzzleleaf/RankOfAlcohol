package com.tistory.puzzleleaf.rankofalcohol;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.tistory.puzzleleaf.rankofalcohol.animation.MainAnimation;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbDataBase;
import com.tistory.puzzleleaf.rankofalcohol.rank.RankActivity;
import com.tistory.puzzleleaf.rankofalcohol.service.UserService;
import com.tistory.puzzleleaf.rankofalcohol.setting.SettingActivity;
import com.tistory.puzzleleaf.rankofalcohol.util.edit.ChatEditText;
import com.tistory.puzzleleaf.rankofalcohol.util.mode.ModePreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    private static final int MODE_BASIC = 1;
    private static final int MODE_CHAT = 2;

    @BindView(R.id.main_chat_layout) LinearLayout mainChatLayout;
    @BindView(R.id.main_chat_edit) ChatEditText mainChatEdit;
    @BindView(R.id.main_setting) ImageView mainSetting;

    private InputMethodManager imm;
    private ModePreference modePreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
        userInit();
        playAnimation();
        chatModeKeyEventInit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        modeCheck(modePreference.getModePreferences());
    }

    private void chatModeKeyEventInit(){
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mainChatEdit.setKeyImeChangeListener(new ChatEditText.KeyImeChange() {
            @Override
            public void onKeyIme(int keyCode, KeyEvent event) {
                if(KeyEvent.KEYCODE_BACK == event.getKeyCode()){
                    hideChat();
                }
            }
        });
    }

    private void playAnimation(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_animation, new MainAnimation());
        fragmentTransaction.commit();
    }

    private void userInit(){
        Intent intent = new Intent(this, UserService.class);
        startService(intent);
    }

    private void init(){
        modePreference = new ModePreference(this);
    }

    private void modeCheck(int mode){
        switch (mode){
            case MODE_BASIC:
            case MODE_CHAT:
                break;
        }
    }


    @Override
    public void onBackPressed() {
        //홈으로 보낸다.
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
    @OnClick(R.id.main_rank)
    public void rankMenu(){
        Intent intent = new Intent(this, RankActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

    }
    @OnClick(R.id.main_setting)
    public void settingMenu(){
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.main_chat_mode)
    public void chatMode(){
        if(mainChatLayout.getVisibility() == View.VISIBLE){
            hideChat();
        }else {
            mainChatLayout.setVisibility(View.VISIBLE);
            mainChatEdit.requestFocus();
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }
    }

    @OnClick(R.id.main_chat_send)
    public void chatSend(){
        sendMessage();
        hideChat();
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void hideChat(){
        mainChatLayout.setVisibility(View.GONE);
    }

    private void sendMessage(){
        FbDataBase.database.getReference().child("Channel").child("user-key").setValue(mainChatEdit.getText().toString());
        mainChatEdit.setText("");
    }


//    @OnClick(R.id.star_click)
//    public void rankTemp(){
//        Intent intent = new Intent("Star");
//        intent.putExtra("star",starEdit.getText().toString());
//        sendBroadcast(intent);
//    }

}
