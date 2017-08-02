package com.tistory.puzzleleaf.rankofalcohol.rank.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbDataBase;
import com.tistory.puzzleleaf.rankofalcohol.model.AlcoholObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cmtyx on 2017-07-31.
 */

public class RankAdapter extends PagerAdapter {

    Context context;
    List<AlcoholObject> res;

    public RankAdapter(Context context, List<AlcoholObject> res){
        this.context = context;
        this.res = res;
    }


    @BindView(R.id.rank_image_view) ImageView rankImageView;
    @BindView(R.id.rank_brand_name) TextView rankBrandName;
    @BindView(R.id.rank_alcohol_degree) TextView rankAlcoholDegree;
    @Override
    public synchronized Object instantiateItem(final ViewGroup container, final int position) {
        View view = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_rank,container,false);
        ButterKnife.bind(this,view);


        //@TODO 이미지를 클릭했을 때 해당하는 술 상세보기 항목으로 이동해야 한다.

        rankBrandName.setText(res.get(position).getBrandName());
        rankAlcoholDegree.setText(String.valueOf(res.get(position).getAlcoholDegree()));

        Glide.with(context).load(res.get(position).getImgKey()).into(rankImageView);

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return res.size();
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
