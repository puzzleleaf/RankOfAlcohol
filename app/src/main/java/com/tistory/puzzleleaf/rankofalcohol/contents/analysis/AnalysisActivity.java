package com.tistory.puzzleleaf.rankofalcohol.contents.analysis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.tistory.puzzleleaf.rankofalcohol.R;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbAuth;
import com.tistory.puzzleleaf.rankofalcohol.fb.FbDataBase;
import com.tistory.puzzleleaf.rankofalcohol.contents.analysis.chart.AnalysisPieChart;
import com.tistory.puzzleleaf.rankofalcohol.model.AnalysisValueObject;
import com.tistory.puzzleleaf.rankofalcohol.contents.analysis.chart.AnalysisBarChart;
import com.tistory.puzzleleaf.rankofalcohol.util.progress.Loading;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

/**
 * Created by cmtyx on 2017-08-13.
 */

public class AnalysisActivity extends AppCompatActivity
        implements OnChartValueSelectedListener , AnalysisRegisterDialog.OnRegisterClickListener{

    @BindView(R.id.chart) BarChart barChart;
    @BindView(R.id.pieChart) PieChart pieChart;
    @BindView(R.id.analysis_info) ImageView analysisInfo;
    @BindView(R.id.analysis_report) ImageView analysisReport;
    @BindView(R.id.analysis_info_count) LinearLayout analysisInfoCount;
    @BindView(R.id.analysis_name) TextView analysisName;
    @BindView(R.id.analysis_num) TextView analysisNum;
    @BindView(R.id.analysis_total) TextView analysisTotal;
    @BindView(R.id.analysis_average) TextView analysisAveraeg;
    @BindView(R.id.analysis_month) TextView analysisMonth;
    @BindView(R.id.analysis_year) TextView analysisYear;
    @BindView(R.id.analysis_alcohol_count) TextView analysisAlcoholCount;
    @BindView(R.id.analysis_alcohol_over_count) TextView analysisAlcoholOverCount;
    @BindView(R.id.analysis_month_soju) TextView analysisMonthSoju;
    @BindView(R.id.analysis_month_beer) TextView analysisMonthBeer;
    @BindView(R.id.analysis_month_makgeolli) TextView analysisMonthMakgeolli;
    @BindView(R.id.analysis_month_etc) TextView analysisMonthEtc;
    @BindView(R.id.analysis_total_sum) TextView analysisTotalSum;
    @BindView(R.id.analysis_total_soju) TextView analysisTotalSoju;
    @BindView(R.id.analysis_total_beer) TextView analysisTotalBeer;
    @BindView(R.id.analysis_total_mak) TextView analysisTotalMak;
    @BindView(R.id.analysis_total_etc) TextView getAnalysisTotalEtc;

    private AnalysisRegisterDialog analysisRegisterDialog;

    private List<AnalysisValueObject> analysisDataList;
    private AnalysisBarChart analysisBarChart;
    private AnalysisPieChart analysisPieChart;

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
        showCase();
        analysisName.setText(FbAuth.mAuth.getCurrentUser().getDisplayName());
        analysisNum.setText(String.valueOf(FbAuth.mUser.gethMany()));
        CalendarView c;


        analysisDataLoad();
    }

    private void analysisDataLoad(){
        loading.show();
        FbDataBase.database.getReference()
                .child("Analysis")
                .child(FbAuth.mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                analysisDataList.clear();
                countReset();
                for(int i=1;i<=lastDayOfMonth;i++){

                    AnalysisValueObject analysisValueObject = dataSnapshot.child(analysisDataKey).child(String.valueOf(i)).getValue(AnalysisValueObject.class);
                    if(analysisValueObject==null){
                        analysisValueObject = new AnalysisValueObject(); // 값이 없는 경우 0병 마신 것으로 간주
                    }else{
                        if(analysisValueObject.getTotal()>FbAuth.mUser.gethMany()){
                            alcoholOverCount++;
                        }
                        if(analysisValueObject.getTotal()!=0) {
                            alcoholCount++;
                        }
                    }

                    analysisDataList.add(analysisValueObject);
                    monthTotal += analysisValueObject.getTotal();
                    analysisTotal.setText(String.format("%.2f",monthTotal));
                    analysisAlcoholOverCount.setText(String.valueOf(alcoholOverCount));
                    analysisAlcoholCount.setText(String.valueOf(alcoholCount));

                    if(alcoholCount !=0) {
                        analysisAveraeg.setText(String.format("%.2f",monthTotal/alcoholCount));
                    }else{
                        analysisAveraeg.setText(String.valueOf(0));
                    }
                }//For end

                if(year==changeYear && month == changeMonth) {
                    analysisBarChart.refreshData(analysisDataList, day);
                }else{
                    analysisBarChart.refreshData(analysisDataList, changeDay);
                }
                //전체 데이터
                AnalysisValueObject analysisTotalObject = dataSnapshot.child("Total").getValue(AnalysisValueObject.class);
                if(analysisTotalObject == null){
                    analysisTotalObject = new AnalysisValueObject();
                }
                analysisReportTotalInit(analysisTotalObject);

                //월별 데이터
                AnalysisValueObject analysisMonthObject = dataSnapshot.child(analysisDataKey).child("Total").getValue(AnalysisValueObject.class);
                if(analysisMonthObject == null){
                    analysisMonthObject = new AnalysisValueObject();
                }

                analysisPieChart.refereshData(analysisMonthObject);
                analysisMonthData(analysisMonthObject);

                loading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //@TODO 데이터 갱신 예외처리 할 것
            }
        });
    }

    private void analysisReportTotalInit(AnalysisValueObject analysisTotalObject){
        analysisTotalSum.setText(String.format("%.2f",analysisTotalObject.getTotal()));
        analysisTotalSoju.setText(String.format("%.2f",analysisTotalObject.getSoju()));
        analysisTotalMak.setText(String.format("%.2f",analysisTotalObject.getMakgeolli()));
        analysisTotalBeer.setText(String.format("%.2f",analysisTotalObject.getBeer()));
        getAnalysisTotalEtc.setText(String.format("%.2f",analysisTotalObject.getEtc()));
    }

    private void analysisMonthData(AnalysisValueObject analysisMonthObject){
        analysisMonthSoju.setText(String.format("%.2f",analysisMonthObject.getSoju()));
        analysisMonthBeer.setText(String.format("%.2f",analysisMonthObject.getBeer()));
        analysisMonthMakgeolli.setText(String.format("%.2f",analysisMonthObject.getMakgeolli()));
        analysisMonthEtc.setText(String.format("%.2f",analysisMonthObject.getEtc()));
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
            Toast.makeText(this,"미래는 분석할 수 없습니다.",Toast.LENGTH_SHORT).show();
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


    private void init(){
        analysisRegisterDialog = new AnalysisRegisterDialog(this,this);
        analysisBarChart = new AnalysisBarChart(barChart,this);
        analysisPieChart = new AnalysisPieChart(pieChart,this);
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
    public void onRegisterClick(final int day) {
        loading.show();
        final AnalysisValueObject analysisValueObject = analysisRegisterDialog.analysisRegisterValue();

        FbDataBase.database.getReference()
                .child("Analysis")
                .child(FbAuth.mAuth.getCurrentUser().getUid())
                .runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                MutableData totalMutableData = mutableData.child("Total");
                MutableData monthTotalMutableData = mutableData.child(analysisDataKey).child("Total");
                MutableData dayMutableData = mutableData.child(analysisDataKey).child(String.valueOf(day));

                //전체 음주 량
                AnalysisValueObject analysisValueObjectTotalTransaction = totalMutableData.getValue(AnalysisValueObject.class);
                //월별 전체 음주 량
                AnalysisValueObject analysisValueObjectMonthTotalTransaction = monthTotalMutableData.getValue(AnalysisValueObject.class);
                //일일 음주 량
                AnalysisValueObject analysisValueObjectDayTransaction = dayMutableData.getValue(AnalysisValueObject.class);

                // null Check
                if(analysisValueObjectTotalTransaction == null) {
                    analysisValueObjectTotalTransaction = new AnalysisValueObject();
                }
                if(analysisValueObjectMonthTotalTransaction == null){
                    analysisValueObjectMonthTotalTransaction = new AnalysisValueObject();
                }
                if(analysisValueObjectDayTransaction == null){
                    analysisValueObjectDayTransaction = new AnalysisValueObject();
                }

                //기존 값 갱신을 위한 감소
                analysisValueDataDecreaseUpdate(analysisValueObjectTotalTransaction,analysisValueObjectMonthTotalTransaction);
                analysisValueDataDecreaseUpdate(analysisValueObjectMonthTotalTransaction,analysisValueObjectDayTransaction);


                //새로운 값 업데이트
                analysisValueDataIncreaseUpdate(analysisValueObjectMonthTotalTransaction,analysisValueObject);
                analysisValueDataIncreaseUpdate(analysisValueObjectTotalTransaction,analysisValueObjectMonthTotalTransaction);


                totalMutableData.setValue(analysisValueObjectTotalTransaction);
                monthTotalMutableData.setValue(analysisValueObjectMonthTotalTransaction);
                dayMutableData.setValue(analysisValueObject);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                analysisDataLoad();
            }
        });


    }

    private void analysisValueDataDecreaseUpdate(AnalysisValueObject analysisOutterObject, AnalysisValueObject analysisInnerObject){
        analysisOutterObject.setSoju(analysisOutterObject.getSoju() - analysisInnerObject.getSoju());
        analysisOutterObject.setBeer(analysisOutterObject.getBeer() - analysisInnerObject.getBeer());
        analysisOutterObject.setMakgeolli(analysisOutterObject.getMakgeolli() - analysisInnerObject.getMakgeolli());
        analysisOutterObject.setEtc(analysisOutterObject.getEtc() - analysisInnerObject.getEtc());
        analysisOutterObject.setTotal(analysisOutterObject.getTotal() - analysisInnerObject.getTotal());
    }

    private void analysisValueDataIncreaseUpdate(AnalysisValueObject analysisOutterObject, AnalysisValueObject analysisInnerObject){
        analysisOutterObject.setSoju(analysisOutterObject.getSoju() + analysisInnerObject.getSoju());
        analysisOutterObject.setBeer(analysisOutterObject.getBeer() + analysisInnerObject.getBeer());
        analysisOutterObject.setMakgeolli(analysisOutterObject.getMakgeolli() + analysisInnerObject.getMakgeolli());
        analysisOutterObject.setEtc(analysisOutterObject.getEtc() + analysisInnerObject.getEtc());
        analysisOutterObject.setTotal(analysisOutterObject.getTotal() + analysisInnerObject.getTotal());
    }

    private void showCase(){
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, "vv");
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(0);
        config.setContentTextColor(ContextCompat.getColor(this,R.color.colorWhite));
        config.setMaskColor(ContextCompat.getColor(this,R.color.showCase));
        config.setDismissTextColor(ContextCompat.getColor(this,R.color.materialBlue));
        sequence.setConfig(config);

        sequence.addSequenceItem(barChart,
                "월별 막대 그래프 입니다.\n\n차트를 터치하면 마신 술의 양을 등록할 수 있고, 일별로 마신 양을 확인할 수 있습니다.","확인");

        sequence.addSequenceItem(pieChart,
                "월별 원형 그래프 입니다.\n\n내가 등록한 술의 알콜의 비율을 %로 나타냅니다.","확인");

        sequence.addSequenceItem(analysisInfoCount,
                "월별 술의 비율 입니다.\n\n소주 1병을 기준으로 내가 마신 알콜의 양을 나타냅니다.\n\nex)맥주 : 1.50\n(맥주를 소주 1.5병 만큼 마셨다.)","확인");

        sequence.addSequenceItem(analysisReport,
                "분석 리포트 입니다.\n\n월별 술의 양에 대한 통계와, 내가 마신 전체 술의 양에 대해서 나타냅니다.","확인");

        sequence.addSequenceItem(analysisInfo,
                "등록 가이드 입니다.\n\n소주 1병을 기준으로 어떻게 변환되는지 나타냅니다.","확인");
        sequence.start();
    }
}
