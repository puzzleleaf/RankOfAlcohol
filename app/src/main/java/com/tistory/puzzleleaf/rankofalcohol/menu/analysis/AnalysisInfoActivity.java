package com.tistory.puzzleleaf.rankofalcohol.menu.analysis;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.menu.analysis.adapter.AnalysisInfoAdapter;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cmtyx on 2017-08-13.
 */

public class AnalysisInfoActivity extends AppCompatActivity {

    @BindView(R.id.analysis_info_recycler) RecyclerView analysisRecyclerView;
    @BindArray(R.array.info_img) TypedArray infoImg;
    private LinearLayoutManager linearLayoutManager;
    private AnalysisInfoAdapter analysisInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_info);
        ButterKnife.bind(this);
        recyclerInit();

    }

    private void recyclerInit(){
        analysisInfoAdapter = new AnalysisInfoAdapter(this,infoImg);
        linearLayoutManager = new LinearLayoutManager(this);
        analysisRecyclerView.setLayoutManager(linearLayoutManager);
        analysisRecyclerView.setAdapter(analysisInfoAdapter);
    }

    @OnClick(R.id.analysis_info_back)
    public void analysisInfoBackClick(){
        onBackPressed();
    }

}
