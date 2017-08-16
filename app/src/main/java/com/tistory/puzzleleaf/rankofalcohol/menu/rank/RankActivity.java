package com.tistory.puzzleleaf.rankofalcohol.menu.rank;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbDataBase;
import com.tistory.puzzleleaf.rankofalcohol.model.RankObject;
import com.tistory.puzzleleaf.rankofalcohol.model.RatingObject;
import com.tistory.puzzleleaf.rankofalcohol.util.progress.Loading;
import com.tistory.puzzleleaf.rankofalcohol.menu.rank.adapter.RankAdapter;
import com.tistory.puzzleleaf.rankofalcohol.menu.rank.adapter.RankRecyclerAdapter;
import com.tistory.puzzleleaf.rankofalcohol.util.sort.DescendingRating;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RankActivity extends AppCompatActivity implements RankRecyclerAdapter.OnRankItemClickListener{

    //@TODO 로딩 이미지 추가하기

    //recycler
    RankRecyclerAdapter rankRecyclerAdapter;
    LinearLayoutManager linearLayoutManager;
    @BindView(R.id.rank_recycler_view) RecyclerView rankRecyclerView;

    //viewpager
    private RankAdapter rankAdapter;
    @BindView(R.id.rank_viewpager) ViewPager rankViewPager;
    @BindView(R.id.rank_tab_layout) TabLayout rankTabLayout;

    //spinner
    @BindView(R.id.rank_spinner) Spinner rankSpinner;
    @BindView(R.id.rank_spinner_text) TextView rankSpinnerText;
    @BindView(R.id.rank_spinner_click) LinearLayout rankSpinnerClick;
    private ArrayAdapter<CharSequence> option;

    private Loading loading;

    @BindArray(R.array.option) String []rankMenuTitle;

    //DBObject
    private List<RankObject> rankObjectList;
    private List<RankObject> rankObject;
    private String dataLoadStr ;
    private String dataRatingLoadStr;
    private DescendingRating descendingRating;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        ButterKnife.bind(this);

        init();

        //@TODO 나중에 로딩 중 보여줄 더미데이터 추가하기(첫 로딩에만 필요)(주류 변경시 필요 x)
      // dataUploadSample();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataLoad();
    }

    private void dataLoad(){
        loading.show(); //Dialog Show
        if(dataLoadStr.equals("")){
            dataLoadTotal();
        }else{
            dataLoadPartial();
        }
    }

    private void dataLoadTotal(){
        FbDataBase.database.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clearData();
                Iterator<DataSnapshot> iterator = dataSnapshot.child(getString(R.string.rank_data_load_zero)).getChildren().iterator();
                while (iterator.hasNext()){
                    RankObject rankObject = iterator.next().getValue(RankObject.class);
                    rankObjectList.add(rankObject);
                }

                iterator = dataSnapshot.child(getString(R.string.rank_data_load_fir)).getChildren().iterator();
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

    private void dataLoadPartial(){
        FbDataBase.database.getReference(dataLoadStr).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clearData();
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
        FbDataBase.database.getReference(dataRatingLoadStr).addListenerForSingleValueEvent(new ValueEventListener() {
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //@TODO 데이터 갱신 예외처리 할 것
            }
        });
    }

    private void clearData(){
        rankObjectList.clear();
    }

    private void refresh(){
        rankAdapter.notifyDataSetChanged();
        rankRecyclerAdapter.notifyDataSetChanged();
        loading.dismiss();
    }

    private void rankData(){
        Collections.sort(rankObjectList,descendingRating);
        rankObject.clear();
        //@TODO 이후에 수정할 데이터 로딩 로직
        if(rankObjectList.size()<3){
            for(int i=0;i<rankObjectList.size();i++){
                rankObject.add(rankObjectList.get(i));
            }
        }else {
            for (int i = 0; i < 3; i++) {
                rankObject.add(rankObjectList.get(i));
            }
        }
        refresh();
    }

    //필터에 따라서 데이터 로딩
    private void setDataLoadStr(int position){
        switch (position){
            case 0:
                dataLoadStr = "";
                dataRatingLoadStr = getString(R.string.rank_data_rating);
                break;
            case 1:
                dataLoadStr = getString(R.string.rank_data_load_zero);
                dataRatingLoadStr = getString(R.string.rank_data_rating);
                break;
            case 2:
                dataLoadStr = getString(R.string.rank_data_load_fir);
                dataRatingLoadStr = getString(R.string.rank_data_rating);
                break;
            case 4:
                dataLoadStr = "";
                dataRatingLoadStr = getString(R.string.rank_data_rating_man);
                break;
            case 5:
                dataLoadStr = "";
                dataRatingLoadStr = getString(R.string.rank_data_rating_woman);
                break;



        }
        dataLoad();
    }

    private void init(){
        descendingRating = new DescendingRating();
        rankObjectList = new ArrayList<>();
        rankObject = new ArrayList<>();

        loading = new Loading(this,"rank");

        dataLoadStr = "";
        dataRatingLoadStr = getString(R.string.rank_data_rating);

        //ViewPager
        rankAdapter = new RankAdapter(this,rankObject);

        rankViewPager.setOffscreenPageLimit(3);
        rankViewPager.setAdapter(rankAdapter);
        rankTabLayout.setupWithViewPager(rankViewPager,true);

        //Recycler
        rankRecyclerAdapter = new RankRecyclerAdapter(this,rankObjectList);
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
                setDataLoadStr(position);
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

    @OnClick(R.id.rank_back)
    public void rankBackClick(){
        onBackPressed();
    }

    @Override
    public void onRankItemSelected(int position) {
        Intent intent = new Intent(this, RankReviewActivity.class);
        intent.putExtra("data",rankObjectList.get(position));
        startActivity(intent);
    }

    //@TODO 데이터 등록용 이므로 이후에 제거할 영역
    private void dataUploadSample(){
//        String objKey = FbDataBase.database.getReference().child("Soju").push().getKey();
//        RankObject rankObject = new RankObject("처음처럼(진한)", 21,"","soju_05.png",objKey);
//        FbDataBase.database.getReference().child("Soju").child(objKey).setValue(rankObject);

        String objKey2 = FbDataBase.database.getReference().child("Beer").push().getKey();
        RankObject rankObject2 = new RankObject("하이트", 4.5,"","beer_01.png",objKey2);
        FbDataBase.database.getReference().child("Beer").child(objKey2).setValue(rankObject2);
    }




}
