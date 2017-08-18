package com.tistory.puzzleleaf.rankofalcohol;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cmtyx on 2017-07-31.
 */

public class ScreenLockAdapter extends RecyclerView.Adapter<ScreenLockAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private List<Integer> res;

    public ScreenLockAdapter(Context context, List<Integer> res){
        this.mInflater = LayoutInflater.from(context);
        this.res = res;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_screen_lock,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
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
        holder.screenNum.setText(String.valueOf(position+1));
        Glide.with(mInflater.getContext()).load(R.drawable.soju_color).into(holder.screenImage);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.screen_image) ImageView screenImage;
        @BindView(R.id.screen_num) TextView screenNum;
        private ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
