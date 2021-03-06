package applab.bedtimeapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import java.util.ArrayList;
import java.util.List;

import applab.bedtimeapp.model.Result;
import applab.bedtimeapp.utils.Constants;
import applab.bedtimeapp.utils.utils;

/**
 * Created by berberakif on 17/04/17.
 */

public class ResultOperations {

    public static final String LOGTAG = "RESULT_MNGMNT_SYS";

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            DatabaseHelper.RESULT_ID,
            DatabaseHelper.USER_ID,
            DatabaseHelper.DAY_ID,
            DatabaseHelper.CREATION_DATE,

            // alarm columns
            DatabaseHelper.ALARM_DATE,
            DatabaseHelper.SLEEP_RATE,
            DatabaseHelper.MORNING_ALARM,
            DatabaseHelper.EVENING_ALARM,
            DatabaseHelper.NUMBER_OF_SNOOZES,

            // feedback columns
            DatabaseHelper.FEEDBACK_DATE,
            DatabaseHelper.QUESTION_BUSY,
            DatabaseHelper.QUESTION_RESTED,
            DatabaseHelper.QUESTION_MOOD,
            DatabaseHelper.QUESTION_CONCENTRATION,

            // reason columns
            DatabaseHelper.REFUSAL_REASON,
            DatabaseHelper.PROCRASTINATION_DURATION,

