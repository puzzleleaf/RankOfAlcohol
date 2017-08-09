package com.tistory.puzzleleaf.rankofalcohol.animation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbDataBase;

/**
 * Created by cmtyx on 2017-08-09.
 */

class ChatMode {

    interface OnChatMessageListener{
        void onChatValueListener(String value);
    }

    private OnChatMessageListener onChatMessageListener;
    private ValueEventListener chatValueEventListener;

     ChatMode(OnChatMessageListener onChatMessageListener){
        this.onChatMessageListener = onChatMessageListener;
    }

    void chatModeListenerInit(){
        chatValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                if (value!=null) {
                    onChatMessageListener.onChatValueListener(value);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    void dataLoadInit(){
        FbDataBase.database.getReference().child("Channel").child("user-key").addValueEventListener(chatValueEventListener);
    }

    void dataLoadRemove(){
        FbDataBase.database.getReference().child("Channel").child("user-key").removeEventListener(chatValueEventListener);
    }


}
