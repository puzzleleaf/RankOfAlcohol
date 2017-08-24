package com.tistory.puzzleleaf.rankofalcohol.contents.battle.adapter;

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
import com.bumptech.glide.request.RequestOptions;
import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.model.BattleObject;
import com.tistory.puzzleleaf.rankofalcohol.model.RankObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cmtyx on 2017-07-31.
 */

public class BattleResultAdpater extends RecyclerView.Adapter<BattleResultAdpater.ViewHolder> {


    private LayoutInflater mInflater;
    private List<BattleObject> res;

    public BattleResultAdpater(Context context, List<BattleObject> res){
        this.mInflater = LayoutInflater.from(context);
        this.res = res;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_battle_result,parent,false);
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
        holder.battleResultName.setText(res.get(position).getName());
        holder.battleResultTime.setText(getTime(res.get(position).getOutTime()));
        holder.battleResultRank.setText(String.valueOf(position+1));
    }

    private String getTime(long time){
       return String.format("%02d:%02d:%02d",time/1000/60,(time/1000)%60,(time%1000)/10);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.battle_result_name) TextView battleResultName;
        @BindView(R.id.battle_result_rank) TextView battleResultRank;
        @BindView(R.id.battle_result_time) TextView battleResultTime;
        private ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
