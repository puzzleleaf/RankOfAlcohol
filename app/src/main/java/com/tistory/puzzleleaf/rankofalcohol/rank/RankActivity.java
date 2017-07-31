package com.tistory.puzzleleaf.rankofalcohol.rank;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.tistory.puzzleleaf.rankofalcohol.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RankActivity extends AppCompatActivity {

    @BindView(R.id.rank_viewpager) ViewPager rankViewPager;
    @BindView(R.id.rank_tab_layout) TabLayout rankTabLayout;

    RankAdapter rankAdapter;
    List<Integer> imageRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        ButterKnife.bind(this);

        init();
        imageDataLoad();
        //@TODO 하단에 각 종류별로 메뉴 목록을 만들어야 한다.
        //@TODO RecyclerView 를 이용해서 하단에 술 랭킹 항목을 추가해야 한다.

    }

    private void imageDataLoad(){
        //@TODO FireBase를 통해서 가져온 정보를 통해 상위 3개에 대해서 이미지가 나타나도록 한다.
        imageRes.add(R.drawable.a);
        imageRes.add(R.drawable.b);
        imageRes.add(R.drawable.c);
        rankAdapter.notifyDataSetChanged();
    }

    private void init(){
        imageRes= new ArrayList<>();
        rankAdapter = new RankAdapter(this,imageRes);
        rankViewPager.setAdapter(rankAdapter);
        rankTabLayout.setupWithViewPager(rankViewPager,true);

    }

}
