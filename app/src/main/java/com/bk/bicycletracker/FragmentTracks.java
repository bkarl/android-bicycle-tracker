package com.bk.bicycletracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bk.bicycletracker.DatabaseOperations.DistanceCalculator;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentTracks extends Fragment {

    private TextView txtDistance;
    private double distanceWeekInKm;
    private float weeklyGoal;

    public FragmentTracks() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_tracks, container, false);
        txtDistance = (TextView) rootView.findViewById(R.id.txtDistance);

        calculateDistanceOfCurrentWeek();
        getWeeklyDistanceGoalFromSettings();
        txtDistance.setText(distanceWeekInKm + " / " + weeklyGoal+ "km");

        PieChart pc = (PieChart) rootView.findViewById(R.id.barchart);
        fillPieChart(pc);
        return rootView;
    }

    private void fillPieChart(PieChart pc) {
        new PieChartFiller(pc).fillWeeklyData(distanceWeekInKm, weeklyGoal);
    }

    private void calculateDistanceOfCurrentWeek() {
        DistanceCalculator distanceCalculator = new DistanceCalculator(this.getContext());
        distanceWeekInKm = distanceCalculator.getDistanceForWeek(Calendar.getInstance());
        distanceWeekInKm = Math.round(distanceWeekInKm / 10.0) / 100.0;
    }

    private void getWeeklyDistanceGoalFromSettings()
    {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("BicycleTrackerPrefs",Context.MODE_PRIVATE);
        this.weeklyGoal = (float)sharedPref.getInt("weeklyGoal",100);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
