package com.tistory.puzzleleaf.rankofalcohol;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.tistory.puzzleleaf.rankofalcohol.util.screenlock.HomeKeyLocker;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ScreenLockActivity extends AppCompatActivity {

    final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;


    private static final int ANIMATION = 100;

    private HomeKeyLocker homeKeyLocker;
    private int progress =0;
    private int oldProgress =0;

    @BindView(R.id.progress) View screenLockProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_lock);
        overridePendingTransition(0,0);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        getWindow().getDecorView().setSystemUiVisibility(flags);
        homeKeyLocker = new HomeKeyLocker();
        screenLockProgress.getBackground().setLevel(0);

        //http://cloudylab.blogspot.kr/2015/02/android-full-screen.html 소프트 홈 사라지게
        //기존의 잠금화면이 있어야 menu가 막힌다.
        //Hard Home 버튼 막기
    }


    @Override
    protected void onResume() {
        super.onResume();
        homeKeyLocker.lock(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
         super.onWindowFocusChanged(hasFocus);
         getWindow().getDecorView().setSystemUiVisibility(flags);
    }

    @OnClick(R.id.temp)
    public void temp(){
        finish();
    }


    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
        homeKeyLocker.unlock();
    }

    @OnClick(R.id.progress)
    public void progressStart(){
        progress = screenLockProgress.getBackground().getLevel();
        oldProgress = progress;
        handler.sendEmptyMessageDelayed(ANIMATION, 100);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progress+=100;
            switch (msg.what) {
                case ANIMATION:
                    if(oldProgress>10000){
                        screenLockProgress.getBackground().setLevel(0);
                    }
                    else if (progress <= oldProgress+1500) {
                        screenLockProgress.getBackground().setLevel(progress);
                        sendEmptyMessageDelayed(ANIMATION, 100);
                    }
                    break;
            }

        }
    };
}
