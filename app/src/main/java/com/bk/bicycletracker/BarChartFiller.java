package com.bk.bicycletracker;

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

    public BarChartFiller(BarChart barchart) {
        this.barchart = barchart;
    }

    public float fillWeeklyData(DistanceCalculator distanceCalculator, Calendar week) {
        barchart.clear();
        List<BarEntry> entries = new ArrayList<BarEntry>();
        float totalWeek = 0;
        final String[] days = new String[7];
        week.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //TODO: set according to locale

        for (int i = 0; i < 7; i++)
        {
            float trackDay = distanceCalculator.getDistanceForDay(week);
            days[i] = week.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
            entries.add(new BarEntry(i, trackDay));
            totalWeek += trackDay;
            week.add(Calendar.DAY_OF_YEAR, 1);
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


        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setLabel("");


        BarData bd = new BarData(dataSet);
        bd.setValueFormatter(new BarChartFiller.KMValueFormater());
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
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

        barchart.setTouchEnabled(false);
        barchart.setDescription("");
        barchart.setData(bd);
        barchart.invalidate();
        return totalWeek;
    }

    public class KMValueFormater implements ValueFormatter {


        public KMValueFormater() {

        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return Math.round(value * 100.0)/100.0 + " km";
        }
    }
}
