package applab.bedtimeapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import applab.bedtimeapp.model.Feedback;

/**
 * Created by berberakif on 17/04/17.
 */

public class FeedbackOperations {

    public static final String LOGTAG = "FB_MNGMNT_SYS";

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            DatabaseHelper.FB_ID,
            DatabaseHelper.USER_ID,
            DatabaseHelper.DATE,
            DatabaseHelper.QUESTION1,
            DatabaseHelper.QUESTION2,
            DatabaseHelper.QUESTION3,
            DatabaseHelper.MORNING_ALARM,
            DatabaseHelper.EVENING_ALARM,
            DatabaseHelper.NUMBER_OF_SNOOZES,
            DatabaseHelper.LANDING_PAGE,
            DatabaseHelper.REFUSAL_REASON
    };

    public FeedbackOperations(Context context) {
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
    public Feedback addFeedback(Feedback feedback) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.USER_ID, feedback.getUserId());
        values.put(DatabaseHelper.DATE, feedback.getDate());
        values.put(DatabaseHelper.QUESTION1, feedback.getQuestion1());
        values.put(DatabaseHelper.QUESTION2, feedback.getQuestion2());
        values.put(DatabaseHelper.QUESTION3, feedback.getQuestion3());
        values.put(DatabaseHelper.MORNING_ALARM, feedback.getMorningAlarm());
        values.put(DatabaseHelper.EVENING_ALARM, feedback.getEveningAlarm());
        values.put(DatabaseHelper.NUMBER_OF_SNOOZES, feedback.getNumberOfSnoozes());
        values.put(DatabaseHelper.LANDING_PAGE, feedback.getLandingPage());
        values.put(DatabaseHelper.REFUSAL_REASON, feedback.getRefusalReason());


        long insertId = database.insert(DatabaseHelper.TABLE_FEEDBACK, null, values);
        feedback.setFbId(insertId);
        return feedback;

    }

    // select
    public Feedback getFeedback(long id) {

        Cursor cursor = database.query(DatabaseHelper.TABLE_FEEDBACK, allColumns, DatabaseHelper.FB_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Feedback f = new Feedback(
                Long.parseLong(cursor.getString(0)),
                Long.parseLong(cursor.getString(1)),
                cursor.getString(2),
                Integer.parseInt(cursor.getString(3)),
                Integer.parseInt(cursor.getString(4)),
                Integer.parseInt(cursor.getString(5)),
                cursor.getString(6),
                cursor.getString(7),
                Integer.parseInt(cursor.getString(8)),
                cursor.getString(9),
                cursor.getString(10)
        );
        // return Feedback
        return f;
    }

    public List<Feedback> getAllFeedbacks() {

        Cursor cursor = database.query(DatabaseHelper.TABLE_FEEDBACK, allColumns, null, null, null, null, null);

        List<Feedback> feedbacks = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Feedback feedback = new Feedback();
                feedback.setFbId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.FB_ID)));
                feedback.setUserId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.USER_ID)));
                feedback.setDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DATE)));
                feedback.setQuestion1(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTION1)));
                feedback.setQuestion2(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTION2)));
                feedback.setQuestion3(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTION3)));
                feedback.setMorningAlarm(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MORNING_ALARM)));
                feedback.setEveningAlarm(cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENING_ALARM)));
                feedback.setNumberOfSnoozes(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.NUMBER_OF_SNOOZES)));
                feedback.setLandingPage(cursor.getString(cursor.getColumnIndex(DatabaseHelper.LANDING_PAGE)));
                feedback.setRefusalReason(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REFUSAL_REASON)));

                feedbacks.add(feedback);
            }
        }
        // return All Feedbacks
        return feedbacks;
    }


    // Updating Feedback
    public int updateFeedback(Feedback feedback) {


        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.USER_ID, feedback.getUserId());
        values.put(DatabaseHelper.DATE, feedback.getDate());
        values.put(DatabaseHelper.QUESTION1, feedback.getQuestion1());
        values.put(DatabaseHelper.QUESTION2, feedback.getQuestion2());
        values.put(DatabaseHelper.QUESTION3, feedback.getQuestion3());
        values.put(DatabaseHelper.MORNING_ALARM, feedback.getMorningAlarm());
        values.put(DatabaseHelper.EVENING_ALARM, feedback.getEveningAlarm());
        values.put(DatabaseHelper.NUMBER_OF_SNOOZES, feedback.getNumberOfSnoozes());
        values.put(DatabaseHelper.LANDING_PAGE, feedback.getLandingPage());
        values.put(DatabaseHelper.REFUSAL_REASON, feedback.getRefusalReason());

        // updating row
        return database.update(DatabaseHelper.TABLE_FEEDBACK, values,
                DatabaseHelper.FB_ID + "=?", new String[]{String.valueOf(feedback.getFbId())});
    }

    // Deleting feedback
    public void removeFeedback(Feedback feedback) {

        database.delete(DatabaseHelper.TABLE_FEEDBACK, DatabaseHelper.FB_ID + "=" + feedback.getFbId(), null);
    }


}
