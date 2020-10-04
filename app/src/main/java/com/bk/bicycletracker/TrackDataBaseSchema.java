package com.bk.bicycletracker;

import android.provider.BaseColumns;

/**
 * Created by bk on 07.07.16.
 */
public final class TrackDataBaseSchema {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public TrackDataBaseSchema() {}

    /* Inner class that defines the table contents */
    public static abstract class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "trackedlocations";
        public static final String COLUMN_NAME_TRACK_ID = "trackid";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_ALTITUDE = "altitude";
        public static final String COLUMN_NAME_TIME = "time";

    }

    public static abstract class TrackEntry implements BaseColumns {
        public static final String TABLE_NAME = "recordedTracks";
        public static final String COLUMN_NAME_TIME_START = "time_start";
        public static final String COLUMN_NAME_TIME_END = "time_end";
        public static final String COLUMN_NAME_DISTANCE_KM = "distance_km";
    }
}