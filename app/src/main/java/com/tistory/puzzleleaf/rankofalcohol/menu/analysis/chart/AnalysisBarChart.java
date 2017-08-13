package com.tistory.puzzleleaf.rankofalcohol.menu.analysis.chart;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.tistory.puzzleleaf.rankofalcohol.model.AnalysisValueObject;
import com.tistory.puzzleleaf.rankofalcohol.menu.analysis.chart.formatter.XAxisValueFormatter;
import com.tistory.puzzleleaf.rankofalcohol.menu.analysis.chart.formatter.YAxisValueFormatter;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by cmtyx on 2017-08-13.
 */

public class AnalysisBarChart {

    private BarChart barChart;
    //ChartData
    private OnChartValueSelectedListener onChartValueSelectedListener;
    private List<BarEntry> yNormal;
    private List<BarEntry> yOver;
    private List<BarEntry> yToday;
    private ArrayList<IBarDataSet> dataSets;
    private BarData barData;
    private BarDataSet normalSet;
    private BarDataSet overSet;
    private BarDataSet todaySet;

    public AnalysisBarChart(BarChart barChart, OnChartValueSelectedListener onChartValueSelectedListener){
        this.barChart = barChart;
        this.onChartValueSelectedListener = onChartValueSelectedListener;
        barChartInit();
        initData();
        setData();
    }

    private void barChartInit(){
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        barChart.setMaxVisibleValueCount(1);
        barChart.setDrawGridBackground(false);
        barChart.setScaleMinima(4f,1f);
        barChart.getAxisRight().setEnabled(false);
        barChart.animateY(1000);
        barChart.setOnChartValueSelectedListener(onChartValueSelectedListener);

        barXAxisInit();
        barYAxisInit();
        barLegendInit();
    }

    private void barXAxisInit(){

        IAxisValueFormatter xAxisFormatter = new XAxisValueFormatter();

        XAxis xAxis = barChart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);
    }

    private void barYAxisInit(){

        IAxisValueFormatter yAxisFormatter = new YAxisValueFormatter();

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(8);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(10f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setValueFormatter(yAxisFormatter);
    }

    private void barLegendInit(){
        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);
        l.setXEntrySpace(7f);
    }

    private void initData() {
        yOver = new ArrayList<BarEntry>();
        yNormal = new ArrayList<BarEntry>();
        yToday = new ArrayList<BarEntry>();
        dataSets = new ArrayList<>();
        barData = new BarData(dataSets);

        normalSet = new BarDataSet(yNormal,"보통");
        overSet = new BarDataSet(yOver,"주량 이상");
        todaySet = new BarDataSet(yToday,"오늘");

        normalSet.setColor(Color.WHITE);
        overSet.setColor(Color.CYAN);
        todaySet.setColor(Color.YELLOW);
    }


    private void setData(){

        for(int i=1;i<30;i++){
            float temp = 0;
            yNormal.add(new BarEntry(i,temp));
        }

        normalSet.notifyDataSetChanged();
        overSet.notifyDataSetChanged();
        todaySet.notifyDataSetChanged();

        dataSets.add(normalSet);
        dataSets.add(overSet);
        dataSets.add(todaySet);

        barData.notifyDataChanged();
        barData.setValueTextSize(10f);
        barData.setBarWidth(0.8f);
        barData.setValueTextColor(Color.WHITE);

        barChart.setData(barData);
    }

    public void refreshData(List<AnalysisValueObject> analysisDataList, int day){

        barChart.centerViewToAnimated(day,0, YAxis.AxisDependency.LEFT,1000);

        yOver.clear();
        yNormal.clear();
        yToday.clear();

        for(int i=1;i<analysisDataList.size()+1;i++){
            float temp = analysisDataList.get(i-1).getNum();
            if(i==day){
                yToday.add(new BarEntry(i,temp));
                continue;
            }
            if(temp>3)
                yOver.add(new BarEntry(i, temp));
            else
                yNormal.add(new BarEntry(i,temp));
        }

        normalSet.notifyDataSetChanged();
        overSet.notifyDataSetChanged();
        todaySet.notifyDataSetChanged();

        dataSets.clear();
        dataSets.add(normalSet);
        dataSets.add(overSet);
        dataSets.add(todaySet);


        barData.setValueTextColor(Color.RED);
        barData.notifyDataChanged();
        barChart.notifyDataSetChanged();
        barChart.animateY(1000);

    }

}
