package com.tistory.puzzleleaf.rankofalcohol;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tistory.puzzleleaf.rankofalcohol.animation.MainAnimation;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbAuth;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbDataBase;
import com.tistory.puzzleleaf.rankofalcohol.contents.analysis.AnalysisActivity;
import com.tistory.puzzleleaf.rankofalcohol.contents.gallery.GalleryActivity;
import com.tistory.puzzleleaf.rankofalcohol.contents.rank.RankActivity;
import com.tistory.puzzleleaf.rankofalcohol.service.UserService;
import com.tistory.puzzleleaf.rankofalcohol.contents.setting.SettingActivity;
import com.tistory.puzzleleaf.rankofalcohol.util.edit.ChatEditText;
import com.tistory.puzzleleaf.rankofalcohol.util.mode.ModePreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


public class MainActivity extends AppCompatActivity {

    private static final int MODE_BASIC = 1;
    private static final int MODE_CHAT = 2;
    private static final int MODE_GAME = 3;
    private static final int MODE_DISPLAY = 4;
    private static final String MODE = "MODE";

    @BindView(R.id.main_chat_mode) ImageView mainChatMode;
    @BindView(R.id.main_chat_layout) LinearLayout mainChatLayout;
    @BindView(R.id.main_chat_edit) ChatEditText mainChatEdit;
    @BindView(R.id.main_setting) ImageView mainSetting;
    @BindView(R.id.main_rank) LinearLayout mainRank;
    @BindView(R.id.main_analysis) LinearLayout mainAnalysis;
    @BindView(R.id.main_gallery) LinearLayout mainGallery;

    private InputMethodManager imm;
    private ModePreference modePreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        showCase();

        init();
        userInit();
        playAnimation();
        chatModeKeyEventInit();



    }


    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
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
                mainChatMode.setVisibility(View.GONE);
                showMenu();
                break;
            case MODE_CHAT:
                mainChatMode.setVisibility(View.VISIBLE);
                showMenu();
                break;
            case MODE_GAME:
                mainChatMode.setVisibility(View.GONE);
                hideMenu();
                break;
            case MODE_DISPLAY:
                mainChatMode.setVisibility(View.GONE);
                hideMenu();
                break;
        }
        sendBroadcast(new Intent(MODE).putExtra("mode",mode));
    }

    private void checkLogin(){
        if(FbAuth.mAuth.getCurrentUser() ==null){
            Toast.makeText(this,"로그아웃 되었습니다.",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void hideMenu(){
        mainRank.setVisibility(View.GONE);
        mainAnalysis.setVisibility(View.GONE);
        mainGallery.setVisibility(View.GONE);
    }

    private void showMenu(){
        mainRank.setVisibility(View.VISIBLE);
        mainAnalysis.setVisibility(View.VISIBLE);
        mainGallery.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.main_analysis)
    public void analysisMenu(){
        if(FbAuth.mUser == null){
            Toast.makeText(this,"계정 정보를 불러오는 중 입니다.",Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(this, AnalysisActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }

    @OnClick(R.id.main_gallery)
    public void galleryMenu(){
        Intent intent = new Intent(this, GalleryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        hideMenu();
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
    }

    private void hideChat(){
        mainChatLayout.setVisibility(View.GONE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void sendMessage(){
        FbDataBase.database.getReference().child("Channel").child("user-key").setValue(mainChatEdit.getText().toString());
        mainChatEdit.setText("");
    }

    private void showCase(){
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, "v");
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(0);
        config.setContentTextColor(ContextCompat.getColor(this,R.color.colorWhite));
        config.setMaskColor(ContextCompat.getColor(this,R.color.showCase));
        config.setDismissTextColor(ContextCompat.getColor(this,R.color.materialBlue));
        sequence.setConfig(config);

        sequence.addSequenceItem(mainChatEdit,
                "메인 애니메이션 창 입니다.\n\n리뷰를 작성하면 애니메이션 효과가 달라지게 됩니다.","확인");

        sequence.addSequenceItem(mainRank,
                "랭킹 메뉴 입니다.\n\n술 랭킹을 확인할 수 있고 리뷰를 작성할 수 있습니다.", "확인");

        sequence.addSequenceItem(mainAnalysis,
                "분석 메뉴 입니다.\n\n내가 마신 술의 양을 등록하고 통계 데이터를 제공합니다.", "확인");

        sequence.addSequenceItem(mainGallery,
                "갤러리 메뉴 입니다.\n\n내가 리뷰를 남긴 술 목록을 확인할 수 있습니다.", "확인");

        sequence.addSequenceItem(mainSetting,
                "설정 메뉴 입니다.\n\n게임과 채팅 등 여러가지 모드를 설정할 수 있습니다.","확인");
        sequence.start();
    }


}
