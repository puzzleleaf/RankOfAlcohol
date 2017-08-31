package com.tistory.puzzleleaf.rankofalcohol.contents.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbDataBase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingDeveloperMessageActivity extends AppCompatActivity {

    @BindView(R.id.setting_developer_edit) EditText settingDeveloperEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_developer_message);
        ButterKnife.bind(this);


    }

    private void sendMessage(){
        FbDataBase.database.getReference().child("ToDeveloper").push().setValue(settingDeveloperEdit.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(),"메시지가 성공적으로 전달되었습니다.",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @OnClick(R.id.setting_developer_back)
    public void settingDeveloperBack(){
        finish();
    }

    @OnClick(R.id.setting_developer_send)
    public void settingDeveloperSend(){
        if(settingDeveloperEdit.length()==0){
            Toast.makeText(getApplicationContext(),"내용을 입력 해주세요!",Toast.LENGTH_SHORT).show();
            return;
        }
        sendMessage();
    }


}
