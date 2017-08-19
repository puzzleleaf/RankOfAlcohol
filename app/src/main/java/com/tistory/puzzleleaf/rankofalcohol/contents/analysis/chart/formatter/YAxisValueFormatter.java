package com.tistory.puzzleleaf.rankofalcohol.contents.analysis.chart.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;


public class YAxisValueFormatter implements IAxisValueFormatter
{
    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        String num = String.format("%.1f",value) +"병";
        return num;
    }
}
