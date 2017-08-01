package com.tistory.puzzleleaf.rankofalcohol.rank.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tistory.puzzleleaf.rankofalcohol.R;

/**
 * Created by cmtyx on 2017-07-31.
 */

public class RankRecyclerAdapter extends RecyclerView.Adapter<RankRecyclerAdapter.ViewHolder> {

    private LayoutInflater mInflater;

    public RankRecyclerAdapter(Context context){
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recycler_rank,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //@TODO Firebase를 통해서 술 평가 순위를 얻어옴
        //@TODO 가지고 온 평가 순위를 통해서 저장된 데이터의 술 이름과 술 설명, 평점 정보를 업데이트

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ViewHolder(View view){
            super(view);
        }
    }
}
