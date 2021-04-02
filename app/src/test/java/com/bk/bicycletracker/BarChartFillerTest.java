package com.bk.bicycletracker;

import android.content.Context;


import com.bk.bicycletracker.DatabaseOperations.DistanceCalculator;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.LinkedList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BarChartFillerTest {

    private DistanceCalculator distanceCalculator;
    private SettingsManager settingsManager;
    private Calendar cal = Calendar.getInstance();

    @Before
    public void setUp() {
        distanceCalculator = mock(DistanceCalculator.class);
        settingsManager = mock(SettingsManager.class);
        when(distanceCalculator.getDistanceForDay(cal)).thenReturn(10.0f);
    }


    private void generateMockedBiasValues() {
        for (Weekday w: Weekday.values()) {
            when(settingsManager.getBiasDistanceForDay(w)).thenReturn(10.0f);
        }
    }

    @Test
    public void testFillWeeklyDataNoBias(){
        LinkedList<BarEntry> entries = new LinkedList<>();
        BarChartFiller bcf = new BarChartFiller(distanceCalculator, settingsManager);
        float distanceWeek = bcf.generateBarChartEntries(cal, entries);
        Assert.assertEquals(70.0f, distanceWeek,0.001f);
    }

    @Test
    public void testFillWeeklyDataWithBias(){
        generateMockedBiasValues();
        LinkedList<BarEntry> entries = new LinkedList<>();
        BarChartFiller bcf = new BarChartFiller(distanceCalculator, settingsManager);
        float distanceWeek = bcf.generateBarChartEntries(cal, entries);
        Assert.assertEquals(140.0f, distanceWeek,0.001f);
    }


}