package com.tistory.puzzleleaf.rankofalcohol.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbAuth;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbDataBase;
import com.tistory.puzzleleaf.rankofalcohol.model.FbUser;



/**
 * Created by cmtyx on 2017-07-18.
 */

public class UserService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        FbDataBase.database.getReference().child("User")
                .child(FbAuth.mAuth.getCurrentUser().getUid())
                .child("info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FbAuth.mUser = dataSnapshot.getValue(FbUser.class);
                stopService();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"네트워크 연결상태가 좋지 않습니다.",Toast.LENGTH_SHORT).show();
                stopService();
            }
        });
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //유저 로딩이 끝난 경우 서비스를 종료
    private void stopService(){
        this.stopSelf();
    }
}
