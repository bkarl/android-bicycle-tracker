package com.bk.bicycletracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;

public class LocationListener implements android.location.LocationListener
{
    private Location mLastLocation;
    private SQLiteDatabase db;
    private long currentTrackID;
    public LocationListener(String provider, TrackDatabaseHelper databaseHelper, long currentTrackID)
    {
        //Log.i(TAG, "LocationListener " + provider);
        mLastLocation = new Location(provider);
        db = databaseHelper.getWritableDatabase();
        this.currentTrackID = currentTrackID;
    }

    @Override
    public void onLocationChanged(Location location)
    {
        //if (location.getAccuracy() > 15)
        //    return;

        //Log.e(TAG, "onLocationChanged: " + location);
        mLastLocation.set(location);
        // Gets the data repository in write mode
        long unixTime = System.currentTimeMillis() / 1000L;

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_TRACK_ID, currentTrackID);
        values.put(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LATITUDE, location.getLatitude());
        values.put(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LONGITUDE, location.getLongitude());
        values.put(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_ALTITUDE, location.getAltitude());
        values.put(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_TIME, unixTime);

        // Insert the new row, returning the primary key value of the new row
        db.insert(TrackDataBaseSchema.LocationEntry.TABLE_NAME, null, values);

    }

    @Override
    public void onProviderDisabled(String provider)
    {
        //Log.e(TAG, "onProviderDisabled: " + provider);
    }

    @Override
    public void onProviderEnabled(String provider)
    {
        //Log.e(TAG, "onProviderEnabled: " + provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        //Log.e(TAG, "onStatusChanged: " + provider);
    }

}