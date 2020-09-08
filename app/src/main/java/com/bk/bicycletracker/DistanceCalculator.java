package com.bk.bicycletracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.bk.bicycletracker.DatabaseOperations.DistanceTracker;

import java.util.Calendar;

public class DistanceCalculator {

    SQLiteDatabase database;

    private final String[] tableColumsForDistanceCalculation = {
            TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LATITUDE,
            TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LONGITUDE,
            TrackDataBaseSchema.LocationEntry.COLUMN_NAME_ALTITUDE,
            TrackDataBaseSchema.LocationEntry.COLUMN_NAME_TRACK_ID,
    };

    public DistanceCalculator(Context c)
    {
        database = new TrackDatabaseHelper(c).getReadableDatabase();
    }

    public DistanceCalculator(SQLiteDatabase db)
    {
        database = db;
    }

    public float getDistanceForDay(Calendar day)
    {
        Timespan timespan = new Timespan();
        timespan.getTimespanForDay(day);
        Cursor c = queryDatabaseForTimespan(timespan);
        return calculateDistanceFromDatabaseResult(c);
    }

    public float getDistanceForWeek(Calendar dayOfWeek)
    {
        Timespan timespan = new Timespan();
        timespan.getTimespanForWeek(dayOfWeek);
        Cursor c = queryDatabaseForTimespan(timespan);
        return calculateDistanceFromDatabaseResult(c);
    }

    private Cursor queryDatabaseForTimespan(Timespan timespan) {
        String[] where = {
                String.valueOf(timespan.getUnixTimeMin()),
                String.valueOf(timespan.getUnixTimeMax())
        };

        return database.query(
                TrackDataBaseSchema.LocationEntry.TABLE_NAME,  // The table to query
                tableColumsForDistanceCalculation,                               // The columns to return
                TrackDataBaseSchema.LocationEntry.COLUMN_NAME_TIME+" >= ? AND "+TrackDataBaseSchema.LocationEntry.COLUMN_NAME_TIME+" <= ?",  // The columns for the WHERE clause
                where,            // The values for the WHERE clause
                null,
                null,                                     // don't group the rows
                null
        );
    }

    private float calculateDistanceFromDatabaseResult(Cursor c) {
        double longitudeA;
        double latitudeA;
        double latitudeB;
        double longitudeB;

        float distance = 0;
        c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        long idA, idB;

        longitudeA = c.getDouble(c.getColumnIndexOrThrow(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LONGITUDE));
        latitudeA = c.getDouble(c.getColumnIndexOrThrow(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LATITUDE));
        idA = c.getLong(c.getColumnIndexOrThrow(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_TRACK_ID));

        c.moveToNext();
        int add = 0;
        float subdistance = 0;
        while (!c.isAfterLast())
        {
            latitudeB = c.getDouble(c.getColumnIndexOrThrow(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LATITUDE));
            longitudeB = c.getDouble(c.getColumnIndexOrThrow(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LONGITUDE));
            idB = c.getLong(c.getColumnIndexOrThrow(TrackDataBaseSchema.LocationEntry.COLUMN_NAME_TRACK_ID));

            float result[] = new float[] {0,0,0};
            // items.add(new OverlayItem("", "", new GeoPoint(latitudeA,longitudeA))); // Lat/Lon decimal degrees

            Location.distanceBetween(latitudeA, longitudeA, latitudeB, longitudeB, result);
            if (idA == idB)
                distance += result[0];

            longitudeA = longitudeB;
            latitudeA = latitudeB;
            idA = idB;

            c.moveToNext();
        }
        //distance += subdistance;//else

        c.close();

        return distance;
    }
}
