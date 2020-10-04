package com.bk.bicycletracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bk.bicycletracker.DatabaseOperations.DistanceCalculator;
import com.github.mikephil.charting.charts.PieChart;

import java.util.Calendar;

public class FragmentPiechartWeek extends TimeDependentFragment {
    private static final String TAG = "FragmentPiechartWeek";

    private TextView txtDistance;
    private double distanceWeekInKm;
    private float weeklyGoal;
    private PieChart pc;

    public FragmentPiechartWeek() {
        // Required empty public constructor
    }

    @Override
    public void dateChanged(Calendar newDate) {
        this.currentWeek = newDate;
        calculateDistanceOfCurrentWeek();
        updateTimeDependentStrings();
        fillPieChart(pc);
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
        View rootView = inflater.inflate(R.layout.fragment_tracks_week_piechart, container, false);
        txtDistance = (TextView) rootView.findViewById(R.id.txtDistance);
        txtWeekorDate  = (TextView) rootView.findViewById(R.id.headlineDistanceWeek);
        getWeeklyDistanceGoalFromSettings();
        pc = (PieChart) rootView.findViewById(R.id.barchart);

        final GestureDetector gesture = new GestureDetector(getActivity(), new SwipeGestureDetector(this));

        pc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gesture.onTouchEvent(motionEvent);
            }
        });

        this.dateChanged(this.currentWeek);

        return rootView;
    }

    @Override
    protected void updateTimeDependentStrings()
    {
        super.updateTimeDependentStrings();
        txtDistance.setText(distanceWeekInKm + " / " + weeklyGoal+ "km");
    }

    private void fillPieChart(PieChart pc) {
        new PieChartFiller(pc).fillWeeklyData(distanceWeekInKm, weeklyGoal);
    }

    private void calculateDistanceOfCurrentWeek() {
        DistanceCalculator distanceCalculator = new DistanceCalculator(this.getContext());
        distanceWeekInKm = distanceCalculator.getDistanceForWeek(currentWeek);
        distanceWeekInKm = Math.round(distanceWeekInKm * 100.0) / 100.0;
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
