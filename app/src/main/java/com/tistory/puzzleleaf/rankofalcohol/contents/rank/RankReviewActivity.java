package com.tistory.puzzleleaf.rankofalcohol.contents.rank;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbAuth;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbDataBase;
import com.tistory.puzzleleaf.rankofalcohol.model.RankObject;
import com.tistory.puzzleleaf.rankofalcohol.model.RatingObject;
import com.tistory.puzzleleaf.rankofalcohol.model.ReviewObject;
import com.tistory.puzzleleaf.rankofalcohol.util.progress.Loading;
import com.tistory.puzzleleaf.rankofalcohol.contents.rank.adapter.RankReviewAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RankReviewActivity extends AppCompatActivity implements RankReviewAdapter.OnRankReviewClickListener {

    @BindView(R.id.rank_review_image) ImageView rankReviewImage;
    @BindView(R.id.rank_review_brand_name) TextView rankReviewBrandName;
    @BindView(R.id.rank_review_degree) TextView rankReviewDegree;
    @BindView(R.id.rank_review_rating) TextView rankReviewRating;
    @BindView(R.id.rank_review_num) TextView rankReviewNum;
    @BindView(R.id.rank_review_rating_bar) RatingBar rankReviewRatingBar;
    @BindView(R.id.rank_review_register_btn) Button rankReviewRegisterBtn;
    @BindView(R.id.rank_review_description) TextView rankReviewDescription;


    private final int DATA_REFRERSH_CODE = 700;
    //Data
    private RankObject rankObject;
    private List<ReviewObject> reviewObjectList;
    //loading
    private Loading loading;
    //userWritingCheck
    private boolean isUserWriting = false;

    //recycler
    @BindView(R.id.rank_review_recycler_view) RecyclerView rankReviewRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RankReviewAdapter rankReviewAdapter;

    //reload
    private boolean isReLoad = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_review);
        ButterKnife.bind(this);

        dataInit();
        recyclerInit();
        rankReviewRatingBarInit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isUserReviewWriting();
        reviewDataLoad();
        rankObjectReLoad();
    }

    private void isUserReviewWriting(){
        FbDataBase.database.getReference().child("User").child(FbAuth.mAuth.getCurrentUser().getUid()).child("review").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    if(iterator.next().getValue(String.class).equals(rankObject.getObjectKey())){
                        isUserWriting = true;
                        break;
                    }
                }
                userReviewBtnSet();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void userReviewBtnSet(){
        if(!isUserWriting) {
            rankReviewRegisterBtn.setVisibility(View.VISIBLE);
        }else{
            rankReviewRegisterBtn.setVisibility(View.INVISIBLE);
        }
    }


    //평점 데이터 실시간 반영
    private void rankObjectReLoad(){
        if(isReLoad) {
            FbDataBase.database.getReference("Rating").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    RatingObject ratingObject = dataSnapshot.child(rankObject.getObjectKey()).getValue(RatingObject.class);
                    if(ratingObject!=null) {
                        double result = (double) ratingObject.getTotal() / ratingObject.getNum();
                        rankObject.setTotal(ratingObject.getNum());
                        rankObject.setScore(result);
                        rankObjectRatingReLoad();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void reviewDataLoad(){
        loading.show();
        reviewObjectList.clear();

        FbDataBase.database.getReference("Review").child(rankObject.getObjectKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    ReviewObject rankObject = iterator.next().getValue(ReviewObject.class);
                    reviewObjectList.add(0,rankObject);
                }
                resultDataUpdate();
                loading.dismiss();
                rankReviewAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //@TODO 데이터 갱신 예외처리 할 것
            }
        });
    }

    //리뷰 데이터 표기
    private void recyclerInit(){
        reviewObjectList = new ArrayList<>();
        rankReviewAdapter = new RankReviewAdapter(this,reviewObjectList);
        rankReviewAdapter.setRankReviewCallback(this);
        linearLayoutManager = new LinearLayoutManager(this);
        rankReviewRecyclerView.setLayoutManager(linearLayoutManager);
        rankReviewRecyclerView.setAdapter(rankReviewAdapter);
    }

    //술 데이터 표기
    private void dataInit(){
        Intent intent = getIntent();
        rankObject = intent.getParcelableExtra("data");

        loading = new Loading(this,"rank");

        rankReviewBrandName.setText(rankObject.getBrandName());
        rankReviewNum.setText(String.valueOf(rankObject.getTotal()));
        Glide.with(this)
                .load(rankObject.getImgKey())
                .apply(new RequestOptions().placeholder(R.drawable.image_loading))
                .into(rankReviewImage);
        rankReviewRating.setText(String.format("%.2f",rankObject.getScore()));
        rankReviewDegree.setText(String.valueOf(rankObject.getAlcoholDegree()));
        rankReviewRatingBar.setRating(Float.parseFloat(String.valueOf(rankObject.getScore())));
        rankReviewDescription.setText(rankObject.getDescription());
    }

    private void rankObjectRatingReLoad(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rankReviewRating.setText(String.format("%.2f",rankObject.getScore()));
                rankReviewNum.setText(String.valueOf(rankObject.getTotal()));
                rankReviewRatingBar.setRating(Float.parseFloat(String.valueOf(rankObject.getScore())));
            }
        });
    }

    @OnClick(R.id.rank_review_register_btn)
    public void rankReviewRegisterClick(){
        Intent intent = new Intent(this,RankReviewRegisterActivity.class);
        intent.putExtra("data", rankObject);
        startActivity(intent);
    }

    private void rankReviewRatingBarInit(){
        LayerDrawable stars = (LayerDrawable)rankReviewRatingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(this,R.color.colorStar), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
    }

    private void resultDataUpdate(){
        Intent intent = new Intent();
        intent.putExtra("data",rankObject);
        setResult(DATA_REFRERSH_CODE,intent);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(!isReLoad){
            isReLoad = !isReLoad;
        }
    }

    @OnClick(R.id.rank_review_back)
    public void rankReviewBackClick(){
        finish();
    }

    @OnClick(R.id.rank_review_top)
    public void rankReviewItemToTop(){
        rankReviewRecyclerView.smoothScrollToPosition(0);
    }


    @Override
    public void onRankReviewItemSelected(int position) {
        Intent intent = new Intent(this,RankReviewDetailActivity.class);
        intent.putExtra("data",reviewObjectList.get(position));
        startActivity(intent);
    }
}
