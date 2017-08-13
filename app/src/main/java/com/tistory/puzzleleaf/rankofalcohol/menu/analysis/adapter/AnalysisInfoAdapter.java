package com.tistory.puzzleleaf.rankofalcohol.menu.analysis.adapter;

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
import com.tistory.puzzleleaf.rankofalcohol.model.RankObject;

import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cmtyx on 2017-08-13.
 */

public class AnalysisInfoAdapter extends RecyclerView.Adapter<AnalysisInfoAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private TypedArray res;
    public AnalysisInfoAdapter(Context context, TypedArray res){
        this.mInflater = LayoutInflater.from(context);
        this.res = res;
}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recycler_analysis,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public int getItemCount() {
        return res.length();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mInflater.getContext()).load(res.getResourceId(position,-1)).into(holder.analysisImageView);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.analysis_info_item) ImageView analysisImageView;
        private ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
