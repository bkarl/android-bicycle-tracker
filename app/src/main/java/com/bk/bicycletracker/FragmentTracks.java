package com.bk.bicycletracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentTracks extends Fragment {


    private TrackDatabaseHelper mDB;
    private double lastLongitude;
    private double lastLatitude;

    private TextView txtDistance;


    private double overallDistance;


    public FragmentTracks() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTracks.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTracks newInstance(String param1, String param2) {
        FragmentTracks fragment = new FragmentTracks();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_tracks, container, false);
        txtDistance = (TextView) rootView.findViewById(R.id.txtDistance);


        DistanceCalculator distanceCalculator = new DistanceCalculator(this.getContext());
        double distanceWeek = distanceCalculator.getDistanceForWeek(Calendar.getInstance());
        float weeklyGoal = getWeeklyDistanceFromSettings();

        distanceWeek = Math.round(distanceWeek / 10.0) / 100.0;
        txtDistance.setText(distanceWeek + " / " + weeklyGoal+ "km");

        if (distanceWeek > 100.0f)
            distanceWeek = 100.0f;

        PieChart pc = (PieChart) rootView.findViewById(R.id.barchart);
        pc.setUsePercentValues(true);

        List<PieEntry> pieEntires = new ArrayList<>();
        pieEntires.add(new PieEntry((float)distanceWeek, ""));
        if (distanceWeek < weeklyGoal)
            pieEntires.add(new PieEntry((float)(weeklyGoal-distanceWeek), ""));

        PieDataSet dataSet = new PieDataSet(pieEntires,"");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);

        double percentDone = (1.0f-(weeklyGoal-distanceWeek)/weeklyGoal)*100.0f;
        percentDone =  Math.round(percentDone * 100.0) / 100.0;
        data.setDrawValues(false);

        //Get the chart
        pc.setData(data);
        pc.setCenterText(percentDone+" %");
        pc.setRotationEnabled(false);
        pc.setCenterTextSize(34);
        pc.setDescription("");
        pc.invalidate();
        return rootView;
    }

    private float getWeeklyDistanceFromSettings()
    {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("BicycleTrackerPrefs",Context.MODE_PRIVATE);
        return (float)sharedPref.getInt("weeklyGoal",100);
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
