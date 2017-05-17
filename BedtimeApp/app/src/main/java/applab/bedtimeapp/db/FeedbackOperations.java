package applab.bedtimeapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import applab.bedtimeapp.model.Feedback;
import applab.bedtimeapp.utils.Constants;
import applab.bedtimeapp.utils.utils;
import com.github.mikephil.charting.data.Entry;

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
            DatabaseHelper.QUESTION_BUSY,
            DatabaseHelper.QUESTION_RESTED,
            DatabaseHelper.QUESTION_MOOD,
            DatabaseHelper.QUESTION_CONCENTRATION,
            DatabaseHelper.QUESTION_CONCENTRATION,
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
        values.put(DatabaseHelper.QUESTION_BUSY, feedback.getQuestionBusy());
        values.put(DatabaseHelper.QUESTION_RESTED, feedback.getQuestionRested());
        values.put(DatabaseHelper.QUESTION_MOOD, feedback.getQuestionMood());
        values.put(DatabaseHelper.QUESTION_CONCENTRATION, feedback.getQuestionConcentration());
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
                Integer.parseInt(cursor.getString(6)),
                cursor.getString(11)
        );
        // return Feedback
        return f;
    }

    public List<Feedback> getAllFeedbacks(long userId) {
        Cursor cursor;
        if (userId != -1)
            cursor = database.query(DatabaseHelper.TABLE_FEEDBACK, allColumns, DatabaseHelper.USER_ID + "=?",  new String[]{String.valueOf(userId)}, null, null, null);
        else
            cursor = database.query(DatabaseHelper.TABLE_FEEDBACK, allColumns, null,  null, null, null, null);

        List<Feedback> feedbacks = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Feedback feedback = new Feedback();
                feedback.setFbId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.FB_ID)));
                feedback.setUserId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.USER_ID)));
                feedback.setDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DATE)));
                feedback.setQuestionBusy(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTION_BUSY)));
                feedback.setQuestionRested(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTION_RESTED)));
                feedback.setQuestionMood(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTION_MOOD)));
                feedback.setQuestionConcentration(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTION_CONCENTRATION)));
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
        values.put(DatabaseHelper.QUESTION_BUSY, feedback.getQuestionBusy());
        values.put(DatabaseHelper.QUESTION_RESTED, feedback.getQuestionRested());
        values.put(DatabaseHelper.QUESTION_MOOD, feedback.getQuestionMood());
        values.put(DatabaseHelper.QUESTION_CONCENTRATION, feedback.getQuestionConcentration());
        values.put(DatabaseHelper.REFUSAL_REASON, feedback.getRefusalReason());

        // updating row
        return database.update(DatabaseHelper.TABLE_FEEDBACK, values,
                DatabaseHelper.FB_ID + "=?", new String[]{String.valueOf(feedback.getFbId())});
    }

    // Deleting feedback
    public void removeFeedback(Feedback feedback) {

        database.delete(DatabaseHelper.TABLE_FEEDBACK, DatabaseHelper.FB_ID + "=" + feedback.getFbId(), null);
    }

    public int counter(long userId){

        //Cursor cursor = database.query(DatabaseHelper.TABLE_FEEDBACK, allColumns, DatabaseHelper.USER_ID + "=? AND substr(" +DatabaseHelper.DATE +", 10)=?", new String[]{String.valueOf(userId), utils.getCurrentTimeString("yyyy-MM-dd")}, null, null, null, null);
        Cursor cursor = database.rawQuery("SELECT FB_ID, USER_ID, DATE, QUESTION_BUSY, QUESTION_RESTED, QUESTION_MOOD, QUESTION_CONCENTRATION, QUESTION_CONCENTRATION, REFUSAL_REASON FROM feedbacks WHERE USER_ID= "+String.valueOf(userId)+" AND DATE LIKE '"+ utils.getCurrentTimeString("yyyy-MM-dd") + "%'", null);
            return cursor.getCount();

    }

    public ArrayList <Entry> getBusyness(long userId){

        //Cursor cursor = database.query(DatabaseHelper.TABLE_FEEDBACK, allColumns, DatabaseHelper.USER_ID + "=? AND substr(" +DatabaseHelper.DATE +", 10)=?", new String[]{String.valueOf(userId), utils.getCurrentTimeString("yyyy-MM-dd")}, null, null, null, null);
        String query = "",
                formattedDate = "";

        int value;
        Calendar cal;

       ArrayList entries = new ArrayList<Entry>();

        for (int i = 0; i< Constants.DAYS_IN_A_WEEK; i++)
        {
            cal = Calendar.getInstance();
            cal.add(Calendar.DATE,-(Constants.DAYS_IN_A_WEEK-i-1));
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            formattedDate=format1.format(cal.getTime());
            Cursor cursor = database.rawQuery("SELECT QUESTION_BUSY FROM feedbacks WHERE USER_ID= "+String.valueOf(userId)+" AND DATE LIKE '"+ formattedDate + "%' LIMIT 1", null);
            value = 0;
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                value =  cursor.getInt(cursor.getColumnIndex(DatabaseHelper.QUESTION_BUSY));
            }

            entries.add(new Entry(i,value ));
        }

        return entries;

    }


}
