package applab.bedtimeapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import applab.bedtimeapp.model.Alarm;
import applab.bedtimeapp.utils.utils;

/**
 * Created by berberakif on 17/04/17.
 */

public class AlarmOperations {

    public static final String LOGTAG = "ALARM_MNGMNT_SYS";

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            DatabaseHelper.ALARM_ID,
            DatabaseHelper.USER_ID,
            DatabaseHelper.DATE,
            DatabaseHelper.SLEEP_RATE,
            DatabaseHelper.MORNING_ALARM,
            DatabaseHelper.EVENING_ALARM,
            DatabaseHelper.NUMBER_OF_SNOOZES
    };

    public AlarmOperations(Context context) {
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
    public Alarm addAlarm(Alarm alarm) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.USER_ID, alarm.getUserId());
        values.put(DatabaseHelper.DATE, alarm.getDate());
        values.put(DatabaseHelper.SLEEP_RATE, alarm.getSleepRate());
        values.put(DatabaseHelper.MORNING_ALARM, alarm.getMorningAlarm());
        values.put(DatabaseHelper.EVENING_ALARM, alarm.getEveningAlarm());
        values.put(DatabaseHelper.NUMBER_OF_SNOOZES, alarm.getNumberOfSnoozes());


        long insertId = database.insert(DatabaseHelper.TABLE_ALARM, null, values);
        alarm.setAlarmId(insertId);
        return alarm;

    }

    // select
    public Alarm getAlarm(long id) {

        Cursor cursor = database.query(DatabaseHelper.TABLE_ALARM, allColumns, DatabaseHelper.ALARM_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Alarm a = new Alarm(
                Long.parseLong(cursor.getString(0)),
                Long.parseLong(cursor.getString(1)),
                cursor.getString(2),
                Integer.parseInt(cursor.getString(3)),
                cursor.getString(4),
                cursor.getString(5),
                Integer.parseInt(cursor.getString(6))
                );
        // return
        return a;
    }

    public List<Alarm> getAllAlarms(int userId) {

        Cursor cursor;

        if (userId != -1)
            cursor = database.query(DatabaseHelper.TABLE_ALARM, allColumns, DatabaseHelper.USER_ID + "=?",  new String[]{String.valueOf(userId)}, null, null, null);
        else
            cursor = database.query(DatabaseHelper.TABLE_ALARM, allColumns, null,  null, null, null, null);

        List<Alarm> alarms = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Alarm alarm = new Alarm();
                alarm.setAlarmId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.ALARM_ID)));
                alarm.setUserId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.USER_ID)));
                alarm.setDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DATE)));
                alarm.setSleepRate(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.SLEEP_RATE)));
                alarm.setMorningAlarm(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MORNING_ALARM)));
                alarm.setEveningAlarm(cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENING_ALARM)));
                alarm.setNumberOfSnoozes(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.NUMBER_OF_SNOOZES)));

                alarms.add(alarm);
            }
        }
        return alarms;
    }


    public int counter(long userId){
        // TODO write Alarm
        //Cursor cursor = database.query(DatabaseHelper.TABLE_FEEDBACK, allColumns, DatabaseHelper.USER_ID + "=? AND substr(" +DatabaseHelper.DATE +", 10)=?", new String[]{String.valueOf(userId), utils.getCurrentTimeString("yyyy-MM-dd")}, null, null, null, null);
        Cursor cursor = database.rawQuery("SELECT FB_ID, USER_ID, DATE, QUESTION_BUSY, QUESTION_RESTED, QUESTION_MOOD, QUESTION_CONCENTRATION, QUESTION_CONCENTRATION, REFUSAL_REASON FROM feedbacks WHERE USER_ID= "+String.valueOf(userId)+" AND DATE LIKE '"+ utils.getCurrentTimeString("yyyy-MM-dd") + "%'", null);
        return cursor.getCount();

    }

    // Updating alarm
    public int updateAlarm(Alarm alarm) {


        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.USER_ID, alarm.getUserId());
        values.put(DatabaseHelper.DATE, alarm.getDate());
        values.put(DatabaseHelper.SLEEP_RATE, alarm.getSleepRate());
        values.put(DatabaseHelper.MORNING_ALARM, alarm.getMorningAlarm());
        values.put(DatabaseHelper.EVENING_ALARM, alarm.getEveningAlarm());
        values.put(DatabaseHelper.NUMBER_OF_SNOOZES, alarm.getNumberOfSnoozes());

        // updating row
        return database.update(DatabaseHelper.TABLE_ALARM, values,
                DatabaseHelper.ALARM_ID + "=?", new String[]{String.valueOf(alarm.getAlarmId())});
    }

    // Deleting alarm
    public void removeAlarm(Alarm alarm) {

        database.delete(DatabaseHelper.TABLE_ALARM, DatabaseHelper.ALARM_ID + "=" + alarm.getAlarmId(), null);
    }


}
