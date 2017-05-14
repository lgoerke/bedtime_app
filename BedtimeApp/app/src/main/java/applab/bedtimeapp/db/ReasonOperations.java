package applab.bedtimeapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import applab.bedtimeapp.model.Reason;

/**
 * Created by berberakif on 17/04/17.
 */

public class ReasonOperations {

    public static final String LOGTAG = "R_MNGMNT_SYS";

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            DatabaseHelper.R_ID,
            DatabaseHelper.USER_ID,
            DatabaseHelper.REASON
    };

    public ReasonOperations(Context context) {
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
    public Reason addReason(Reason reason) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.USER_ID, reason.getUserId());
        values.put(DatabaseHelper.REASON, reason.getReason());


        long insertId = database.insert(DatabaseHelper.TABLE_REASON, null, values);
        reason.setReasonId(insertId);
        return reason;

    }

    // select
    public Reason getReason(long id) {

        Cursor cursor = database.query(DatabaseHelper.TABLE_REASON, allColumns, DatabaseHelper.R_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Reason r = new Reason(
                Long.parseLong(cursor.getString(0)),
                Long.parseLong(cursor.getString(1)),
                cursor.getString(2)

        );
        // return
        return r;
    }

    public List<Reason> getAllReasons() {

        Cursor cursor = database.query(DatabaseHelper.TABLE_REASON, allColumns, null, null, null, null, null);

        List<Reason> reasons = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Reason reason = new Reason();
                reason.setReasonId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.R_ID)));
                reason.setUserId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.USER_ID)));
                reason.setReason(cursor.getString(cursor.getColumnIndex(DatabaseHelper.REASON)));


                reasons.add(reason);
            }
        }
        return reasons;
    }


    // Updating reason
    public int updateReason(Reason reason) {


        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.USER_ID, reason.getUserId());
        values.put(DatabaseHelper.REASON, reason.getReason());

        // updating row
        return database.update(DatabaseHelper.TABLE_REASON, values,
                DatabaseHelper.R_ID + "=?", new String[]{String.valueOf(reason.getReasonId())});
    }

    // Deleting se
    public void removeReason(Reason reason) {

        database.delete(DatabaseHelper.TABLE_REASON, DatabaseHelper.R_ID + "=" + reason.getReasonId(), null);
    }


}
