package com.bk.bicycletracker;


import android.os.Bundle;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bk.bicycletracker.DatabaseOperations.DistanceCalculator;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;

import java.util.Calendar;
import java.util.LinkedList;


public class FragmentBarChartWeek extends TimeDependentFragment {

    private TextView txtDistanceWeek;
    private BarChart barChart;
    public FragmentBarChartWeek() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void fillBarChart()
    {
        DistanceCalculator distanceCalculator =  new DistanceCalculator(getContext());
        SettingsManager settingsManager = new SettingsManager(getContext());

        BarChartFiller barChartFiller = new BarChartFiller(distanceCalculator, settingsManager);
        BarchartDrawer barchartDrawer = new BarchartDrawer(barChart);
        LinkedList<BarEntry> entries = new LinkedList<>();

        float totalWeek = barChartFiller.generateBarChartEntries((Calendar)this.currentWeek.clone(), entries);
        barchartDrawer.drawData(entries);
        txtDistanceWeek.setText(Math.round(totalWeek * 100.0)/100.0 + " km");
    }

    @Override
    public void dateChanged(Calendar newDate) {
        this.currentWeek = newDate;
        updateTimeDependentStrings();
        fillBarChart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_tracks_barchart, container, false);
        txtWeekorDate  = (TextView) rootView.findViewById(R.id.headlineDistanceWeek);

        barChart = (BarChart) rootView.findViewById(R.id.chart);
        txtDistanceWeek = (TextView) rootView.findViewById(R.id.txtDistanceWeek);
        fillBarChart();

        final GestureDetector gesture = new GestureDetector(getActivity(), new SwipeGestureDetector(this));

        barChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gesture.onTouchEvent(motionEvent);
            }
        });
        return rootView;
    }

}
