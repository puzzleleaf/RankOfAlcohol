package com.tistory.puzzleleaf.rankofalcohol.menu.analysis.chart;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.tistory.puzzleleaf.rankofalcohol.R;

import java.util.ArrayList;

/**
 * Created by cmtyx on 2017-08-16.
 */

public class AnalysisPieChart {
    private PieChart pieChart;
    private String[] dataName ={"소주","맥주","막걸리","기타"};
    private Context context;

    public AnalysisPieChart(PieChart pieChart,Context context){
        this.pieChart = pieChart;
        this.context = context;
        pieChartInit();
        pieLegendInit();
        initData();
    }

    private void pieChartInit(){
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("내가 마시는 술의 비율");
        pieChart.setCenterTextColor(Color.WHITE);
        pieChart.setDragDecelerationFrictionCoef(1f);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(50f);
        pieChart.setTransparentCircleRadius(51f);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.setHoleColor(Color.BLACK);
    }

    private void pieLegendInit(){
        Legend l = pieChart.getLegend();
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

    private void initData(){

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < 4 ; i++) {
            entries.add(new PieEntry((float)(5),dataName[i]));
        }

        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setSliceSpace(3f);

        ArrayList<Integer> colors = new ArrayList<Integer>();



        colors.add(ContextCompat.getColor(context, R.color.materialGreen));
        colors.add(ContextCompat.getColor(context, R.color.materialYellow));
        colors.add(ContextCompat.getColor(context, R.color.materialRed));
        colors.add(ContextCompat.getColor(context, R.color.materialBlue));



        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);
    }

}
