package applab.bedtimeapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import applab.bedtimeapp.model.SelfEfficacy;
import applab.bedtimeapp.utils.Constants;
import applab.bedtimeapp.utils.utils;

/**
 * Created by berberakif on 17/04/17.
 */

public class SelfEfficacyOperations {

    public static final String LOGTAG = "SE_MNGMNT_SYS";

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            DatabaseHelper.SE_ID,
            DatabaseHelper.USER_ID,
            DatabaseHelper.DATE,
            DatabaseHelper.Q1,
            DatabaseHelper.Q2,
            DatabaseHelper.Q3,
            DatabaseHelper.Q4,
            DatabaseHelper.Q5,
            DatabaseHelper.Q6,
            DatabaseHelper.Q7,
            DatabaseHelper.Q8,
            DatabaseHelper.Q9,
            DatabaseHelper.Q10
    };

    public SelfEfficacyOperations(Context context) {
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
    public SelfEfficacy addSelfEfficacy(SelfEfficacy selfEfficacy) {

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.USER_ID, selfEfficacy.getUserId());
        values.put(DatabaseHelper.DATE, selfEfficacy.getDate());
        values.put(DatabaseHelper.Q1, selfEfficacy.getQ1());
        values.put(DatabaseHelper.Q2, selfEfficacy.getQ2());
        values.put(DatabaseHelper.Q3, selfEfficacy.getQ3());
        values.put(DatabaseHelper.Q4, selfEfficacy.getQ4());
        values.put(DatabaseHelper.Q5, selfEfficacy.getQ5());
        values.put(DatabaseHelper.Q6, selfEfficacy.getQ6());
        values.put(DatabaseHelper.Q7, selfEfficacy.getQ7());
        values.put(DatabaseHelper.Q8, selfEfficacy.getQ8());
        values.put(DatabaseHelper.Q9, selfEfficacy.getQ9());
        values.put(DatabaseHelper.Q10, selfEfficacy.getQ10());


        long insertId = database.insert(DatabaseHelper.TABLE_SELF_EFFICACY, null, values);
        selfEfficacy.setSeId(insertId);
        return selfEfficacy;

    }

    // select
    public SelfEfficacy getSelfEfficacy(long id) {

        Cursor cursor = database.query(DatabaseHelper.TABLE_SELF_EFFICACY, allColumns, DatabaseHelper.SE_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        SelfEfficacy se = new SelfEfficacy(
                Long.parseLong(cursor.getString(0)),
                Long.parseLong(cursor.getString(1)),
                cursor.getString(2),
                Integer.parseInt(cursor.getString(3)),
                Integer.parseInt(cursor.getString(4)),
                Integer.parseInt(cursor.getString(5)),
                Integer.parseInt(cursor.getString(6)),
                Integer.parseInt(cursor.getString(7)),
                Integer.parseInt(cursor.getString(8)),
                Integer.parseInt(cursor.getString(9)),
                Integer.parseInt(cursor.getString(10)),
                Integer.parseInt(cursor.getString(11)),
                Integer.parseInt(cursor.getString(12))
        );
        // return SelfEfficacy
        return se;
    }

    public List<SelfEfficacy> getAllSelfEfficacies() {

        Cursor cursor = database.query(DatabaseHelper.TABLE_SELF_EFFICACY, allColumns, null, null, null, null, null);

        List<SelfEfficacy> selfEfficacies = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                SelfEfficacy selfEfficacy = new SelfEfficacy();
                selfEfficacy.setSeId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.SE_ID)));
                selfEfficacy.setUserId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.USER_ID)));
                selfEfficacy.setDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DATE)));
                selfEfficacy.setQ1(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Q1)));
                selfEfficacy.setQ2(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Q2)));
                selfEfficacy.setQ3(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Q3)));
                selfEfficacy.setQ4(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Q4)));
                selfEfficacy.setQ5(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Q5)));
                selfEfficacy.setQ6(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Q6)));
                selfEfficacy.setQ7(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Q7)));
                selfEfficacy.setQ8(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Q8)));
                selfEfficacy.setQ9(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Q9)));
                selfEfficacy.setQ10(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Q10)));

                selfEfficacies.add(selfEfficacy);
            }
        }
        // return All ses
        return selfEfficacies;
    }


    // Updating SelfEfficacy
    public int updateSelfEfficacy(SelfEfficacy selfEfficacy) {


        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.USER_ID, selfEfficacy.getUserId());
        values.put(DatabaseHelper.DATE, selfEfficacy.getDate());
        values.put(DatabaseHelper.Q1, selfEfficacy.getQ1());
        values.put(DatabaseHelper.Q2, selfEfficacy.getQ2());
        values.put(DatabaseHelper.Q3, selfEfficacy.getQ3());
        values.put(DatabaseHelper.Q4, selfEfficacy.getQ4());
        values.put(DatabaseHelper.Q5, selfEfficacy.getQ5());
        values.put(DatabaseHelper.Q6, selfEfficacy.getQ6());
        values.put(DatabaseHelper.Q7, selfEfficacy.getQ7());
        values.put(DatabaseHelper.Q8, selfEfficacy.getQ8());
        values.put(DatabaseHelper.Q9, selfEfficacy.getQ9());
        values.put(DatabaseHelper.Q10, selfEfficacy.getQ10());


        // updating row
        return database.update(DatabaseHelper.TABLE_SELF_EFFICACY, values,
                DatabaseHelper.SE_ID + "=?", new String[]{String.valueOf(selfEfficacy.getSeId())});
    }

    // Deleting se
    public void removeSelfEfficacy(SelfEfficacy selfEfficacy) {

        database.delete(DatabaseHelper.TABLE_SELF_EFFICACY, DatabaseHelper.SE_ID + "=" + selfEfficacy.getSeId(), null);
    }


    public int counter(long userId){
        //Cursor cursor = database.query(DatabaseHelper.TABLE_SELF_EFFICACY, allColumns, DatabaseHelper.USER_ID + "=? AND substr(" +DatabaseHelper.DATE +", 10)=?", new String[]{String.valueOf(userId), utils.getCurrentTimeString("yyyy-MM-dd")}, null, null, null, null);
        // TODO not like this, maybe using SharedPreferences
        Cursor cursor = database.rawQuery("SELECT DATE FROM efficacy WHERE USER_ID= "+String.valueOf(userId), null);

        return cursor.getCount();
    }
}
