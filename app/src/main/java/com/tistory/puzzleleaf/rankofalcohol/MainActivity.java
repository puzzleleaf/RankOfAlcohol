package com.tistory.puzzleleaf.rankofalcohol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.tistory.puzzleleaf.rankofalcohol.auth.FbAuth;
import com.tistory.puzzleleaf.rankofalcohol.progress.Loading;
import com.tistory.puzzleleaf.rankofalcohol.progress.LoadingDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FbAuth.mAuth = FirebaseAuth.getInstance();
        LoadingDialog.loading = new Loading(this);

        if(FbAuth.mAuth.getCurrentUser()==null){
            changeSignInActivity();
        }
    }


    private void changeSignInActivity(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }



}
