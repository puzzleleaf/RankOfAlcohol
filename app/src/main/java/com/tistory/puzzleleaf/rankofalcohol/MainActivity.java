package com.tistory.puzzleleaf.rankofalcohol;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;


import com.tistory.puzzleleaf.rankofalcohol.animation.MainAnimation;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbAuth;
import com.tistory.puzzleleaf.rankofalcohol.rank.RankActivity;
import com.tistory.puzzleleaf.rankofalcohol.service.UserService;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);

        userInit();
        playAnimation();
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

}
