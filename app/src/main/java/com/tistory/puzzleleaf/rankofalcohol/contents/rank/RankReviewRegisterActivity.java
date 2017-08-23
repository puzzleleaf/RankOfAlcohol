package com.tistory.puzzleleaf.rankofalcohol.contents.rank;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbAuth;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbDataBase;
import com.tistory.puzzleleaf.rankofalcohol.model.RankObject;
import com.tistory.puzzleleaf.rankofalcohol.model.RatingObject;
import com.tistory.puzzleleaf.rankofalcohol.model.ReviewObject;
import com.tistory.puzzleleaf.rankofalcohol.util.progress.Loading;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RankReviewRegisterActivity extends AppCompatActivity {

    @BindView(R.id.rank_review_register_ratingbar) RatingBar rankReviewRegisterRatingBar;
    @BindView(R.id.rank_review_register_nick_name) EditText rankReviewRegisterNickName;
    @BindView(R.id.rank_review_register_radio) RadioGroup rankReviewRegisterRadioGroup;
    @BindView(R.id.rank_radio_btn1) RadioButton rankRadioBtnLittle;
    @BindView(R.id.rank_radio_btn2) RadioButton rankRadioBtnQuiteLittle;
    @BindView(R.id.rank_radio_btn3) RadioButton rankRadioBtnMuch;
    @BindView(R.id.rank_radio_btn4) RadioButton rankRadioBtnVeryMuch;
    @BindView(R.id.rank_review_register_contents_first) EditText rankReviewRegisterContentsFirst;
    @BindView(R.id.rank_review_register_contents_second) EditText rankReviewRegisterContentsSecond;
    @BindView(R.id.rank_review_register_image) ImageView rankReviewRegisterImageView;
    @BindView(R.id.rank_review_register_brand_name) TextView rankReviewRegisterBrandName;
    @BindView(R.id.rank_review_register_degree) TextView rankReviewRegisterDegree;


    //DB
    private RankObject rankObject;
    private ReviewObject reviewObject;
    private Loading loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_review_register);
        ButterKnife.bind(this);

        dataInit();
        ratingBarInit();
        dateInit();
    }

    private void dataInit(){
        rankObject = getIntent().getParcelableExtra("data");
        loading = new Loading(this,"write");
        Glide.with(this).load(rankObject.getImgKey()).into(rankReviewRegisterImageView);
        rankReviewRegisterBrandName.setText(rankObject.getBrandName());
        rankReviewRegisterDegree.setText(String.valueOf(rankObject.getAlcoholDegree()));

        reviewObject = getIntent().getParcelableExtra("modify");
        if(reviewObject!=null){
            reviewModify();
        }
    }

    private void reviewModify(){
        rankReviewRegisterRatingBar.setRating((float) reviewObject.getRating());
        rankReviewRegisterNickName.setText(reviewObject.getNickName());
        rankReviewRegisterContentsFirst.setText(reviewObject.getContents1());
        rankReviewRegisterContentsSecond.setText(reviewObject.getContents2());

        switch (reviewObject.getHowMany()){
            case "아주 조금":
                rankRadioBtnLittle.setChecked(true);
                break;
            case "조금":
                rankRadioBtnQuiteLittle.setChecked(true);
                break;
            case "적당히":
                rankRadioBtnMuch.setChecked(true);
                break;
            case "많이":
                rankRadioBtnVeryMuch.setChecked(true);
                break;
        }
    }

    private void ratingBarInit(){
        LayerDrawable stars = (LayerDrawable)rankReviewRegisterRatingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(this,R.color.colorStar), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
    }

    private String getRankRadioBtnValue(int id){
        switch (id){
            case R.id.rank_radio_btn1 :
                return getString(R.string.rank_radio1);
            case R.id.rank_radio_btn2 :
                return getString(R.string.rank_radio2);
            case R.id.rank_radio_btn3 :
                return getString(R.string.rank_radio3);
            case R.id.rank_radio_btn4 :
                return getString(R.string.rank_radio4);
        }
        return "Error";
    }

    //등록 시간 구하기
    private String dateInit(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("MM월 dd일");
        return simpleDate.format(date);
    }

    private boolean checkContents(){
        if(rankReviewRegisterRatingBar.getRating()>0 && !rankReviewRegisterNickName.getText().equals("")
                && rankReviewRegisterRadioGroup.getCheckedRadioButtonId()!=-1 && rankReviewRegisterContentsFirst.length()>=5
                && rankReviewRegisterContentsSecond.length()>=5 ){
            return true;
        }else if(rankReviewRegisterRatingBar.getRating()==0){
            Toast.makeText(this,"평점을 등록해주세요.",Toast.LENGTH_SHORT).show();
        } else if(rankReviewRegisterRadioGroup.getCheckedRadioButtonId()==-1){
            Toast.makeText(this,"얼마나 마셔봤는지 선택해주세요.",Toast.LENGTH_SHORT).show();
        }else if(rankReviewRegisterContentsFirst.length()<12 || rankReviewRegisterContentsSecond.length()<12){
            Toast.makeText(this,"5자 이상 평가 내용을 입력해주세요.",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"모든 항목을 채워주세요.",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void reviewRegister(){
        loading.show();
        String reviewKey = FbDataBase.database.getReference().child("Review").child(rankObject.getObjectKey()).push().getKey();
        ReviewObject reviewObj = new ReviewObject(rankReviewRegisterNickName.getText().toString(),
                (int) rankReviewRegisterRatingBar.getRating(),
                getRankRadioBtnValue(rankReviewRegisterRadioGroup.getCheckedRadioButtonId()),
                rankReviewRegisterContentsFirst.getText().toString(),
                rankReviewRegisterContentsSecond.getText().toString(),
                dateInit(),
                FbAuth.mUser.gethMany(),
                reviewKey,
                FbAuth.mAuth.getCurrentUser().getUid());

        FbDataBase.database.getReference().child("Review").child(rankObject.getObjectKey()).child(reviewKey).setValue(reviewObj);

        FbDataBase.database.getReference().child("User").child(FbAuth.mAuth.getCurrentUser().getUid())
                .child("review")
                .push()
                .setValue(rankObject.getObjectKey());

        ratingRegister((int) rankReviewRegisterRatingBar.getRating());
    }

    private void reviewModifyRegister(){
        loading.show();

        ReviewObject reviewObj = new ReviewObject(rankReviewRegisterNickName.getText().toString(),
                (int) rankReviewRegisterRatingBar.getRating(),
                getRankRadioBtnValue(rankReviewRegisterRadioGroup.getCheckedRadioButtonId()),
                rankReviewRegisterContentsFirst.getText().toString(),
                rankReviewRegisterContentsSecond.getText().toString(),
                dateInit(),
                FbAuth.mUser.gethMany(),
                reviewObject.getReviewKey(),
                FbAuth.mAuth.getCurrentUser().getUid());

        FbDataBase.database.getReference().child("Review").child(rankObject.getObjectKey()).child(reviewObject.getReviewKey()).setValue(reviewObj);

        ratingRegister((int) rankReviewRegisterRatingBar.getRating());
    }

    //전체 평점 실시간 등록 - FireBase Transaction
    private void ratingRegister(final int rating){
        FbDataBase.database.getReference().child("Rating").child(rankObject.getObjectKey()).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                RatingObject ratingObject = mutableData.getValue(RatingObject.class);
                if(ratingObject == null){
                    ratingObject = new RatingObject(rating,1);
                }else{
                    int total = ratingObject.getTotal();
                    int num = ratingObject.getNum();
                    if(reviewObject!=null){
                        ratingObject.setTotal(total+rating-(int)reviewObject.getRating());
                    }else{
                        ratingObject.setTotal(total+rating);
                        ratingObject.setNum(++num);
                    }
                }
                mutableData.setValue(ratingObject);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                ratingGenderRegister(rating);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this,"왼쪽 상단 버튼을 눌러주세요.",Toast.LENGTH_SHORT).show();
    }

    // 성별별 리뷰 데이터 저장 로직
    private void ratingGenderRegister(final int rating){
        String fbGenderRating = "";
        if(FbAuth.mUser.getGender().equals("남성")){
            fbGenderRating = "ManRating";
        }else{
            fbGenderRating = "WomanRating";
        }

        FbDataBase.database.getReference().child(fbGenderRating).child(rankObject.getObjectKey()).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                RatingObject ratingObject = mutableData.getValue(RatingObject.class);
                if(ratingObject == null){
                    ratingObject = new RatingObject(rating,1);
                }else{
                    int total = ratingObject.getTotal();
                    int num = ratingObject.getNum();
                    if(reviewObject!=null){
                        ratingObject.setTotal(total+rating-(int)reviewObject.getRating());
                    }else{
                        ratingObject.setTotal(total+rating);
                        ratingObject.setNum(++num);
                    }

                }
                mutableData.setValue(ratingObject);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(),"데이터 등록 완료",Toast.LENGTH_SHORT).show();
                loading.dismiss();
                finish();
            }
        });
    }

    @OnClick(R.id.rank_review_register_submit)
    public void reviewSubmit(){
        if(checkContents() && reviewObject!=null){
            reviewModifyRegister();
        }
        else if(checkContents()) {
            reviewRegister();
        }
    }

    @OnClick(R.id.rank_register_back)
    public void rankRegisterBackClick(){
        finish();
    }
}
