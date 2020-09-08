package com.bk.bicycletracker;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentGraph#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentGraph extends Fragment {


    private TrackDatabaseHelper mDB;

    private TextView txtDistanceWeek;
    public FragmentGraph() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentGraph.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentGraph newInstance(String param1, String param2) {
        FragmentGraph fragment = new FragmentGraph();
        /*
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        */
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        */
    }

    private void fillBarChart(BarChart bc)
    {
        DistanceCalculator distanceCalculator = new DistanceCalculator(this.getContext());
        Calendar cal = Calendar.getInstance();
        List<BarEntry> entries = new ArrayList<BarEntry>();
        float totalWeek = 0;
        final String[] days = new String[7];
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //TODO: set according to locale

        for (int i = 0; i < 7; i++)
        {
            float trackDay = distanceCalculator.getDistanceForDay(cal);
            days[i] = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
            entries.add(new BarEntry(i, trackDay));
            totalWeek += trackDay;
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        AxisValueFormatter formatter = new AxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return days[(int) value];
            }

            // we don't draw numbers, so no decimal digits needed
            @Override
            public int getDecimalDigits() {  return 0; }
        };


        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setLabel("");


        BarData bd = new BarData(dataSet);
        bd.setValueFormatter(new KMValueFormater());
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        bc.getXAxis().setDrawGridLines(false);
        bc.getXAxis().setDrawAxisLine(false);
        bc.getXAxis().setDrawLabels(true);
        bc.getXAxis().setValueFormatter(formatter);
        bc.getXAxis().setGranularity(1);
        bc.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        bc.setFitBars(true);

        bc.getAxisLeft().setDrawGridLines(false);
        bc.getAxisLeft().setDrawAxisLine(false);
        bc.getAxisLeft().setDrawLabels(false);
        bc.getAxisLeft().setDrawZeroLine(true);

        bc.getAxisRight().setEnabled(false);

        bc.setTouchEnabled(false);
        bc.setDescription("");
        bc.setData(bd);
        bc.invalidate();

        txtDistanceWeek.setText(Math.round(totalWeek / 10.0)/100.0 + " km");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_tracking_graph, container, false);

        BarChart bc = (BarChart) rootView.findViewById(R.id.chart);
        mDB = new TrackDatabaseHelper(this.getContext());
        txtDistanceWeek = (TextView) rootView.findViewById(R.id.txtDistanceWeek);
        fillBarChart(bc);
        return rootView;
    }

    public class KMValueFormater implements ValueFormatter {


        public KMValueFormater() {

        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return Math.round(value / 10.0)/100.0 + " km";
        }
    }

}
