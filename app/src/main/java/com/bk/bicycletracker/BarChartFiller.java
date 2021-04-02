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
    private DistanceCalculator distanceCalculator;
    private SettingsManager settingsManager;

    public BarChartFiller(DistanceCalculator distanceCalculator, SettingsManager settingsManager) {
       this.distanceCalculator = distanceCalculator;
       this.settingsManager = settingsManager;
    }

    public float generateBarChartEntries(Calendar week, List<BarEntry> entries) {
        float totalWeek = 0;
        week.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        int idxWeekDay = 0;
        for (Weekday w: Weekday.values())
        {
            float trackDay = distanceCalculator.getDistanceForDay(week);
            float biasForDay = settingsManager.getBiasDistanceForDay(w);

            trackDay += biasForDay;

            entries.add(new BarEntry(idxWeekDay, trackDay));
            if (biasForDay != 0)
                entries.add(new BarEntry(idxWeekDay, biasForDay));
            else
                entries.add(new BarEntry(idxWeekDay, 0));
            totalWeek += trackDay;
            week.add(Calendar.DAY_OF_YEAR, 1);
            idxWeekDay++;
        }
        return totalWeek;
    }
}
