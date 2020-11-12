package com.bk.bicycletracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ActivitySettings extends AppCompatActivity {

    private EditText txtWeekyGoal;
    private SettingsManager settingsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settingsManager = new SettingsManager(this);

        int weeklyGoal = settingsManager.getWeeklyGoalInKm();
        txtWeekyGoal = (EditText) findViewById(R.id.settingsGoal);
        txtWeekyGoal.setText(Integer.toString(weeklyGoal));

        populateBiasFieldsPerDay();

        TextView licenseLauncher = findViewById(R.id.licenseLauncher);
        licenseLauncher.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void populateBiasFieldsPerDay() {
        EditText txtBiasKm;
        for (Weekday w : Weekday.values()) {
            float bias_km = settingsManager.getBiasDistanceForDay(w);
            txtBiasKm = (EditText) findViewById(w.getRessourceID());
            txtBiasKm.setText(Float.toString(bias_km));
        }
    }

    private void saveChangedBiassesToSettings() {
        EditText txtBiasKm;
        for (Weekday w : Weekday.values()) {
            txtBiasKm = (EditText) findViewById(w.getRessourceID());
            settingsManager.setBiasDistanceForDay(w, Float.parseFloat(txtBiasKm.getText().toString()));
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        settingsManager.setWeeklyGoalInKm(Integer.parseInt(txtWeekyGoal.getText().toString()));
        saveChangedBiassesToSettings();
        settingsManager.commitChanges();
    }
}
