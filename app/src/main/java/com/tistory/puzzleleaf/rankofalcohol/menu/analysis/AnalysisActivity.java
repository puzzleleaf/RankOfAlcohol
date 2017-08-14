package com.tistory.puzzleleaf.rankofalcohol.menu.analysis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbAuth;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbDataBase;
import com.tistory.puzzleleaf.rankofalcohol.model.AnalysisValueObject;
import com.tistory.puzzleleaf.rankofalcohol.menu.analysis.chart.AnalysisBarChart;
import com.tistory.puzzleleaf.rankofalcohol.util.progress.Loading;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cmtyx on 2017-08-13.
 */

public class AnalysisActivity extends AppCompatActivity
        implements OnChartValueSelectedListener , AnalysisRegisterDialog.OnRegisterClickListener{

    @BindView(R.id.chart) BarChart barChart;
    @BindView(R.id.analysis_name) TextView analysisName;
    @BindView(R.id.analysis_num) TextView analysisNum;
    @BindView(R.id.analysis_total) TextView analysisTotal;
    @BindView(R.id.analysis_average) TextView analysisAveraeg;
    @BindView(R.id.analysis_month) TextView analysisMonth;
    @BindView(R.id.analysis_year) TextView analysisYear;
    @BindView(R.id.analysis_alcohol_count) TextView analysisAlcoholCount;
    @BindView(R.id.analysis_alcohol_over_count) TextView analysisAlcoholOverCount;
    @BindView(R.id.analysis_comment) TextView analysisComment;

    private AnalysisRegisterDialog analysisRegisterDialog;

    private List<AnalysisValueObject> analysisDataList;
    private AnalysisBarChart analysisBarChart;

    private Loading loading;

    //count
    private int alcoholCount;
    private int alcoholOverCount;

    //Calendar
    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    private int changeYear;
    private int changeMonth;
    private int changeDay;

    private int lastDayOfMonth;
    private float monthTotal;
    private String analysisDataKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        ButterKnife.bind(this);
        init();
        calenderInit();

        analysisName.setText(FbAuth.mAuth.getCurrentUser().getDisplayName());
        analysisNum.setText(String.valueOf(FbAuth.mUser.gethMany()));

        analysisDataLoad();
    }

    private void analysisDataLoad(){
        loading.show();
        FbDataBase.database.getReference()
                .child("Analysis")
                .child(FbAuth.mAuth.getCurrentUser().getUid())
                .child(analysisDataKey)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                analysisDataList.clear();
                countReset();
                for(int i=1;i<=lastDayOfMonth;i++){
                    AnalysisValueObject analysisValueObject = dataSnapshot.child(String.valueOf(i)).getValue(AnalysisValueObject.class);
                    if(analysisValueObject==null){
                        analysisValueObject = new AnalysisValueObject(0); // 값이 없는 경우 0병 마신 것으로 간주
                    }

                    if(analysisValueObject.getNum()>=FbAuth.mUser.gethMany()){
                        alcoholOverCount++;
                    }
                    if(analysisValueObject.getNum()!=0){
                        alcoholCount++;
                    }

                    analysisDataList.add(analysisValueObject);
                    monthTotal += analysisValueObject.getNum();
                    analysisTotal.setText(String.format("%.2f",monthTotal));
                    analysisAlcoholOverCount.setText(String.valueOf(alcoholOverCount));
                    analysisAlcoholCount.setText(String.valueOf(alcoholCount));
                    if(alcoholCount !=0) {
                        analysisAveraeg.setText(String.format("%.2f",monthTotal/alcoholCount));
                    }else{
                        analysisAveraeg.setText(String.valueOf(0));
                    }
                }
                if(year==changeYear && month == changeMonth) {
                    analysisBarChart.refreshData(analysisDataList, day);
                }else{
                    analysisBarChart.refreshData(analysisDataList, changeDay);
                }
                analysisComment();

                loading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //@TODO 데이터 갱신 예외처리 할 것
            }
        });
    }

    private void countReset(){
        alcoholCount = 0;
        alcoholOverCount = 0;
        monthTotal = 0;
    }

    private void setYearMonth(){
        analysisMonth.setText(String.format("%02d",changeMonth));
        analysisYear.setText(String.valueOf(changeYear));
    }

    private void getLastDayOfMonth(){
        calendar.set(Calendar.YEAR,changeYear);
        calendar.set(Calendar.MONTH,changeMonth-1);
        lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private void calenderInit(){
        calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH)+1;
        year = calendar.get(Calendar.YEAR);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        changeYear = year;
        changeMonth = month;
        changeDay = 100;

        lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        analysisDataKey = String.valueOf(year) + String.valueOf(month);
        setYearMonth();
    }

    private void calendarPrevChange(){
        if(changeMonth == 1){
            changeYear--;
            changeMonth = 12;
        }else{
            changeMonth--;
        }
        analysisDataKey = String.valueOf(changeYear) + String.valueOf(changeMonth);
        setYearMonth();
        getLastDayOfMonth();
        analysisDataLoad();
    }

    private void calendarNextChange(){
        if(changeYear == year && changeMonth == month){
            Toast.makeText(this,"미래는 분석할 수 없습니다...",Toast.LENGTH_SHORT).show();
            return;
        }else if(changeMonth == 12){
            changeYear++;
            changeMonth=1;
        }else{
            changeMonth++;
        }
        analysisDataKey = String.valueOf(changeYear) + String.valueOf(changeMonth);
        setYearMonth();
        getLastDayOfMonth();
        analysisDataLoad();
    }

    private void analysisComment(){
        if(alcoholCount==0) {
            analysisComment.setText("금주하는 습관 좋아요!");
        }else if(alcoholOverCount>alcoholCount){
            analysisComment.setText("주량 이상으로 마시는 날이 많아요!\n주의가 필요합니다.");
        }else if(alcoholOverCount == alcoholCount && lastDayOfMonth/2<alcoholCount){
            analysisComment.setText("딱 주량 만큼 먹는 습관 좋아요!\n그런데 술을 마시는 빈도가 높습니다ㅜㅜ");
        }else if(monthTotal>(monthTotal/alcoholCount)*10){
            analysisComment.setText("적당히 마시는 것은 좋습니다!\n그러나 몰아서 많이 드시는 것 같아요");
        } else if(alcoholOverCount == alcoholCount){
            analysisComment.setText("딱 주량 만큼 먹는 습관 좋아요!");
        }else{
            analysisComment.setText("적당한 음주는 몸에 이로워요!");
        }

    }


    private void init(){
        analysisRegisterDialog = new AnalysisRegisterDialog(this,this);
        analysisBarChart = new AnalysisBarChart(barChart,this);
        analysisDataList = new ArrayList<>();
        loading = new Loading(this,"analysis");
    }

    @OnClick(R.id.analysis_back)
    public void analysisBackClick(){
        finish();
    }

    @OnClick(R.id.analysis_info)
    public void analysisInfoClick(){
        Intent intent = new Intent(this,AnalysisInfoActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.analysis_prev)
    public void analysisPrevClick(){
        calendarPrevChange();
    }

    @OnClick(R.id.analysis_next)
    public void analysisNextClick(){
        calendarNextChange();
    }

    //차트 클릭 리스너
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if((int)e.getX()>day && changeYear==year && changeMonth==month) {
            Toast.makeText(this,"미래의 데이터를 등록할 수 없습니다.",Toast.LENGTH_SHORT).show();
            return;
        }
        analysisRegisterDialog.setDay((int)e.getX());
        analysisRegisterDialog.show();
    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onRegisterClick(int day) {
        AnalysisValueObject analysisValueObject = new AnalysisValueObject(analysisRegisterDialog.analysisRegisterValue());
        FbDataBase.database.getReference()
                .child("Analysis")
                .child(FbAuth.mAuth.getCurrentUser().getUid())
                .child(analysisDataKey)
                .child(String.valueOf(day))
                .setValue(analysisValueObject);
        analysisDataLoad();
    }
}
