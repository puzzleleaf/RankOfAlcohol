package com.tistory.puzzleleaf.rankofalcohol.rank.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.model.RankObject;
import com.tistory.puzzleleaf.rankofalcohol.rank.RankReviewActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cmtyx on 2017-07-31.
 */

public class RankRecyclerAdapter extends RecyclerView.Adapter<RankRecyclerAdapter.ViewHolder> {

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

    @Override
    public int getItemCount() {
        return res.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //@DONE Firebase를 통해서 술 평가 순위를 얻어옴
        //@TODO 가지고 온 평가 순위를 통해서 저장된 데이터의 술 이름과 술 설명, 평점 정보를 업데이트
        Glide.with(mInflater.getContext()).load(res.get(position).getImgKey()).into(holder.rankRecyclerImage);
        if(position>2){
            holder.rankRecyclerTropy.setVisibility(View.INVISIBLE);
        }
        holder.rankRecyclerNum.setText(String.valueOf(position+1));
        holder.rankRecyclerBrandName.setText(res.get(position).getBrandName());
        holder.rankRecyclerDegree.setText(String.valueOf(res.get(position).getAlcoholDegree()));

        holder.rankRecyclerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mInflater.getContext(), RankReviewActivity.class);
                intent.putExtra("data",res.get(position));
                mInflater.getContext().startActivity(intent);
            }
        });
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rank_recycler_image) ImageView rankRecyclerImage;
        @BindView(R.id.rank_recycler_num) TextView rankRecyclerNum;
        @BindView(R.id.rank_recycler_brand_name) TextView rankRecyclerBrandName;
        @BindView(R.id.rank_recycler_degree) TextView rankRecyclerDegree;
        @BindView(R.id.rank_recycler_tropy) ImageView rankRecyclerTropy;
        @BindView(R.id.rank_recycler_item) LinearLayout rankRecyclerItem;
        private ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
