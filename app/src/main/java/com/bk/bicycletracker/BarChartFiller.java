package com.bk.bicycletracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.bk.bicycletracker.DatabaseOperations.DistanceCalculator;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BarChartFiller {
    private BarChart barchart;
    private int[] COLORFUL_COLORS_BIAS;

    public BarChartFiller(BarChart barchart) {
        this.barchart = barchart;
    }

    public float fillWeeklyData(Context context, Calendar week) {
        DistanceCalculator distanceCalculator =  new DistanceCalculator(context);
        SettingsManager settingsManager = new SettingsManager(context);

        barchart.clear();
        List<BarEntry> entries = new ArrayList<BarEntry>();
        float totalWeek = 0;
        final String[] days = new String[7];
        week.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //TODO: set according to locale
        int idxWeekDay = 0;
        for (Weekday w: Weekday.values())
        {
            float trackDay = distanceCalculator.getDistanceForDay(week);
            float biasForDay = settingsManager.getBiasDistanceForDay(w);
            //if (idxWeekDay < bias_number_of_days)
            trackDay += biasForDay;

            days[idxWeekDay] = week.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
            entries.add(new BarEntry(idxWeekDay, trackDay));
            if (biasForDay != 0)
                entries.add(new BarEntry(idxWeekDay, biasForDay));
            else
                entries.add(new BarEntry(idxWeekDay, 0));
            totalWeek += trackDay;
            week.add(Calendar.DAY_OF_YEAR, 1);
            idxWeekDay++;
        }

        AxisValueFormatter formatter = new AxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return days[(int) value];
            }

            // we don't draw numbers, so no decimal digits needed
            @Override
            public int getDecimalDigits() {  return 0; }
        };

        generateColorForBiasedTracks(7);
        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setLabel("");
        dataSet.setColors(COLORFUL_COLORS_BIAS);

        BarData bd = new BarData(dataSet);
        bd.setValueFormatter(new BarChartFiller.KMValueFormater());
        barchart.getXAxis().setDrawGridLines(false);
        barchart.getXAxis().setDrawAxisLine(false);
        barchart.getXAxis().setDrawLabels(true);
        barchart.getXAxis().setValueFormatter(formatter);
        barchart.getXAxis().setGranularity(1);
        barchart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        barchart.setFitBars(true);

        barchart.getAxisLeft().setDrawGridLines(false);
        barchart.getAxisLeft().setDrawAxisLine(false);
        barchart.getAxisLeft().setDrawLabels(false);
        barchart.getAxisLeft().setDrawZeroLine(true);

        barchart.getAxisRight().setEnabled(false);

        barchart.setDescription("");
        barchart.setData(bd);
        barchart.getLegend().setEnabled(false);
        barchart.invalidate();
        return totalWeek;
    }

    void generateColorForBiasedTracks(int weeksdays) {
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
            // write your logic here
            if (value == 0)
                return "";

            return Math.round(value * 100.0)/100.0 + " km";
        }
    }
}
