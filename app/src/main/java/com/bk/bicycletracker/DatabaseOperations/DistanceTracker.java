package com.bk.bicycletracker.DatabaseOperations;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.bk.bicycletracker.TrackDataBaseSchema;

public class DistanceTracker {

    private long currentTrackID;
    private Location lastlocation;
    private float totalDistance = 0;

    public long getCurrentTrackID() {
        return currentTrackID;
    }

    private SQLiteDatabase db;

    public DistanceTracker(SQLiteDatabase db) {
        this.db = db;
        generateTrackID();
    }

    private void generateTrackID() {
        long unixTime = System.currentTimeMillis() / 1000L;
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(TrackDataBaseSchema.TrackEntry.COLUMN_NAME_TIME, unixTime);
        // Insert the new row, returning the primary key value of the new row
        currentTrackID = db.insert(TrackDataBaseSchema.TrackEntry.TABLE_NAME, null, values);
    }

    public void addNewTrackedLocation(Location location) {

        long unixTime = System.currentTimeMillis() / 1000L;

        ContentValues values = new ContentValues();
        values.put(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_TRACK_ID, currentTrackID);
        values.put(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LATITUDE, location.getLatitude());
        values.put(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LONGITUDE, location.getLongitude());
        values.put(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_ALTITUDE, location.getAltitude());
        values.put(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_TIME, unixTime);

        // Insert the new row, returning the primary key value of the new row
        db.insert(TrackDataBaseSchema.LocationEntry.TABLE_NAME, null, values);

        if (lastlocation != null) {
            float result[] = new float[] {0,0,0};
            Location.distanceBetween(location.getLatitude(), location.getLongitude(), lastlocation.getLatitude(), lastlocation.getLongitude(), result);
            totalDistance += result[0];
        }
        lastlocation = location;
    }

    public void commitAccumulatedTrack() {
        if (totalDistance == 0)
            return;

        long unixTime = System.currentTimeMillis() / 1000L;

        ContentValues values = new ContentValues();
        values.put(TrackDataBaseSchema.TrackEntry.COLUMN_NAME_TRACK_ID, currentTrackID);
        values.put(TrackDataBaseSchema.TrackEntry.COLUMN_NAME_DISTANCE_KM, totalDistance);
        values.put(TrackDataBaseSchema.TrackEntry.COLUMN_NAME_TIME, unixTime);

        db.insert(TrackDataBaseSchema.TrackEntry.TABLE_NAME, null, values);
    }

    public float getTotalDistance() {
        return totalDistance;
    }


}
