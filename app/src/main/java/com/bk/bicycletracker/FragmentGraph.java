package com.bk.bicycletracker;


import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bk.bicycletracker.DatabaseOperations.DistanceCalculator;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class FragmentGraph extends TimeDependentFragment {

    private TextView txtDistanceWeek;
    private BarChart barChart;
    public FragmentGraph() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void fillBarChart()
    {
        BarChartFiller barChartFiller = new BarChartFiller(barChart);
        float totalWeek = barChartFiller.fillWeeklyData(new DistanceCalculator(getContext()), (Calendar)this.currentWeek.clone());
        txtDistanceWeek.setText(Math.round(totalWeek / 10.0)/100.0 + " km");
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
