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
    public static final String TABLE_SELF_EFFICACY = "efficacy";
    public static final String TABLE_REASON = "reason";

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

    // self efficacy columns
    public static final String SE_ID = "SE_ID";
    public static final String Q1 = "Q1";
    public static final String Q2 = "Q2";
    public static final String Q3 = "Q3";
    public static final String Q4 = "Q4";
    public static final String Q5 = "Q5";
    public static final String Q6 = "Q6";
    public static final String Q7 = "Q7";
    public static final String Q8 = "Q8";
    public static final String Q9 = "Q9";
    public static final String Q10 = "Q10";

    // reason columns
    public static final String R_ID = "R_ID";
    public static final String REASON = "REASON";



    private static final String TABLE_CREATE_FEEDBACK =
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


    private static final String TABLE_CREATE_SELF_EFFICACY =
            "CREATE TABLE " + TABLE_SELF_EFFICACY + " ( " +
                    SE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    USER_ID + " INTEGER, " +
                    DATE + " NUMERIC, " +
                    Q1 + " INTEGER, " +
                    Q2 + " INTEGER, " +
                    Q3 + " INTEGER, " +
                    Q4 + " INTEGER, " +
                    Q5 + " INTEGER, " +
                    Q6 + " INTEGER, " +
                    Q7 + " INTEGER, " +
                    Q8 + " INTEGER, " +
                    Q9 + " INTEGER, " +
                    Q10 + " INTEGER);";

    private static final String TABLE_CREATE_REASON =
            "CREATE TABLE " + TABLE_REASON + " ( " +
                    R_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    USER_ID + " INTEGER, " +
                    REASON + " TEXT); ";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_FEEDBACK);
        db.execSQL(TABLE_CREATE_SELF_EFFICACY);
        db.execSQL(TABLE_CREATE_REASON);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDBACK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SELF_EFFICACY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REASON);

        onCreate(db);
    }
}
