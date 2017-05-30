package applab.bedtimeapp.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    //
    public String databasePath = "";

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "Bedtime.db";

    // Table name
    public static final String TABLE_RESULT = "result";

    // Column names

    // result columns
    public static final String RESULT_ID = "RESULT_ID";
    public static final String USER_ID = "USER_ID";
    public static final String DAY_ID = "DAY_ID";
    public static final String CREATION_DATE = "CREATION_DATE";

    // alarm columns
    public static final String ALARM_DATE = "ALARM_DATE";
    public static final String SLEEP_RATE = "SLEEP_RATE";
    public static final String MORNING_ALARM = "MORNING_ALARM";
    public static final String EVENING_ALARM = "EVENING_ALARM";
    public static final String NUMBER_OF_SNOOZES = "NUMBER_OF_SNOOZES";

    // feedback columns
    public static final String FEEDBACK_DATE = "FEEDBACK_DATE";
    public static final String QUESTION_BUSY = "QUESTION_BUSY";
    public static final String QUESTION_RESTED = "QUESTION_RESTED";
    public static final String QUESTION_MOOD = "QUESTION_MOOD";
    public static final String QUESTION_CONCENTRATION = "QUESTION_CONCENTRATION";

    // reason columns
    public static final String REFUSAL_REASON = "REFUSAL_REASON";
    public static final String PROCRASTINATION_DURATION = "PROCRASTINATION_DURATION";

    // self efficacy columns
    public static final String SELF_EFFICACY_DATE = "SELF_EFFICACY_DATE";
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

    private static final String TABLE_CREATE_RESULT =
            "CREATE TABLE " + TABLE_RESULT + " ( " +

                    RESULT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    USER_ID + " INTEGER, " +
                    DAY_ID + " INTEGER, " +
                    CREATION_DATE + " NUMERIC, " +

                    // ALARM COLUMNS
                    ALARM_DATE + " NUMERIC, " +
                    SLEEP_RATE + " INTEGER, " +
                    MORNING_ALARM + " TEXT, " +
                    EVENING_ALARM + " TEXT, " +
                    NUMBER_OF_SNOOZES + " INTEGER, " +

                    // FEEDBACK COLUMNS
                    FEEDBACK_DATE + " NUMERIC, " +
                    QUESTION_RESTED + " INTEGER, " +
                    QUESTION_MOOD + " INTEGER, " +
                    QUESTION_CONCENTRATION + " INTEGER, " +
                    QUESTION_BUSY + " INTEGER, " +
                    REFUSAL_REASON + " TEXT, " +
                    PROCRASTINATION_DURATION + " INTEGER, " +

                    //SELF EFFICACY COLUMNS
                    SELF_EFFICACY_DATE + " NUMERIC, " +
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




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        databasePath = context.getDatabasePath("Bedtime.db").getPath();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_RESULT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULT);

        onCreate(db);
    }

    public JSONObject getResults(Context context) throws JSONException {
        //String dbPath = context.getDatabasePath("Bedtime.db");
        SQLiteDatabase bedtimeDB = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READONLY);
        JSONObject meta_object = new JSONObject();

        // add User_ID
        //  Initialize SharedPreferences
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        //  Create a new boolean and preference and set it to true
        int userID = getPrefs.getInt("userID", 0);
        Log.e("User_ID", Integer.toString(userID));
        meta_object.put("user_ID", userID);

        //List<String> tableList = new ArrayList<String>();
        List<String> tableList = Arrays.asList(TABLE_RESULT);
        for (String table: tableList) {
            JSONArray resultSet = new JSONArray();
            Log.e("table name", table);
            String myTable = table;

            String searchQuery = "SELECT  * FROM " + myTable;
            Cursor cursor = bedtimeDB.rawQuery(searchQuery, null);

            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {

                int totalColumn = cursor.getColumnCount();
                JSONObject rowObject = new JSONObject();

                for (int i = 0; i < totalColumn; i++) {
                    if (cursor.getColumnName(i) != null) {
                        try {
                            if (cursor.getString(i) != null) {
                                Log.d("TAG_NAME", cursor.getString(i));
                                rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                            } else {
                                rowObject.put(cursor.getColumnName(i), "");
                            }
                        } catch (Exception e) {
                            Log.d("TAG_NAME", e.getMessage());
                        }
                    }
                }
                resultSet.put(rowObject);
                cursor.moveToNext();
            }
            cursor.close();
            Log.d("TAG_NAME", resultSet.toString());
            meta_object.put(table, resultSet);
        }
        return meta_object;
    }
}
