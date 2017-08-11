package com.tistory.puzzleleaf.rankofalcohol.ui.rank.adapter;

import android.content.Context;
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
import com.tistory.puzzleleaf.rankofalcohol.model.RankObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cmtyx on 2017-07-31.
 */

public class RankRecyclerAdapter extends RecyclerView.Adapter<RankRecyclerAdapter.ViewHolder> {

    public interface OnRankItemClickListener{
        void onRankItemSelected(int position);
    }

    private OnRankItemClickListener rankItemCallback;
    private LayoutInflater mInflater;
    private List<RankObject> res;

    public RankRecyclerAdapter(Context context, List<RankObject> res){
        this.mInflater = LayoutInflater.from(context);
        this.res = res;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recycler_rank,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void setRankItemCallback(OnRankItemClickListener rankItemCallback){
        this.rankItemCallback = rankItemCallback;
    }

    @Override
    public int getItemCount() {
        return res.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //@DONE Firebase를 통해서 술 평가 순위를 얻어옴
        //@TODO 가지고 온 평가 순위를 통해서 저장된 데이터의 술 이름과 술 설명, 평점 정보를 업데이트
        rankRatingBarInit(holder);
        rankTropyInit(holder,position);
        rankItemClick(holder,position);
        Glide.with(mInflater.getContext()).load(res.get(position).getImgKey()).into(holder.rankRecyclerImage);
        holder.rankRecyclerNum.setText(String.valueOf(position+1));
        holder.rankRecyclerBrandName.setText(res.get(position).getBrandName());
        holder.rankRecyclerDegree.setText(String.valueOf(res.get(position).getAlcoholDegree()));
        holder.rankRecyclerRatingBar.setRating(Float.parseFloat(String.valueOf(res.get(position).getScore())));
        holder.rankRecyclerRating.setText(String.format("%.2f",res.get(position).getScore()));


    }

    private void rankItemClick(ViewHolder holder, final int position){
        holder.rankRecyclerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rankItemCallback.onRankItemSelected(position);
            }
        });
    }

    private void rankTropyInit(ViewHolder holder, int position){
        if(position>2){
            holder.rankRecyclerTropy.setVisibility(View.INVISIBLE);
        }
    }

    private void rankRatingBarInit(ViewHolder holder){
        LayerDrawable stars = (LayerDrawable)holder.rankRecyclerRatingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(mInflater.getContext(),R.color.colorStar), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rank_recycler_image) ImageView rankRecyclerImage;
        @BindView(R.id.rank_recycler_num) TextView rankRecyclerNum;
        @BindView(R.id.rank_recycler_brand_name) TextView rankRecyclerBrandName;
        @BindView(R.id.rank_recycler_degree) TextView rankRecyclerDegree;
        @BindView(R.id.rank_recycler_tropy) ImageView rankRecyclerTropy;
        @BindView(R.id.rank_recycler_item) LinearLayout rankRecyclerItem;
        @BindView(R.id.rank_recycler_rating_bar) RatingBar rankRecyclerRatingBar;
        @BindView(R.id.rank_recycler_rating) TextView rankRecyclerRating;
        private ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
