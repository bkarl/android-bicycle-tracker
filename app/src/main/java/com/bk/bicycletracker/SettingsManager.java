package com.bk.bicycletracker;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    public SettingsManager(Context context){
        this.sharedPref = context.getSharedPreferences("BicycleTrackerPrefs", Context.MODE_PRIVATE);
        this.editor = sharedPref.edit();
    }


    float getBiasDistanceForDay(Weekday weekday) {
        return sharedPref.getFloat("bias_km_"+weekday.getSuffix(),0);
    }

    void setBiasDistanceForDay(Weekday weekday, float newBiasValue) {
        editor.putFloat("bias_km_"+weekday.getSuffix(), newBiasValue);
    }

    int getWeeklyGoalInKm() {
        return sharedPref.getInt("weeklyGoal",100);
    }

    void setWeeklyGoalInKm(int newWeeklyGoal) {
        editor.putInt("weeklyGoal", newWeeklyGoal);
    }

    void commitChanges() {
        editor.commit();
    }
}

