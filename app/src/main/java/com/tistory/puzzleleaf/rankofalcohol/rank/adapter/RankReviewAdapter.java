package com.tistory.puzzleleaf.rankofalcohol.rank.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbAuth;
import com.tistory.puzzleleaf.rankofalcohol.model.RankObject;
import com.tistory.puzzleleaf.rankofalcohol.model.ReviewObject;
import com.tistory.puzzleleaf.rankofalcohol.rank.RankReviewActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cmtyx on 2017-07-31.
 */

public class RankReviewAdapter extends RecyclerView.Adapter<RankReviewAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<ReviewObject> res;

    public RankReviewAdapter(Context context, List<ReviewObject> res){
        this.mInflater = LayoutInflater.from(context);
        this.res = res;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_review_recycler_rank,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return res.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ratingBarInit(holder);
        holder.rankReviewNickName.setText(res.get(position).getNickName());
        holder.rankReviewRatingBar.setRating(Float.valueOf(String.valueOf(res.get(position).getRating())));
        holder.rankReviewMany.setText(res.get(position).getHowMany());
        holder.rankReviewNum.setText(String.valueOf(FbAuth.mUser.gethMany()));
        holder.rankReviewDescription.setText(res.get(position).getContents1());

    }


    private void ratingBarInit(ViewHolder holder){
        LayerDrawable stars = (LayerDrawable)holder.rankReviewRatingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(mInflater.getContext(),R.color.colorStar), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rank_review_recycler_nick_name) TextView rankReviewNickName;
        @BindView(R.id.rank_review_recycler_rating_bar) RatingBar rankReviewRatingBar;
        @BindView(R.id.rank_review_recycler_many) TextView rankReviewMany;
        @BindView(R.id.rank_review_recycler_many_num) TextView rankReviewNum;
        @BindView(R.id.rank_review_recycler_description) TextView rankReviewDescription;
        private ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
