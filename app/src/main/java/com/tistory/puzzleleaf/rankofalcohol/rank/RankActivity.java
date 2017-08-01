package com.tistory.puzzleleaf.rankofalcohol.rank;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.rank.adapter.RankAdapter;
import com.tistory.puzzleleaf.rankofalcohol.rank.adapter.RankRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RankActivity extends AppCompatActivity {

    //recycler
    RankRecyclerAdapter rankRecyclerAdapter;
    LinearLayoutManager linearLayoutManager;
    @BindView(R.id.rank_recycler_view) RecyclerView rankRecyclerView;

    //viewpager
    RankAdapter rankAdapter;
    List<Integer> imageRes;
    @BindView(R.id.rank_viewpager) ViewPager rankViewPager;
    @BindView(R.id.rank_tab_layout) TabLayout rankTabLayout;

    //spinner
    @BindView(R.id.rank_spinner) Spinner rankSpinner;
    @BindView(R.id.rank_spinner_text) TextView rankSpinnerText;
    @BindView(R.id.rank_spinner_click) LinearLayout rankSpinnerClick;
    private ArrayAdapter<CharSequence> option;

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
        imageRes.add(R.drawable.sam1);
        imageRes.add(R.drawable.sam2);
        imageRes.add(R.drawable.sam3);
        rankAdapter.notifyDataSetChanged();
    }

    private void init(){
        imageRes= new ArrayList<>();
        //ViewPager
        rankAdapter = new RankAdapter(this,imageRes);

        rankViewPager.setAdapter(rankAdapter);
        rankTabLayout.setupWithViewPager(rankViewPager,true);

        //Recycler
        rankRecyclerAdapter = new RankRecyclerAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this);
        rankRecyclerView.setLayoutManager(linearLayoutManager);
        rankRecyclerView.setAdapter(rankRecyclerAdapter);

        //spinner
        option = ArrayAdapter.createFromResource(this,R.array.option,android.R.layout.simple_spinner_dropdown_item);
        option.setDropDownViewResource(R.layout.spinner_dropdown);
        rankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rankSpinnerText.setText(option.getItem(position));
                //@TODO 선택한 필터에 따라서 내용들이 갱신되도록 바꾸기
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        rankSpinner.setAdapter(option);

    }

    @OnClick(R.id.rank_spinner_click)
    public void spinnerClick(){
        rankSpinner.performClick();
    }

}
