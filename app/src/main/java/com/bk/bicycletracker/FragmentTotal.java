package com.bk.bicycletracker;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bk.bicycletracker.DatabaseOperations.DistanceCalculator;

public class FragmentTotal extends Fragment {

    private TextView txtDistance;
    private double distanceTotalInKm;

    public FragmentTotal() {
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
        View rootView = inflater.inflate(R.layout.fragment_tracks_total, container, false);
        txtDistance = (TextView) rootView.findViewById(R.id.txtDistanceTotal);

        calculateTotalDistance();
        txtDistance.setText(distanceTotalInKm + " km");

        return rootView;
    }

    private void calculateTotalDistance() {
        DistanceCalculator distanceCalculator = new DistanceCalculator(this.getContext());
        distanceTotalInKm = distanceCalculator.getTotalDistance();
        distanceTotalInKm = Math.round(distanceTotalInKm / 10.0) / 100.0;
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
