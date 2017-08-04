package com.tistory.puzzleleaf.rankofalcohol.rank;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.model.RankObject;
import com.tistory.puzzleleaf.rankofalcohol.rank.adapter.RankRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RankDetailActivity extends AppCompatActivity implements RankRecyclerAdapter.OnRankItemClickListener {

    private final int DATA_REFRERSH_CODE = 700;

    //recycler
    private LinearLayoutManager linearLayoutManager;
    private RankRecyclerAdapter rankRecyclerAdapter;
    @BindView(R.id.rank_detail_recycler_view) RecyclerView rankDetailRecyclerView;
    List<RankObject> rankObjectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        rankObjectList = intent.getParcelableArrayListExtra("data");
        //Recycler
        rankRecyclerAdapter = new RankRecyclerAdapter(this, rankObjectList);
        rankRecyclerAdapter.setRankItemCallback(this);
        linearLayoutManager = new LinearLayoutManager(this);
        rankDetailRecyclerView.setLayoutManager(linearLayoutManager);
        rankDetailRecyclerView.setAdapter(rankRecyclerAdapter);

    }
    private void dataReLoad(){
        rankRecyclerAdapter.notifyDataSetChanged();
    }

    //어댑터에서 호출한 startActivityForResult 값 돌려 받기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == DATA_REFRERSH_CODE){
            RankObject rankObject = data.getParcelableExtra("data");
            if(rankObject!=null) {
                rankObjectList.set(requestCode, rankObject);
                dataReLoad();
            }
        }
    }

    @Override
    public void onRankItemSelected(int position) {
        Intent intent = new Intent(this, RankReviewActivity.class);
        intent.putExtra("data",rankObjectList.get(position));
        startActivityForResult(intent,position);
    }
}
