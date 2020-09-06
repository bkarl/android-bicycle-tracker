package com.bk.bicycletracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.InstrumentationRegistry;

import static org.junit.Assert.*;

public class TrackDatabaseHelperTest {

    @org.junit.Test
    public void onCreate() {
        // we will create a temporary database for testing
        TrackDatabaseHelper helper = new TrackDatabaseHelper(InstrumentationRegistry.getInstrumentation().getTargetContext(), true);
        SQLiteDatabase database = helper.getReadableDatabase();
        assertNotNull(database);

        checkLocationTableCreated(database);
        checkTracksTableCreated(database);
    }

    private void checkLocationTableCreated(SQLiteDatabase database) {
        Cursor c = queryDatabaseForTable(database, TrackDataBaseSchema.LocationEntry.TABLE_NAME);

        String[] columNames = {
                TrackDataBaseSchema.LocationEntry._ID,
                TrackDataBaseSchema.LocationEntry.COLUMN_NAME_TRACK_ID,
                TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LATITUDE,
                TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LONGITUDE,
                TrackDataBaseSchema.LocationEntry.COLUMN_NAME_ALTITUDE,
                TrackDataBaseSchema.LocationEntry.COLUMN_NAME_TIME};

        assertArrayEquals(c.getColumnNames(), columNames);
    }

    private void checkTracksTableCreated(SQLiteDatabase database) {
        Cursor c = queryDatabaseForTable(database, TrackDataBaseSchema.TrackEntry.TABLE_NAME);

        String[] columNames = {
                TrackDataBaseSchema.TrackEntry._ID,
                TrackDataBaseSchema.TrackEntry.COLUMN_NAME_TIME,};

        assertArrayEquals(c.getColumnNames(), columNames);
    }

    private Cursor queryDatabaseForTable(SQLiteDatabase database, String tableName) {
        return database.query(
                tableName,          // The table to query
                null,       // The columns to return
                null,      // The columns for the WHERE clause
                null,   // The values for the WHERE clause
                null,
                null,
                null);
    }
}