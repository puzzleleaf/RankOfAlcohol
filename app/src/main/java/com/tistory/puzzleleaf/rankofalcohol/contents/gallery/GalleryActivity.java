package com.tistory.puzzleleaf.rankofalcohol.contents.gallery;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.contents.rank.RankReviewRegisterActivity;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbAuth;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbDataBase;
import com.tistory.puzzleleaf.rankofalcohol.contents.gallery.adapter.GalleryAdapter;
import com.tistory.puzzleleaf.rankofalcohol.model.RankObject;
import com.tistory.puzzleleaf.rankofalcohol.model.ReviewObject;
import com.tistory.puzzleleaf.rankofalcohol.util.progress.Loading;
import com.tistory.puzzleleaf.rankofalcohol.util.sort.DescendingRating;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class GalleryActivity extends AppCompatActivity implements GalleryAdapter.OnGalleryObjectClickListener {

    @BindView(R.id.gallery_recycler_view) RecyclerView galleryRecyclerView;
    @BindView(R.id.gallery_num) TextView galleryNum;
    private GalleryAdapter galleryAdapter;
    private List<RankObject> obj;
    private List<String> userKey;
    private DescendingRating descendingRating;
    private Loading loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        dataInit();
        recyclerInit();
        dataUserReviewItemKeyLoad();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void refreshData(){
        Collections.sort(obj,descendingRating);
        galleryNum.setText(String.valueOf(FbDataBase.userReviewCount));
        galleryAdapter.notifyDataSetChanged();
    }


    private void dataUserReviceItemRatingLoad(){
        FbDataBase.database.getReference().child("Review").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FbDataBase.userReviewCount = 0;
                for(int i=0;i<obj.size();i++){
                    Iterator<DataSnapshot> iterator =dataSnapshot.child(obj.get(i).getObjectKey()).getChildren().iterator();
                    while(iterator.hasNext()){
                        ReviewObject reviewObject = iterator.next().getValue(ReviewObject.class);
                        if(reviewObject.getUserKey().equals(FbAuth.mAuth.getCurrentUser().getUid())){
                            obj.get(i).setScore(reviewObject.getRating());
                        }
                    }
                    FbDataBase.userReviewCount ++;
                }
                refreshData();
                loading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void dataUserReviewItemLoad(){

            FbDataBase.database.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    RankObject rankObject;
                    for(int i=0;i<userKey.size();i++){
                        rankObject = dataSnapshot.child("Soju").child(userKey.get(i)).getValue(RankObject.class);
                        if(rankObject!=null){
                            obj.add(rankObject);
                            continue;
                        }
                        rankObject = dataSnapshot.child("Beer").child(userKey.get(i)).getValue(RankObject.class);
                        if(rankObject!=null){
                            obj.add(rankObject);
                        }
                    }
                    dataUserReviceItemRatingLoad();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

    }

    private void dataUserReviewItemKeyLoad(){
        loading.show();
        userKey.clear();
        FbDataBase.database.getReference()
                .child("User")
                .child(FbAuth.mAuth.getCurrentUser().getUid())
                .child("review").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                while(iterator.hasNext()){
                    String temp = iterator.next().getValue(String.class);
                    userKey.add(temp);
                }
                dataUserReviewItemLoad();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void dataInit(){
        obj = new ArrayList<>();
        userKey = new ArrayList<>();
        descendingRating = new DescendingRating();
    }

    private void recyclerInit(){
        loading = new Loading(this,"gallery");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        galleryAdapter = new GalleryAdapter(this,obj);
        galleryAdapter.setOnGalleryObjectClickListener(this);
        galleryRecyclerView.setLayoutManager(gridLayoutManager);
        galleryRecyclerView.setAdapter(galleryAdapter);
    }


    @OnClick(R.id.gallery_back)
    public void galleryBackClick(){
        onBackPressed();
    }

    @Override
    public void onGalleryObjectSelected(final RankObject obj) {
        loading.show();
        FbDataBase.database.getReference().child("Review").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ReviewObject reviewObject;
                Iterator<DataSnapshot> iterator = dataSnapshot.child(obj.getObjectKey()).getChildren().iterator();
                while (iterator.hasNext()){
                    reviewObject = iterator.next().getValue(ReviewObject.class);

                    if(reviewObject.getUserKey().equals(FbAuth.mAuth.getCurrentUser().getUid())){
                        loading.dismiss();
                        Intent intent = new Intent(getApplicationContext(), RankReviewRegisterActivity.class);
                        intent.putExtra("modify",reviewObject);
                        intent.putExtra("data",obj);
                        startActivity(intent);
                        finish();
                        break;
                    }

                }
                loading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
