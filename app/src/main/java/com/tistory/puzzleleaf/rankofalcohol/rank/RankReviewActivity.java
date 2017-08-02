package com.tistory.puzzleleaf.rankofalcohol.rank;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.model.AlcoholObject;
import com.tistory.puzzleleaf.rankofalcohol.rank.adapter.RankRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RankReviewActivity extends AppCompatActivity {


    AlcoholObject alcoholObject;
    @BindView(R.id.rank_review_image) ImageView rankReviewImage;
    @BindView(R.id.rank_review_brand_name) TextView rankReviewBrandName;
    @BindView(R.id.rank_review_degree) TextView rankReviewDegree;

    //recycler
    @BindView(R.id.rank_review_recycler_view) RecyclerView rankReviewRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RankRecyclerAdapter rankRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_review);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        alcoholObject = intent.getParcelableExtra("data");

        rankReviewBrandName.setText(alcoholObject.getBrandName());
        rankReviewDegree.setText(String.valueOf(alcoholObject.getAlcoholDegree()));
        Glide.with(this).load(alcoholObject.getImgKey()).into(rankReviewImage);

        //임시
        List<AlcoholObject> alcoholObjectsList = new ArrayList<>();
        alcoholObjectsList.add(alcoholObject);
        rankRecyclerAdapter = new RankRecyclerAdapter(this, alcoholObjectsList);
        linearLayoutManager = new LinearLayoutManager(this);
        rankReviewRecyclerView.setLayoutManager(linearLayoutManager);
        rankReviewRecyclerView.setAdapter(rankRecyclerAdapter);






    }
}
