package com.example.cd.workreminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cd.workreminder.WorkReaderContract;

public class WorkReaderDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WorkReader.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WorkReaderContract.WorkEntry.TABLE_NAME + " (" +
                    WorkReaderContract.WorkEntry._ID + " INTEGER PRIMARY KEY," +
                    WorkReaderContract.WorkEntry.COLUMN_NAME_EMPLOYEE_ID + " TEXT," +
                    WorkReaderContract.WorkEntry.COLUMN_NAME_WEEK + " TEXT," +
                    WorkReaderContract.WorkEntry.COLUMN_NAME_START_HOUR + " TEXT," +
                    WorkReaderContract.WorkEntry.COLUMN_NAME_START_MINUTE + " TEXT" +
                    WorkReaderContract.WorkEntry.COLUMN_NAME_START_AM_OR_PM + " TEXT" +
                    WorkReaderContract.WorkEntry.COLUMN_NAME_END_HOUR + " TEXT" +
                    WorkReaderContract.WorkEntry.COLUMN_NAME_END_MINUTE + " TEXT" +
                    WorkReaderContract.WorkEntry.COLUMN_NAME_END_AM_OR_PM + " TEXT" +
                    ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WorkReaderContract.WorkEntry.TABLE_NAME;

    public WorkReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //super.onDowngrade(db, oldVersion, newVersion);
        onUpgrade(db, oldVersion, newVersion);
    }
}//end class