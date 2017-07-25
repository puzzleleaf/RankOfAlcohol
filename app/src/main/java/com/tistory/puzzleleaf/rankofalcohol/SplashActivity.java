package com.tistory.puzzleleaf.rankofalcohol;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.tistory.puzzleleaf.rankofalcohol.auth.FbAuth;
import com.tistory.puzzleleaf.rankofalcohol.progress.Loading;
import com.tistory.puzzleleaf.rankofalcohol.progress.LoadingDialog;


public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DELAY_TIME = 3000;

    final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            changeActivity();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler.sendEmptyMessageDelayed(0,SPLASH_DELAY_TIME);
    }

    private void changeActivity(){
        changeMainActivity();
        finish();
    }

    private void changeMainActivity(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }



}
