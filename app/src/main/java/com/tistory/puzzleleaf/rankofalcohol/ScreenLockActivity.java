package com.tistory.puzzleleaf.rankofalcohol;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ScreenLockActivity extends AppCompatActivity {

    @BindView(R.id.screen_recycler) RecyclerView screenRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ScreenLockAdapter screenLockAdapter;
    private List<Integer> obj;

    final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;


    private static final int ANIMATION = 100;

    private int progress =0;
    private int oldProgress =0;

    private boolean progressLock = false;

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
        screenLockProgress.getBackground().setLevel(0);

        obj = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        screenLockAdapter = new ScreenLockAdapter(this,obj);
        screenRecyclerView.setLayoutManager(linearLayoutManager);
        screenRecyclerView.setAdapter(screenLockAdapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
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
    }

    @OnClick(R.id.progress)
    public void progressStart(){
        if(progressLock){
            return;
        }
        progressLock = true;
        progress = screenLockProgress.getBackground().getLevel();
        oldProgress = progress;
        handler.sendEmptyMessageDelayed(ANIMATION, 100);
    }

    private Handler updateHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progress+=100;
            if(oldProgress<10000) {
                if (progress <= oldProgress + 1500) {
                    screenLockProgress.getBackground().setLevel(progress);
                    updateHandler.sendEmptyMessageDelayed(ANIMATION, 100);
                }
            }
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ANIMATION:
                    if(oldProgress>=10000){
                        screenLockProgress.getBackground().setLevel(0);
                        obj.add(0);
                        screenLockAdapter.notifyDataSetChanged();
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressLock = false;
                            }
                        },500);
                    }
                    else if (progress <= oldProgress+1500) {
                        updateHandler.sendEmptyMessage(ANIMATION);
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressLock = false;
                                Log.d("qwe","UNLOCK");
                            }
                        },1500);
                    }
                    break;
            }

        }
    };
}
