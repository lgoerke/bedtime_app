package applab.bedtimeapp.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {


    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "Bedtime.db";

    // Table name
    private static final String TABLE_NAME = "data";

    // Column names
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_DAY = "DAY";
    private static final String COLUMN_Q1 = "QUESTION1";
    private static final String COLUMN_Q2 = "QUESTION2";
    private static final String COLUMN_Q3 = "QUESTION3";
    private static final String COLUMN_EVENING_ALARM = "EVENING_ALARM";
    private static final String COLUMN_MORNING_ALARM = "MORNING_ALARM";
    private static final String COLUMN_NUMBER_OF_SNOOZES = "N_SNOOZES";
    private static final String COLUMN_LANDING_PAGE = "LANDING_PAGE";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " ( " +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DAY + " DATE, " +
                    COLUMN_Q1 + " INTEGER, " +
                    COLUMN_Q2 + " INTEGER, " +
                    COLUMN_Q3 + " INTEGER, " +
                    COLUMN_EVENING_ALARM + " INTEGER, " +
                    COLUMN_MORNING_ALARM + " INTEGER, " +
                    COLUMN_NUMBER_OF_SNOOZES + " INTEGER, " +
                    COLUMN_LANDING_PAGE + " INTEGER);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
