package com.bk.bicycletracker;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class PieChartFiller {
    private PieChart pc;

    public PieChartFiller(PieChart pc) {
        this.pc = pc;
    }

    public void fillWeeklyData(double distanceWeekInKm, float weeklyGoal) {

        List<PieEntry> pieEntires = new ArrayList<>();
        pieEntires.add(new PieEntry((float)distanceWeekInKm, ""));
        if (distanceWeekInKm < weeklyGoal)
            pieEntires.add(new PieEntry((float)(weeklyGoal-distanceWeekInKm), ""));

        PieDataSet dataSet = new PieDataSet(pieEntires,"");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);

        double percentDone = (1.0f-(weeklyGoal-distanceWeekInKm)/weeklyGoal)*100.0f;
        percentDone =  Math.round(percentDone * 100.0) / 100.0;
        data.setDrawValues(false);

        pc.setData(data);
        pc.setCenterText(percentDone+" %");
        pc.setRotationEnabled(false);
        pc.setCenterTextSize(34);
        pc.setDescription("");
        pc.setUsePercentValues(true);
        pc.invalidate();
    }
}
