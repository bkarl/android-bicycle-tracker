package com.bk.bicycletracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by bk on 07.07.16.
 */
public class TrackDatabaseHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Tracks.db";

    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES_LOCATION =
            "CREATE TABLE " + TrackDataBaseSchema.LocationEntry.TABLE_NAME + " (" +
                    TrackDataBaseSchema.LocationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TrackDataBaseSchema.LocationEntry.COLUMN_NAME_TRACK_ID+ " INTEGER" + COMMA_SEP +
                    TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LATITUDE+ " REAL" +COMMA_SEP +
                    TrackDataBaseSchema.LocationEntry.COLUMN_NAME_LONGITUDE+ " REAL" +COMMA_SEP +
                    TrackDataBaseSchema.LocationEntry.COLUMN_NAME_ALTITUDE+ " REAL" + COMMA_SEP+
                    TrackDataBaseSchema.LocationEntry.COLUMN_NAME_TIME+ " INTEGER )";

    private static final String SQL_CREATE_ENTRIES_TRACKS =
            "CREATE TABLE " + TrackDataBaseSchema.TrackEntry.TABLE_NAME + " (" +
                    TrackDataBaseSchema.TrackEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TrackDataBaseSchema.TrackEntry.COLUMN_NAME_TRACK_ID+ " INTEGER" + COMMA_SEP +
                    TrackDataBaseSchema.TrackEntry.COLUMN_NAME_DISTANCE_KM+ " REAL" + COMMA_SEP+
                    TrackDataBaseSchema.TrackEntry.COLUMN_NAME_TIME+ " INTEGER" + " )";

    public TrackDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public TrackDatabaseHelper(Context context, boolean createTemporaryDatabase) {
        //if name is null the database is temporary. important for testing.
        super(context, null, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_LOCATION);
        db.execSQL(SQL_CREATE_ENTRIES_TRACKS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        /*
        Log.i("TrackDatabaseHelper", "upgrading Database o:"+oldVersion+" n:"+newVersion);
        db.execSQL("DROP TABLE " + TrackDataBaseSchema.TrackEntry.TABLE_NAME);
        db.execSQL(SQL_CREATE_ENTRIES_TRACKS);
        */
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //onUpgrade(db, oldVersion, newVersion);
    }


}

