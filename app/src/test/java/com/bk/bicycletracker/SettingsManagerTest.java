package com.bk.bicycletracker;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SettingsManagerTest {

    private SharedPreferences sharedPref;
    private SettingsManager dut;
    SharedPreferences.Editor editor;
    @Before
    public void setUp() {
        Context c = mock(Context.class);
        sharedPref = mock(SharedPreferences.class);
        editor = mock(SharedPreferences.Editor.class);
        when(c.getSharedPreferences("BicycleTrackerPrefs", Context.MODE_PRIVATE)).thenReturn(sharedPref);
        when(sharedPref.edit()).thenReturn(editor);
        dut = new SettingsManager(c);
    }


    @Test
    public void getBiasDistanceForDay() {
        when(sharedPref.getFloat("bias_km_mon", 0)).thenReturn(25081991.0f);
        Assert.assertEquals(dut.getBiasDistanceForDay(Weekday.MONDAY), 25081991.0f, 0.01);
    }

    @Test
    public void setBiasDistanceForDay() {
        dut.setBiasDistanceForDay(Weekday.MONDAY, 1234.56f);
        verify(editor).putFloat("bias_km_mon", 1234.56f);
    }

    @Test
    public void getWeeklyGoalInKm_set() {
        when(sharedPref.getInt("weeklyGoal", 100)).thenReturn(69);
        Assert.assertEquals(dut.getWeeklyGoalInKm(), 69);
    }

    @Test
    public void setWeeklyGoalInKm() {
        dut.setWeeklyGoalInKm(77);
        verify(editor).putInt("weeklyGoal", 77);
    }

    @Test
    public void commitChanges() {
        dut.commitChanges();
        verify(editor).commit();
    }
}