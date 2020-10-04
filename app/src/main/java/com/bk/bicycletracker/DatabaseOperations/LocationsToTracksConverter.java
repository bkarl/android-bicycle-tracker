package com.bk.bicycletracker.DatabaseOperations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.bk.bicycletracker.TrackDataBaseSchema;
import com.bk.bicycletracker.TrackDatabaseHelper;

/* Converts entries in the location database to tracks in the tracks database by accumulating.
* Needed for database upgrades in development.
* */

public class LocationsToTracksConverter {

    SQLiteDatabase database;

    private final String[] tableColumsForDistanceCalculation = {
            TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LATITUDE,
            TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LONGITUDE,
            TrackDataBaseSchema.LocationEntry.COLUMN_NAME_ALTITUDE,
            TrackDataBaseSchema.LocationEntry.COLUMN_NAME_TRACK_ID,
            TrackDataBaseSchema.LocationEntry.COLUMN_NAME_TIME
    };

    public LocationsToTracksConverter(Context c)
    {
        database = new TrackDatabaseHelper(c).getWritableDatabase();
    }

    public LocationsToTracksConverter(SQLiteDatabase db)
    {
        database = db;
    }

    public void translateEntries() {
        database.execSQL("DELETE FROM " + TrackDataBaseSchema.TrackEntry.TABLE_NAME);

        Cursor c = queryWholeDatabase();
        double longitudeA;
        double latitudeA;
        double latitudeB;
        double longitudeB;

        float distance = 0;
        c.moveToFirst();

        if (c.getCount() == 0)
            return;

        long idA, idB;

        longitudeA = c.getDouble(c.getColumnIndexOrThrow(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LONGITUDE));
        latitudeA = c.getDouble(c.getColumnIndexOrThrow(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LATITUDE));
        idA = c.getLong(c.getColumnIndexOrThrow(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_TRACK_ID));
        long unixTimeA = c.getLong(c.getColumnIndexOrThrow(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_TIME));
        long time_start = unixTimeA;
        c.moveToNext();
        while (!c.isAfterLast())
        {
            latitudeB = c.getDouble(c.getColumnIndexOrThrow(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LATITUDE));
            longitudeB = c.getDouble(c.getColumnIndexOrThrow(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LONGITUDE));
            idB = c.getLong(c.getColumnIndexOrThrow(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_TRACK_ID));
            long unixTimeB = c.getLong(c.getColumnIndexOrThrow(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_TIME));

            if (time_start == 0)
                time_start = unixTimeA;

            float result[] = new float[] {0,0,0};
            // items.add(new OverlayItem("", "", new GeoPoint(latitudeA,longitudeA))); // Lat/Lon decimal degrees

            Location.distanceBetween(latitudeA, longitudeA, latitudeB, longitudeB, result);
            if (Math.abs(unixTimeA-unixTimeB) < 900)
                distance += result[0];
            else if (distance != 0){
                ContentValues values = new ContentValues();
                values.put(TrackDataBaseSchema.TrackEntry.COLUMN_NAME_DISTANCE_KM, distance/1000.0f);
                values.put(TrackDataBaseSchema.TrackEntry.COLUMN_NAME_TIME_START, time_start);
                values.put(TrackDataBaseSchema.TrackEntry.COLUMN_NAME_TIME_END, unixTimeA);
                database.insert(TrackDataBaseSchema.TrackEntry.TABLE_NAME, null, values);
                distance = 0;
                time_start = 0;
            }
            else
            {
                distance = 0;
                time_start = 0;
            }

            longitudeA = longitudeB;
            latitudeA = latitudeB;
            idA = idB;
            unixTimeA = unixTimeB;
            c.moveToNext();
        }
        ContentValues values = new ContentValues();
        values.put(TrackDataBaseSchema.TrackEntry.COLUMN_NAME_DISTANCE_KM, distance/1000.0f);
        values.put(TrackDataBaseSchema.TrackEntry.COLUMN_NAME_TIME_START, time_start);
        values.put(TrackDataBaseSchema.TrackEntry.COLUMN_NAME_TIME_END, unixTimeA);
        database.insert(TrackDataBaseSchema.TrackEntry.TABLE_NAME, null, values);
        distance = 0;
        //distance += subdistance;//else

        c.close();
    }

    private Cursor queryWholeDatabase()
    {
        return database.query(
                TrackDataBaseSchema.LocationEntry.TABLE_NAME,
                tableColumsForDistanceCalculation,
                null,
                null,
                null,
                null,
                null
        );
    }
}
