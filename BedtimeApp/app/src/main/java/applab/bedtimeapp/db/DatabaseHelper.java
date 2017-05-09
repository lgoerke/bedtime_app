package applab.bedtimeapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "Bedtime.db";

    // Table name
    public static final String TABLE_FEEDBACK = "feedbacks";

    // Column names
    public static final String FB_ID = "FB_ID";
    public static final String USER_ID = "USER_ID";
    public static final String DATE = "DATE";
    public static final String QUESTION_BUSY = "QUESTION_BUSY";
    public static final String QUESTION_RESTED = "QUESTION_RESTED";
    public static final String QUESTION_MOOD = "QUESTION_MOOD";
    public static final String QUESTION_CONCENTRATION = "QUESTION_CONCENTRATION";
    public static final String MORNING_ALARM = "MORNING_ALARM";
    public static final String EVENING_ALARM = "EVENING_ALARM";
    public static final String NUMBER_OF_SNOOZES = "NUMBER_OF_SNOOZES";
    public static final String LANDING_PAGE = "LANDING_PAGE";
    public static final String REFUSAL_REASON = "REFUSAL_REASON";


    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_FEEDBACK + " ( " +
                    FB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    USER_ID + " INTEGER, " +
                    DATE + " NUMERIC, " +
                    QUESTION_BUSY + " INTEGER, " +
                    QUESTION_RESTED + " INTEGER, " +
                    QUESTION_MOOD + " INTEGER, " +
                    QUESTION_CONCENTRATION + " INTEGER, " +
                    MORNING_ALARM + " NUMERIC, " +
                    EVENING_ALARM + " NUMERIC, " +
                    NUMBER_OF_SNOOZES + " INTEGER, " +
                    LANDING_PAGE + " TEXT, " +
                    REFUSAL_REASON + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDBACK);
        onCreate(db);
    }
}
