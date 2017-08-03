package com.tistory.puzzleleaf.rankofalcohol.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbAuth;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbDataBase;
import com.tistory.puzzleleaf.rankofalcohol.model.FbUser;



/**
 * Created by cmtyx on 2017-07-18.
 */

public class UserService extends IntentService {

    public UserService() {
        super("UserService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // 갱신 작업내용
        FbDataBase.database.getReference().child("User").child(FbAuth.mAuth.getCurrentUser().getUid()).child("info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FbAuth.mUser = dataSnapshot.getValue(FbUser.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
