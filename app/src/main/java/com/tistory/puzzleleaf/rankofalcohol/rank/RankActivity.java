package com.tistory.puzzleleaf.rankofalcohol.rank;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbDataBase;
import com.tistory.puzzleleaf.rankofalcohol.model.RankObject;
import com.tistory.puzzleleaf.rankofalcohol.model.RatingObject;
import com.tistory.puzzleleaf.rankofalcohol.progress.Loading;
import com.tistory.puzzleleaf.rankofalcohol.rank.adapter.RankAdapter;
import com.tistory.puzzleleaf.rankofalcohol.rank.adapter.RankRecyclerAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RankActivity extends AppCompatActivity implements RankRecyclerAdapter.OnRankItemClickListener{

    //recycler
    RankRecyclerAdapter rankRecyclerAdapter;
    LinearLayoutManager linearLayoutManager;
    @BindView(R.id.rank_recycler_view) RecyclerView rankRecyclerView;

    //viewpager
    RankAdapter rankAdapter;
    @BindView(R.id.rank_viewpager) ViewPager rankViewPager;
    @BindView(R.id.rank_tab_layout) TabLayout rankTabLayout;

    //spinner
    @BindView(R.id.rank_spinner) Spinner rankSpinner;
    @BindView(R.id.rank_spinner_text) TextView rankSpinnerText;
    @BindView(R.id.rank_spinner_click) LinearLayout rankSpinnerClick;
    private ArrayAdapter<CharSequence> option;

    private Loading loading;

    //DBObject
    List<RankObject> rankObjectList;
    List<RankObject> rankObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        ButterKnife.bind(this);

        init();

        //@TODO 나중에 로딩 중 보여줄 더미데이터 추가하기(첫 로딩에만 필요)(주류 변경시 필요 x)
       //dataUploadSample();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataLoad();
    }

    private void dataLoad(){
        //@TODO FireBase를 통해서 가져온 정보를 통해 상위 3개에 대해서 이미지가 나타나도록 한다.
        loading.show(); //Dialog Show
        clearData();
        FbDataBase.database.getReference("Soju").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    RankObject rankObject = iterator.next().getValue(RankObject.class);
                    rankObjectList.add(rankObject);
                }
                ratingDataLoad();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //@TODO 데이터 갱신 예외처리 할 것
            }
        });
    }

    private void ratingDataLoad(){
        FbDataBase.database.getReference("Rating").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(int i=0;i<rankObjectList.size();i++){
                    String temp = rankObjectList.get(i).getObjectKey();
                    RatingObject ratingObject = dataSnapshot.child(temp).getValue(RatingObject.class);
                    if(ratingObject !=null){
                        double result = (double)ratingObject.getTotal()/ratingObject.getNum();
                        rankObjectList.get(i).setTotal(ratingObject.getNum());
                        rankObjectList.get(i).setScore(result);
                    }else {
                        rankObjectList.get(i).setScore(0);
                        rankObjectList.get(i).setTotal(0);
                    }
                }
                rankData();
                refresh();
                loading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void clearData(){
        rankObjectList.clear();
        rankObject.clear();
    }

    private void refresh(){
        rankAdapter.notifyDataSetChanged();
        rankRecyclerAdapter.notifyDataSetChanged();
    }

    //@TODO 데이터 정렬해서 3개만 넣기
    private void rankData(){
        for(int i=0;i<3;i++){
            rankObject.add(rankObjectList.get(i));
        }
    }

    private void init(){
        rankObjectList = new ArrayList<>();
        rankObject = new ArrayList<>();

        loading = new Loading(this,"rank");

        //ViewPager
        rankAdapter = new RankAdapter(this,rankObject);

        rankViewPager.setOffscreenPageLimit(3);
        rankViewPager.setAdapter(rankAdapter);
        rankTabLayout.setupWithViewPager(rankViewPager,true);

        //Recycler
        rankRecyclerAdapter = new RankRecyclerAdapter(this,rankObject);
        rankRecyclerAdapter.setRankItemCallback(this);
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
                //@TODO 선택한 필터에 따라서 내용들이 갱신되도록 바꾸기 , 다른 콘텐츠가 준비 된 이후에 추가
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

    @OnClick(R.id.rank_more_btn)
    public void moreBtnClick(){
        Intent intent = new Intent(this,RankDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) rankObjectList);
        startActivity(intent);

    }

    @Override
    public void onRankItemSelected(int position) {
        Intent intent = new Intent(this, RankReviewActivity.class);
        intent.putExtra("data",rankObjectList.get(position));
        startActivity(intent);
    }

    //@TODO 데이터 등록용 이므로 이후에 제거할 영역
    private void dataUploadSample(){
        String objKey = FbDataBase.database.getReference().child("Soju").push().getKey();
        RankObject rankObject = new RankObject("처음처럼(진한)", 21,"","soju_05.png",objKey);
        FbDataBase.database.getReference().child("Soju").child(objKey).setValue(rankObject);

        rankObject = new RankObject("처음처럼(진한)", 21,"","soju_05.png",objKey);
    }

}
