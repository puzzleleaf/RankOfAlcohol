package com.tistory.puzzleleaf.rankofalcohol.menu.analysis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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


public class AnalysisActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    @BindView(R.id.chart) BarChart barChart;
    private List<AnalysisValueObject> analysisDataList;
    private AnalysisBarChart analysisBarChart;

    private Loading loading;

    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    private int lastDayOfMonth;
    private String analysisDataKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        ButterKnife.bind(this);
        init();
        calenderInit();
      //  analysisDataLoad();
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
                for(int i=1;i<=lastDayOfMonth;i++){
                    AnalysisValueObject analysisValueObject = dataSnapshot.child(String.valueOf(i)).getValue(AnalysisValueObject.class);
                    if(analysisValueObject==null){
                        analysisValueObject = new AnalysisValueObject(0); // 값이 없는 경우 0병 마신 것으로 간주
                    }
                    analysisDataList.add(analysisValueObject);
                }
                analysisBarChart.refreshData(analysisDataList,day);
                loading.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //@TODO 데이터 갱신 예외처리 할 것
            }
        });
    }

    private void calenderInit(){
        calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH)+1;
        year = calendar.get(Calendar.YEAR);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        analysisDataKey = String.valueOf(year) + String.valueOf(month);
        Log.d("qwe",analysisDataKey);
    }


    private void init(){
        analysisBarChart = new AnalysisBarChart(barChart,this);
        analysisDataList = new ArrayList<>();
        loading = new Loading(this,"analysis");
    }

    @OnClick(R.id.analysis_back)
    public void analysisBackClick(){
        finish();
    }

    //차트 클릭 리스너
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        AnalysisValueObject analysisValueObject = new AnalysisValueObject(3);
        FbDataBase.database.getReference()
                .child("Analysis")
                .child(FbAuth.mAuth.getCurrentUser().getUid())
                .child(analysisDataKey)
                .child(String.valueOf((int)e.getX()))
                .setValue(analysisValueObject);
        analysisDataLoad();
    }

    @Override
    public void onNothingSelected() {

    }
}
