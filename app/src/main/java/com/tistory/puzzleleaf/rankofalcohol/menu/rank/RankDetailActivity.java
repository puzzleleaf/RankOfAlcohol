package com.tistory.puzzleleaf.rankofalcohol.menu.rank;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.model.RankObject;
import com.tistory.puzzleleaf.rankofalcohol.menu.rank.adapter.RankRecyclerAdapter;
import com.tistory.puzzleleaf.rankofalcohol.util.sort.DescendingRating;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RankDetailActivity extends AppCompatActivity implements RankRecyclerAdapter.OnRankItemClickListener {

    private final int DATA_REFRERSH_CODE = 700;

    //recycler
    private LinearLayoutManager linearLayoutManager;
    private RankRecyclerAdapter rankRecyclerAdapter;
    private DescendingRating descendingRating;
    @BindView(R.id.rank_detail_recycler_view) RecyclerView rankDetailRecyclerView;
    @BindView(R.id.rank_detail_title) TextView rankDetailTitle;
    List<RankObject> rankObjectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_detail);
        ButterKnife.bind(this);
        recyclerInit();
    }

    private void recyclerInit(){
        rankObjectList = getIntent().getParcelableArrayListExtra("data");
        rankDetailTitle.setText(getIntent().getStringExtra("title"));
        rankRecyclerAdapter = new RankRecyclerAdapter(this, rankObjectList);
        rankRecyclerAdapter.setRankItemCallback(this);
        linearLayoutManager = new LinearLayoutManager(this);
        rankDetailRecyclerView.setLayoutManager(linearLayoutManager);
        rankDetailRecyclerView.setAdapter(rankRecyclerAdapter);

        descendingRating = new DescendingRating();
    }

    private void dataReLoad(){
        Collections.sort(rankObjectList,descendingRating);
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

    @OnClick(R.id.rank_detail_back)
    public void rankDetailBackClick(){
        onBackPressed();
    }
}
