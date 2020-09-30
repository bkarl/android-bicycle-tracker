package com.bk.bicycletracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.bk.bicycletracker.DatabaseOperations.DistanceTracker;

public class LocationListener implements android.location.LocationListener
{
    private DistanceTracker distanceTracker;
    private static final String TAG = "LocationListener";
    public DistanceTracker getDistanceTracker() {
        return distanceTracker;
    }

    public LocationListener(String provider, SQLiteDatabase db) {
        //Log.i(TAG, "LocationListener " + provider);
        distanceTracker = new DistanceTracker(db);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.getAccuracy() > 15)
            return;

        distanceTracker.addNewTrackedLocation(location);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i(TAG, "onProviderDisabled: " + provider);
    }

    public void finishLogging() {
        distanceTracker.commitAccumulatedTrack();
    }

    @Override
    public void onProviderEnabled(String provider) {
        //Log.e(TAG, "onProviderEnabled: " + provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //Log.e(TAG, "onStatusChanged: " + provider);
    }

}