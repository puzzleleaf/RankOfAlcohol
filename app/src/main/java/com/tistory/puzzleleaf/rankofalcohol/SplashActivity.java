package com.tistory.puzzleleaf.rankofalcohol;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbAuth;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DELAY_TIME = 3000;


    @BindView(R.id.splash) ImageView splash;
    @BindView(R.id.splash_logo) ImageView splashLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        imageLoading();
        handler.sendEmptyMessageDelayed(0,SPLASH_DELAY_TIME);
    }

    final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            changeActivity();
        }
    };

    private void imageLoading(){
        Glide.with(this).load(R.drawable.splash).into(splash);
        Glide.with(this).load(R.drawable.puzzleleaf).into(splashLogo);
    }

    private void changeActivity(){
        if(FbAuth.mAuth ==null){
            changeSignInActivity();
        }else {
            changeMainActivity();
        }
    }

    private void changeMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void changeSignInActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
