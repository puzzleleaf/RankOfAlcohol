package com.tistory.puzzleleaf.rankofalcohol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.tistory.puzzleleaf.rankofalcohol.auth.FbAuth;
import com.tistory.puzzleleaf.rankofalcohol.progress.Loading;
import com.tistory.puzzleleaf.rankofalcohol.progress.LoadingDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.testbtn) Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);

        FbAuth.mAuth = FirebaseAuth.getInstance();
        LoadingDialog.loading = new Loading(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    @OnClick(R.id.testbtn)
    public void test(){
        LoadingDialog.loadingShow();
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


}
