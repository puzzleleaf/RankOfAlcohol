package com.tistory.puzzleleaf.rankofalcohol.rank;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tistory.puzzleleaf.rankofalcohol.R;

import java.util.List;

/**
 * Created by cmtyx on 2017-07-31.
 */

public class RankAdapter extends PagerAdapter {

    Context context;
    List<Integer> res;

    RankAdapter(Context context, List<Integer> res){
        this.context = context;
        this.res = res;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_rank,container,false);

        //@TODO 이미지를 클릭했을 때 해당하는 술 상세보기 항목으로 이동해야 한다.
        ImageView imageView = (ImageView)view.findViewById(R.id.rank_image_view);
        Glide.with(context).load(res.get(position)).into(imageView);

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