            // self efficacy columns
            DatabaseHelper.SELF_EFFICACY_DATE,
            DatabaseHelper.Q1,
            DatabaseHelper.Q2,
            DatabaseHelper.Q3,
            DatabaseHelper.Q4,
            DatabaseHelper.Q5,
            DatabaseHelper.Q6,
            DatabaseHelper.Q7,
            DatabaseHelper.Q8,
            DatabaseHelper.Q9,
            DatabaseHelper.Q10 // Q10 is gonna used as evening notification flag
    };

    public ResultOperations(Context context) {
        dbhandler = new DatabaseHelper(context);
    }

    public void open() {
        Log.i(LOGTAG, "Database Opened");
        database = dbhandler.getWritableDatabase();
    }

    public void close() {
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();

    }

    // insert
    public Result addResult(Result result) {

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.USER_ID, result.getUserId());
        values.put(DatabaseHelper.DAY_ID, result.getDayId());
        values.put(DatabaseHelper.CREATION_DATE, result.getCreationDate());
        values.put(DatabaseHelper.ALARM_DATE, result.getAlarmDate());
        values.put(DatabaseHelper.SLEEP_RATE, result.getSleepRate());
        values.put(DatabaseHelper.MORNING_ALARM, result.getMorningAlarm());
        values.put(DatabaseHelper.EVENING_ALARM, result.getEveningAlarm());
        values.put(DatabaseHelper.NUMBER_OF_SNOOZES, result.getNumberOfSnoozes());
        values.put(DatabaseHelper.FEEDBACK_DATE, result.getFeedbackDate());
        values.put(DatabaseHelper.QUESTION_BUSY, result.getQuestionBusy());
        values.put(DatabaseHelper.QUESTION_RESTED, result.getQuestionRested());
        values.put(DatabaseHelper.QUESTION_MOOD, result.getQuestionMood());
        values.put(DatabaseHelper.QUESTION_CONCENTRATION, result.getQuestionConcentration());
        values.put(DatabaseHelper.REFUSAL_REASON, result.getRefusalReason());
        values.put(DatabaseHelper.PROCRASTINATION_DURATION, result.getProcrastinationDuration());
        values.put(DatabaseHelper.SELF_EFFICACY_DATE, result.getSelfEfficacyDate());
        values.put(DatabaseHelper.Q1, result.getQ1());
        values.put(DatabaseHelper.Q2, result.getQ2());
        values.put(DatabaseHelper.Q3, result.getQ3());
        values.put(DatabaseHelper.Q4, result.getQ4());
        values.put(DatabaseHelper.Q5, result.getQ5());
        values.put(DatabaseHelper.Q6, result.getQ6());
        values.put(DatabaseHelper.Q7, result.getQ7());
        values.put(DatabaseHelper.Q8, result.getQ8());
        values.put(DatabaseHelper.Q9, result.getQ9());
        values.put(DatabaseHelper.Q10, result.getQ10());

        long insertId = database.insert(DatabaseHelper.TABLE_RESULT, null, values);
        result.setResultId(insertId);
        return result;

    }

    // select
    public Result getResult(long id) {

        Cursor cursor = database.query(DatabaseHelper.TABLE_RESULT, allColumns, DatabaseHelper.RESULT_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Result r = new Result(
                Long.parseLong(cursor.getString(0)),
                Long.parseLong(cursor.getString(1)),
                Long.parseLong(cursor.getString(2)),
                cursor.getString(3),
                cursor.getString(4),
                Integer.parseInt(cursor.getString(5)),
                cursor.getString(6),
                cursor.getString(7),
                Integer.parseInt(cursor.getString(8)),
                cursor.getString(9),
                Integer.parseInt(cursor.getString(10)),
                Integer.parseInt(cursor.getString(11)),
                Integer.parseInt(cursor.getString(12)),
                Integer.parseInt(cursor.getString(13)),
                cursor.getString(14),
                Integer.parseInt(cursor.getString(15)),
                cursor.getString(16),
                Integer.parseInt(cursor.getString(17)),
                Integer.parseInt(cursor.getString(18)),
                Integer.parseInt(cursor.getString(19)),
                Integer.parseInt(cursor.getString(20)),
                Integer.parseInt(cursor.getString(21)),
                Integer.parseInt(cursor.getString(22)),
                Integer.parseInt(cursor.getString(23)),
                Integer.parseInt(cursor.getString(24)),
                Integer.parseInt(cursor.getString(25)),
                Integer.parseInt(cursor.getString(26))
        );

        // return Result
        return r;
    }

    public List<Result> getAllResults(long userId) {
        Cursor cursor;
        if (userId != -1)
            cursor = database.query(DatabaseHelper.TABLE_RESULT, allColumns, DatabaseHelper.USER_ID + "=?",  new String[]{String.valueOf(userId)}, null, null, null);
        else
            cursor = database.query(DatabaseHelper.TABLE_RESULT, allColumns, null,  null, null, null, null);

        List<Result> results = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Result result = new Result();

                result.setResultId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.RESULT_ID)));
                result.setUserId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.USER_ID)));
                result.setDayId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.DAY_ID)));
                result.setCreationDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CREATION_DATE)));
                result.setAlarmDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ALARM_DATE)));
                result.setSleepRate(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SLEEP_RATE)));
                result.setMorningAlarm(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MORNING_ALARM)));
                result.setEveningAlarm(cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENING_ALARM)));
                result.setNumberOfSnoozes(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.NUMBER_OF_SNOOZES)));
                result.setFeedbackDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FEEDBACK_DATE)));
                result.setQuestionBusy(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTION_BUSY)));
                result.setQuestionRested(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTION_RESTED)));
                result.setQuestionMood(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTION_MOOD)));
                result.setQuestionConcentration(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTION_CONCENTRATION)));
                result.setRefusalReason(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFUSAL_REASON)));
                result.setProcrastinationDuration(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PROCRASTINATION_DURATION)));
                result.setSelfEfficacyDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SELF_EFFICACY_DATE)));
                result.setQ1(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Q1)));
                result.setQ2(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Q2)));
                result.setQ3(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Q3)));
                result.setQ4(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Q4)));
                result.setQ5(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Q5)));
                result.setQ6(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Q6)));
                result.setQ7(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Q7)));
                result.setQ8(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Q8)));
                result.setQ9(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Q9)));
                result.setQ10(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Q10)));

                results.add(result);
            }
        }
        // return All Feedbacks
        return results;
    }


    // Updating Result according to user id and the day id
    public int updateResult(Result result, long dayId) {


        ContentValues values = new ContentValues();
        //values.put(DatabaseHelper.USER_ID, result.getUserId());
        //values.put(DatabaseHelper.DAY_ID, result.getDayId());
        //values.put(DatabaseHelper.CREATION_DATE, result.getCreationDate());

        if (result.getUpdateType() == 'A') {
            values.put(DatabaseHelper.SLEEP_RATE, result.getSleepRate());
            values.put(DatabaseHelper.NUMBER_OF_SNOOZES, result.getNumberOfSnoozes());
        }
        else if (result.getUpdateType() == 'M'){
            values.put(DatabaseHelper.ALARM_DATE, result.getAlarmDate());
            values.put(DatabaseHelper.MORNING_ALARM, result.getMorningAlarm());
        }
        else if (result.getUpdateType() == 'E'){
            values.put(DatabaseHelper.ALARM_DATE, result.getAlarmDate());
            values.put(DatabaseHelper.EVENING_ALARM, result.getEveningAlarm());
        }
        else if (result.getUpdateType()== 'F') {
            values.put(DatabaseHelper.FEEDBACK_DATE, result.getFeedbackDate());
            values.put(DatabaseHelper.QUESTION_BUSY, result.getQuestionBusy());
            values.put(DatabaseHelper.QUESTION_RESTED, result.getQuestionRested());
            values.put(DatabaseHelper.QUESTION_MOOD, result.getQuestionMood());
            values.put(DatabaseHelper.QUESTION_CONCENTRATION, result.getQuestionConcentration());
            values.put(DatabaseHelper.REFUSAL_REASON, result.getRefusalReason());
            values.put(DatabaseHelper.PROCRASTINATION_DURATION, result.getProcrastinationDuration());
        }
        else if(result.getUpdateType() == 'S') {
            values.put(DatabaseHelper.SELF_EFFICACY_DATE, result.getSelfEfficacyDate());
            values.put(DatabaseHelper.Q1, result.getQ1());
            values.put(DatabaseHelper.Q2, result.getQ2());
            values.put(DatabaseHelper.Q3, result.getQ3());
            values.put(DatabaseHelper.Q4, result.getQ4());
            values.put(DatabaseHelper.Q5, result.getQ5());
            values.put(DatabaseHelper.Q6, result.getQ6());
            values.put(DatabaseHelper.Q7, result.getQ7());
            values.put(DatabaseHelper.Q8, result.getQ8());
            values.put(DatabaseHelper.Q9, result.getQ9());
            values.put(DatabaseHelper.Q10, result.getQ9());
        }
        // updating row
        return database.update(DatabaseHelper.TABLE_RESULT, values,
                DatabaseHelper.DAY_ID + "=? ", new String[]{String.valueOf(dayId)});
    }

    // Deleting result
    public void removeFeedback(Result result) {

        database.delete(DatabaseHelper.TABLE_RESULT, DatabaseHelper.RESULT_ID + "=" + result.getResultId(), null);
    }

    public int isQuestionnaireFilled(long userId, int currentDay){

        Cursor cursor = database.rawQuery("SELECT RESULT_ID FROM result WHERE USER_ID= "+String.valueOf(userId)+" AND DAY_ID = " + String.valueOf(currentDay) +" AND FEEDBACK_DATE IS NOT NULL", null);
            return cursor.getCount();

    }

    public ArrayList <Entry> getField(long userId, long currentDay, String field){

        int value;

       ArrayList entries = new ArrayList<Entry>();

        for (int i = 0; i< Constants.DAYS_IN_A_WEEK; i++)
        {
            long day = currentDay - i;
            Cursor cursor = database.rawQuery("SELECT "+ field + " FROM result WHERE USER_ID= "+String.valueOf(userId)+" AND DAY_ID = " + String.valueOf(day), null);
            value = 0;
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                value =  cursor.getInt(cursor.getColumnIndex(field));
            }

            entries.add(new Entry(i,value ));
        }

        return entries;

    }

    public ArrayList <String> getFieldString(long userId, long currentDay, String field){

        String value;

        ArrayList<String> entries = new ArrayList<>();

        for (int i = 0; i< Constants.DAYS_IN_A_WEEK; i++)
        {
            long day = currentDay - i;
            Cursor cursor = database.rawQuery("SELECT "+ field + " FROM result WHERE USER_ID= "+String.valueOf(userId)+" AND DAY_ID = " + String.valueOf(day), null);
            value = "";
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                value =  cursor.getString(cursor.getColumnIndex(field));
            }
            entries.add(value);
        }

        return entries;

    }


    public List<String> getAllReasons(int userID) {

        List reasons = new ArrayList();
        Cursor cursor = database.rawQuery("SELECT REFUSAL_REASON FROM result WHERE REFUSAL_REASON <> '' and USER_ID= "+String.valueOf(userID), null);
        String reason = "";
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            reason =  cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFUSAL_REASON));
            reasons.add(reason);
        }

        return reasons;
    }

    // get day id to write feedback
    public int getFeedbackDayId(Context context) {

        /*Cursor cursor = database.rawQuery("SELECT DAY_ID FROM result WHERE FEEDBACK_DATE IS NULL and USER_ID= "+String.valueOf(userID) + " ORDER BY DAY_ID LIMIT 1", null);
        int dayId = 0;
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            dayId =  cursor.getInt(cursor.getColumnIndex(DatabaseHelper.DAY_ID));
        }*/
        int dayId = (int)(utils.getDayId(context)) - 1;

        if(dayId < 1)
            dayId = 1;

        if(dayId > 13)
            dayId = 14;

        return dayId;

    }

    public boolean isFeedbackGivenToday(int userID,Context context){

        Log.e("Day", Integer.toString((int)utils.getDayId(context)));

        // CHECK FOR THE DAY 1
        int currentFeedbackDay = (int)utils.getDayId(context) -1;
        if (currentFeedbackDay < 1)
            return true;

        Cursor cursor = database.rawQuery("SELECT FEEDBACK_DATE FROM result WHERE DAY_ID = " + currentFeedbackDay + " AND FEEDBACK_DATE IS NOT NULL and USER_ID= " +String.valueOf(userID), null);
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    public boolean isQuestionairreFilled(int userID){
        Cursor cursor = database.rawQuery("SELECT DAY_ID FROM result WHERE SELF_EFFICACY_DATE IS NOT NULL and USER_ID= " +String.valueOf(userID) , null);
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    // get day id to write sleeprate
    public int getSleepRateDayId(Context context) {

       /* Cursor cursor = database.rawQuery("SELECT DAY_ID FROM result WHERE SLEEP_RATE = 0 and USER_ID= "+String.valueOf(userID) + " ORDER BY DAY_ID LIMIT 1", null);
        int dayId = 0;
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            dayId =  cursor.getInt(cursor.getColumnIndex(DatabaseHelper.DAY_ID));
        }*/
        int dayId = (int)(utils.getDayId(context)) -1;

        if(dayId < 1)
            dayId = 1;

        if(dayId > 13)
            dayId = 14;

        return dayId;
    }

    // TODO : get the unnotified notification for today if exists
    public int[] getNotificationTime(int userID, Context context) {
        int result[] = new int[2];
        result[0] = -1;
        result[1] = -1;

        if(userID == 0)
            return result;

        int dayId = (int)(utils.getDayId(context)) - 1;

        if(dayId < 1)
            dayId = 1;

        if(dayId > 13)
            dayId = 14;

        Cursor cursor = database.rawQuery("SELECT MORNING_ALARM FROM result WHERE MORNING_ALARM IS NOT NULL AND DAY_ID = "+ dayId + " AND USER_ID= " +String.valueOf(userID), null);

        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            result[0] =  Constants.DELAY_MORNING_QUESTIONNAIRE + Integer.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MORNING_ALARM)).substring(0,2));
            result[1] =  Integer.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MORNING_ALARM)).substring(3,5));
        }

        // Constants.DELAY_MORNING_QUESTIONNAIRE
        return result;
    }

    // get the times for alarm
    public int[] getAlarmTime(int userID, Context context) {
        int result[] = new int[2];
        result[0] = -1;
        result[1] = -1;

        if(userID == 0)
            return result;

        int dayId = (int)(utils.getDayId(context)) - 1;

        if(dayId < 1)
            dayId = 1;

        if(dayId > 13)
            dayId = 14;

        Cursor cursor = database.rawQuery("SELECT MORNING_ALARM FROM result WHERE MORNING_ALARM IS NOT NULL AND DAY_ID = "+ dayId + " AND USER_ID= " +String.valueOf(userID), null);

        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            result[0] =  Integer.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MORNING_ALARM)).substring(0,2));
            result[1] =  Integer.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MORNING_ALARM)).substring(3,5));
        }

        // Constants.DELAY_MORNING_QUESTIONNAIRE
        return result;
    }
}
