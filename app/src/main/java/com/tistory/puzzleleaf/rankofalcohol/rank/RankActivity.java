package com.tistory.puzzleleaf.rankofalcohol.rank;

import android.content.Intent;
import android.net.Uri;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbDataBase;
import com.tistory.puzzleleaf.rankofalcohol.model.AlcoholObject;
import com.tistory.puzzleleaf.rankofalcohol.progress.Loading;
import com.tistory.puzzleleaf.rankofalcohol.rank.adapter.RankAdapter;
import com.tistory.puzzleleaf.rankofalcohol.rank.adapter.RankRecyclerAdapter;

import java.util.ArrayList;
import java.util.Iterator;
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
    @BindView(R.id.rank_viewpager) ViewPager rankViewPager;
    @BindView(R.id.rank_tab_layout) TabLayout rankTabLayout;

    //spinner
    @BindView(R.id.rank_spinner) Spinner rankSpinner;
    @BindView(R.id.rank_spinner_text) TextView rankSpinnerText;
    @BindView(R.id.rank_spinner_click) LinearLayout rankSpinnerClick;
    private ArrayAdapter<CharSequence> option;

    private Loading loading;

    //DBObject
    List<AlcoholObject> alcoholObjectList;
    List<AlcoholObject> rankObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        ButterKnife.bind(this);

        init();
        dataLoad();
        //@TODO 나중에 로딩 중 보여줄 더미데이터 추가하기(첫 로딩에만 필요)(주류 변경시 필요 x)
       //dataUploadSample();
    }

    private void dataLoad(){
        //@TODO FireBase를 통해서 가져온 정보를 통해 상위 3개에 대해서 이미지가 나타나도록 한다.

        loading.show(); //Dialog Show
        FbDataBase.database.getReference("Soju").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    AlcoholObject alcoholObject = iterator.next().getValue(AlcoholObject.class);
                    alcoholObjectList.add(alcoholObject);
                }
                rankData();
                refresh();
                loading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //@TODO 데이터 갱신 예외처리 할 것
            }
        });
    }

    private void refresh(){
        rankAdapter.notifyDataSetChanged();
        rankRecyclerAdapter.notifyDataSetChanged();
    }

    //@TODO 상위 3개 데이터를 정렬해서 저장한다.
    private void rankData(){
        for(int i=0;i<3;i++){
            rankObject.add(alcoholObjectList.get(i));
        }
    }

    private void init(){
        alcoholObjectList = new ArrayList<>();
        rankObject = new ArrayList<>();

        loading = new Loading(this);

        //ViewPager
        rankAdapter = new RankAdapter(this,rankObject);

        rankViewPager.setOffscreenPageLimit(3);
        rankViewPager.setAdapter(rankAdapter);
        rankTabLayout.setupWithViewPager(rankViewPager,true);

        //Recycler
        rankRecyclerAdapter = new RankRecyclerAdapter(this,rankObject);
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
        intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) alcoholObjectList);
        startActivity(intent);

    }

    //@TODO 데이터 등록용 이므로 이후에 제거할 영역
    private void dataUploadSample(){
        String objKey = FbDataBase.database.getReference().child("Soju").push().getKey();
        AlcoholObject alcoholObject = new AlcoholObject("처음처럼(진한)", 21,"","soju_05.png",objKey);

        FbDataBase.database.getReference().child("Soju").child(objKey).setValue(alcoholObject);
    }

}
