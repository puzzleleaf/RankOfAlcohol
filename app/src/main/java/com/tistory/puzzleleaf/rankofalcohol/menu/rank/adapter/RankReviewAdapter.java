package com.tistory.puzzleleaf.rankofalcohol.menu.rank.adapter;

import android.content.Context;
import android.content.res.TypedArray;
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
import com.bumptech.glide.request.RequestOptions;
import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbAuth;
import com.tistory.puzzleleaf.rankofalcohol.model.ReviewObject;

import java.util.List;
import java.util.Random;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cmtyx on 2017-07-31.
 */

public class RankReviewAdapter extends RecyclerView.Adapter<RankReviewAdapter.ViewHolder> {


    public interface OnRankReviewClickListener{
        void onRankReviewItemSelected(int position);
    }

    private LayoutInflater mInflater;
    private List<ReviewObject> res;
    private OnRankReviewClickListener rankReviewCallback;


    public RankReviewAdapter(Context context, List<ReviewObject> res){
        this.mInflater = LayoutInflater.from(context);
        this.res = res;
    }

    public void setRankReviewCallback(OnRankReviewClickListener onRankReviewClickListener){
        this.rankReviewCallback = onRankReviewClickListener;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        ratingBarInit(holder);
        profileRandomInit(holder);
        rankReviewItemClick(holder,position);
        holder.rankReviewNickName.setText(res.get(position).getNickName());
        holder.rankReviewRatingBar.setRating(Float.valueOf(String.valueOf(res.get(position).getRating())));
        holder.rankReviewMany.setText(res.get(position).getHowMany());
        holder.rankReviewNum.setText(String.valueOf(FbAuth.mUser.gethMany()));
        holder.rankReviewDescription.setText(res.get(position).getContents1());
    }

    private void rankReviewItemClick(ViewHolder holder, final int position){
       holder.rankReviewItem.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               rankReviewCallback.onRankReviewItemSelected(position);
           }
       });
    }

    private void profileRandomInit(ViewHolder holder){
        Random randomProfile = new Random();
        Glide.with(mInflater.getContext())
                .load(holder.profileImage.getResourceId(randomProfile.nextInt(6),-1))
                .apply(new RequestOptions().placeholder(R.drawable.image_loading))
                .into(holder.rankReviewProfile);

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
        @BindView(R.id.rank_review_recycler_profile) ImageView rankReviewProfile;
        @BindView(R.id.rank_review_item) LinearLayout rankReviewItem;
        @BindArray(R.array.profile_img) TypedArray profileImage;
        private ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
