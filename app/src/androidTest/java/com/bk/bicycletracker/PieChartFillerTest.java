package com.bk.bicycletracker;

import androidx.test.InstrumentationRegistry;
import com.github.mikephil.charting.charts.PieChart;

import org.junit.Assert;
import org.junit.Test;


public class PieChartFillerTest {

    @Test
    public void fillWeeklyData() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                testDistanceLessThanWeeklyGoal();
                testDistanceMoreThanWeeklyGoal();
            }
        });
    }

    private void testDistanceLessThanWeeklyGoal(){
        PieChart pc = new PieChart(InstrumentationRegistry.getInstrumentation().getTargetContext());
        PieChartFiller pcf = new PieChartFiller(pc);

        float weeklyGoal = 100.0f;
        double distanceWeekInKm = 75.7433333333;
        pcf.fillWeeklyData(distanceWeekInKm, weeklyGoal);
        Assert.assertEquals(pc.getData().getDataSet().getEntryCount(), 2);
        Assert.assertEquals(pc.getData().getDataSet().getEntryForIndex(0).getValue(), distanceWeekInKm, 0.0001);
        Assert.assertEquals(pc.getData().getDataSet().getEntryForIndex(1).getValue(), (float)(weeklyGoal-distanceWeekInKm), 0.0001);
        Assert.assertEquals(75.74+" %", pc.getCenterText());
    }

    private void testDistanceMoreThanWeeklyGoal(){
        PieChart pc = new PieChart(InstrumentationRegistry.getInstrumentation().getTargetContext());
        PieChartFiller pcf = new PieChartFiller(pc);

        float weeklyGoal = 100.0f;
        double distanceWeekInKm = 110.7799999999;
        pcf.fillWeeklyData(distanceWeekInKm, weeklyGoal);
        Assert.assertEquals(pc.getData().getDataSet().getEntryCount(), 1);
        Assert.assertEquals(pc.getData().getDataSet().getEntryForIndex(0).getValue(), distanceWeekInKm, 0.0001);
        Assert.assertEquals(110.78f+" %", pc.getCenterText());
    }
}