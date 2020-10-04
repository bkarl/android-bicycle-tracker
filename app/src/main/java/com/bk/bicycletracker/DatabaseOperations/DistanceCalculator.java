package com.bk.bicycletracker.DatabaseOperations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.bk.bicycletracker.TrackDataBaseSchema;
import com.bk.bicycletracker.TrackDatabaseHelper;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DistanceCalculator {

    SQLiteDatabase database;

    private final String[] tableColumsForDistanceCalculation_n = {
            TrackDataBaseSchema.TrackEntry.COLUMN_NAME_TIME_START,
            TrackDataBaseSchema.TrackEntry.COLUMN_NAME_TIME_END,
            TrackDataBaseSchema.TrackEntry.COLUMN_NAME_DISTANCE_KM
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

    public float getTotalDistance()
    {
        Cursor c = queryWholeDatabase();
        return calculateDistanceFromDatabaseResult(c);
    }

    public Calendar getTimeOfFirstTrackedLocation()
    {
        Cursor c = queryWholeDatabase();
        if (c.getCount() == 0)
            return Calendar.getInstance();
        c.moveToFirst();

        long unixtime = c.getLong(c.getColumnIndexOrThrow(TrackDataBaseSchema.TrackEntry.COLUMN_NAME_TIME_START));
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(unixtime*1000);
        return cal;
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
                TrackDataBaseSchema.TrackEntry.TABLE_NAME,  // The table to query
                tableColumsForDistanceCalculation_n,                               // The columns to return
                "(" + TrackDataBaseSchema.TrackEntry.COLUMN_NAME_TIME_START+" >= ? AND "+TrackDataBaseSchema.TrackEntry.COLUMN_NAME_TIME_END+" <= ? )",  // The columns for the WHERE clause
                where,            // The values for the WHERE clause
                null,
                null,                                     // don't group the rows
                null
        );
    }

    private Cursor queryWholeDatabase()
    {
        return database.query(
                TrackDataBaseSchema.TrackEntry.TABLE_NAME,
                tableColumsForDistanceCalculation_n,
                null,
                null,
                null,
                null,
                TrackDataBaseSchema.TrackEntry.COLUMN_NAME_TIME_START+" ASC"
        );
    }

    private float calculateDistanceFromDatabaseResult(Cursor c) {

        float distance = 0;
        c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        while (!c.isAfterLast())
        {
            distance += c.getFloat(c.getColumnIndexOrThrow(TrackDataBaseSchema.TrackEntry.COLUMN_NAME_DISTANCE_KM));
            c.moveToNext();
        }

        c.close();
        return distance;
    }
}
