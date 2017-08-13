package com.tistory.puzzleleaf.rankofalcohol.menu.rank;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.model.ReviewObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RankReviewDetailActivity extends AppCompatActivity {
    private ReviewObject reviewObject;
    @BindView(R.id.rank_review_detail_nick_name) TextView rankReviewDetailNickName;
    @BindView(R.id.rank_review_detail_ratingbar) RatingBar rankReviewDetailRatingBar;
    @BindView(R.id.rank_review_detail_many) TextView rankReviewDetailMany;
    @BindView(R.id.rank_review_detail_many_num) TextView rankReviewDetailManyNum;
    @BindView(R.id.rank_review_detail_contents_first) TextView rankReviewDetailContentsFirst;
    @BindView(R.id.rank_review_detail_contents_second) TextView rankReviewDetailContentsSecond;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_review_detail);
        ButterKnife.bind(this);

        dataInit();
        rankReviewRatingBarInit();

    }

    private void dataInit(){
        reviewObject = getIntent().getParcelableExtra("data");
        rankReviewDetailNickName.setText(reviewObject.getNickName());
        rankReviewDetailRatingBar.setRating(Float.valueOf(String.valueOf(reviewObject.getRating())));
        rankReviewDetailMany.setText(reviewObject.getHowMany());
        rankReviewDetailManyNum.setText(String.valueOf(reviewObject.getUserHowMany()));
        rankReviewDetailContentsFirst.setText(reviewObject.getContents1());
        rankReviewDetailContentsSecond.setText(reviewObject.getContents2());

    }

    @OnClick(R.id.rank_review_detail_back)
    public void rankReviewDetailBackClick(){
        onBackPressed();
    }

    //@TODO RatingBar 초기화 나중에 하나로 묶어서 관리하기
    private void rankReviewRatingBarInit(){
        LayerDrawable stars = (LayerDrawable)rankReviewDetailRatingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(this,R.color.colorStar), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
    }

}
