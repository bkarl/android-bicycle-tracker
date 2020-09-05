package com.bk.bicycletracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ActivitySettings extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPref = getSharedPreferences("BicycleTrackerPrefs", Context.MODE_PRIVATE);

        int weeklyGoal = sharedPref.getInt("weeklyGoal",100);
        editText = (EditText) findViewById(R.id.settingsGoal);
        editText.setText(Integer.toString(weeklyGoal));

        TextView licenseLauncher = findViewById(R.id.licenseLauncher);
        licenseLauncher.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("weeklyGoal", Integer.parseInt(editText.getText().toString()));
        editor.commit();
    }
}
