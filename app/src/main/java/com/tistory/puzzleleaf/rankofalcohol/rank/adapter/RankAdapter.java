package com.tistory.puzzleleaf.rankofalcohol.rank.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class RankAdapter extends PagerAdapter {

    Context context;
    List<RankObject> res;

    public RankAdapter(Context context, List<RankObject> res) {
        this.context = context;
        this.res = res;
    }

    @BindView(R.id.rank_image_view) ImageView rankImageView;
    @BindView(R.id.rank_brand_name) TextView rankBrandName;
    @BindView(R.id.rank_alcohol_degree) TextView rankAlcoholDegree;
    @BindView(R.id.rank_rating) TextView rankRating;
    @BindView(R.id.rank_num) TextView rankNum;
    @BindView(R.id.rank_rating_bar) RatingBar rankRatingBar;

    @Override
    public synchronized Object instantiateItem(final ViewGroup container, final int position) {
        View view = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_rank, container, false);
        ButterKnife.bind(this, view);

        ratingBarInit();
        rankBrandName.setText(res.get(position).getBrandName());
        rankAlcoholDegree.setText(String.valueOf(res.get(position).getAlcoholDegree()));
        rankRating.setText(String.format("%.2f", res.get(position).getScore()));
        rankNum.setText(String.valueOf(res.get(position).getTotal()));
        rankRatingBar.setRating(Float.parseFloat(String.valueOf(res.get(position).getScore())));

        Glide.with(context).load(res.get(position).getImgKey()).into(rankImageView);

        container.addView(view);
        return view;
    }

    private void ratingBarInit() {
        LayerDrawable stars = (LayerDrawable) rankRatingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(context, R.color.colorStar), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public int getCount() {
        return res.size();
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
