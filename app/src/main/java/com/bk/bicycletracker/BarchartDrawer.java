package com.bk.bicycletracker;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

public class BarchartDrawer {
    private int[] COLORFUL_COLORS_BIAS;
    private BarChart barChart;
    final String[] days = {
            "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
    };


    public BarchartDrawer(BarChart barchart) {
        this.barChart = barchart;
    }

    private AxisValueFormatter formatter = new AxisValueFormatter() {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return days[(int) value];
        }

        // we don't draw numbers, so no decimal digits needed
        @Override
        public int getDecimalDigits() {  return 0; }
    };


    public void drawData(List<BarEntry> entries) {
        barChart.clear();

        generateColorForBiasedTracks(7);
        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setLabel("");
        dataSet.setColors(COLORFUL_COLORS_BIAS);

        BarData bd = new BarData(dataSet);
        bd.setValueFormatter(new BarchartDrawer.KMValueFormater());
        formatChart();

        barChart.setData(bd);
        barChart.invalidate();
    }

    private void formatChart() {
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getXAxis().setDrawLabels(true);
        barChart.getXAxis().setValueFormatter(formatter);
        barChart.getXAxis().setGranularity(1);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.setFitBars(true);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisLeft().setDrawZeroLine(true);

        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setDescription("");
    }

    private void generateColorForBiasedTracks(int weeksdays) {
        int[] COLORFUL_COLORS_BIAS_GEN = new int[ColorTemplate.COLORFUL_COLORS.length + weeksdays];
        for (int i = 0; i < ColorTemplate.COLORFUL_COLORS.length + weeksdays; i++)
            if (i % 2 == 1)
                COLORFUL_COLORS_BIAS_GEN[i] = Color.rgb(128, 128, 128);
            else
                COLORFUL_COLORS_BIAS_GEN[i] = ColorTemplate.COLORFUL_COLORS[i % ColorTemplate.COLORFUL_COLORS.length];

        this.COLORFUL_COLORS_BIAS = COLORFUL_COLORS_BIAS_GEN;
    }

    public class KMValueFormater implements ValueFormatter {


        public KMValueFormater() {

        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            if (value == 0)
                return "";

            return Math.round(value * 100.0)/100.0 + " km";
        }
    }
}
