package com.tistory.puzzleleaf.rankofalcohol.contents.battle.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.contents.battle.adapter.BattleResultAdpater;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbDataBase;
import com.tistory.puzzleleaf.rankofalcohol.model.BattleObject;
import com.tistory.puzzleleaf.rankofalcohol.util.sort.AscendingTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cmtyx on 2017-08-10.
 */

public class BattleResultDialog extends Dialog{

    @BindView(R.id.battle_result_recycler) RecyclerView resultRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private BattleResultAdpater battleResultAdpater;
    private List<BattleObject> obj;
    private String channel;


    public BattleResultDialog(@NonNull Context context, String channel) {
         super(context);
         this.channel = channel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_result_dialog);
        ButterKnife.bind(this);
        this.setCanceledOnTouchOutside(false);


        recyclerViewInit();
        battleResultLoad();
    }

    private void recyclerViewInit(){
        obj = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getContext());
        battleResultAdpater = new BattleResultAdpater(getContext(),obj);
        resultRecyclerView.setLayoutManager(linearLayoutManager);
        resultRecyclerView.setAdapter(battleResultAdpater);
    }

    private void battleResultLoad(){

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                obj.clear();
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    BattleObject battleObject = iterator.next().getValue(BattleObject.class);
                    obj.add(battleObject);
                }
                battleTimeSort();
                battleResultAdpater.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        FbDataBase.database.getReference()
                .child("Battle")
                .child(channel)
                .addValueEventListener(valueEventListener);
    }

    private void battleTimeSort(){
        AscendingTime ascendingTime = new AscendingTime();
        Collections.sort(obj,ascendingTime);
    }

    @OnClick(R.id.battle_result_reset)
    public void battleResultReset(){
        FbDataBase.database.getReference()
                .child("Battle")
                .child(channel).removeValue();
    }

    @OnClick(R.id.battle_result_close)
    public void battleResultClose(){
        this.dismiss();
    }

}
