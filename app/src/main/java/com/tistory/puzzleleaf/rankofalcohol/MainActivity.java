package com.tistory.puzzleleaf.rankofalcohol;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;


import com.tistory.puzzleleaf.rankofalcohol.animation.MainAnimation;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbAuth;

import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);

        checkLogin();

        playAnimation();

    }

    private void playAnimation(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_animation, new MainAnimation());
        fragmentTransaction.commit();
    }


    private void checkLogin(){
        if(FbAuth.mAuth.getCurrentUser()==null){
            changeSignInActivity();
        }
    }

    private void changeSignInActivity(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        //홈으로 보낸다.
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);

    }

}
