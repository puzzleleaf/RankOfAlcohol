package com.tistory.puzzleleaf.rankofalcohol.animation;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbDataBase;
import com.tistory.puzzleleaf.rankofalcohol.util.mode.ModePreference;

/**
 * Created by cmtyx on 2017-08-09.
 */

class ChatMode {

    interface OnChatMessageListener{
        void onChatValueListener(String value);
    }

    private OnChatMessageListener onChatMessageListener;
    private ValueEventListener chatValueEventListener;
    private ModePreference modePreference;

     ChatMode(Context context,OnChatMessageListener onChatMessageListener){
         modePreference = new ModePreference(context);
        this.onChatMessageListener = onChatMessageListener;
    }

    void chatModeListenerInit(){
        chatValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                if (value==null) {
                    value = "";
                }
                onChatMessageListener.onChatValueListener(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    void dataLoadInit(){
        FbDataBase.database.getReference().child("Channel").child(modePreference.getMessageModeCh()).addValueEventListener(chatValueEventListener);
    }

    void dataLoadRemove(){
        FbDataBase.database.getReference().child("Channel").child(modePreference.getMessageModeCh()).removeEventListener(chatValueEventListener);
    }


}
