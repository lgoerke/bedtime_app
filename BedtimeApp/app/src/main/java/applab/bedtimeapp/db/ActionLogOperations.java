package applab.bedtimeapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import applab.bedtimeapp.model.ActionLog;
import applab.bedtimeapp.utils.ActionStatus;
import applab.bedtimeapp.utils.ActionType;

/**
 * Created by berberakif on 17/04/17.
 */
// TODO: 23/05/17 add all the alarms and notifications to log
    // control the status of all records, and call notification if they are not called yet after reboot
    //
public class ActionLogOperations {

    public static final String LOGTAG = "ACTION_LOG_MNGMNT_SYS";

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            DatabaseHelper.ACTION_LOG_ID,
            DatabaseHelper.USER_ID,
            DatabaseHelper.CREATION_DATE_TIME,
            DatabaseHelper.LAST_UPDATE_DATE_TIME,
            DatabaseHelper.ACTION_DATE_TIME,
            DatabaseHelper.ACTION_TYPE,
            DatabaseHelper.ACTION_STATUS

    };

    public ActionLogOperations(Context context) {
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
    public ActionLog addActionLog(ActionLog actionLog) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.USER_ID, actionLog.getUserId());
        values.put(DatabaseHelper.ACTION_DATE_TIME, actionLog.getActionDateTime());
        values.put(DatabaseHelper.CREATION_DATE_TIME, actionLog.getCreationDateTime());
        values.put(DatabaseHelper.LAST_UPDATE_DATE_TIME, actionLog.getLastUpdateDateTime());
        values.put(DatabaseHelper.ACTION_STATUS, actionLog.getActionStatus().name());
        values.put(DatabaseHelper.ACTION_TYPE, actionLog.getActionType().name());


        long insertId = database.insert(DatabaseHelper.TABLE_ACTION_LOG, null, values);
        actionLog.setActionLogId(insertId);
        return actionLog;

    }

    // select
    public ActionLog getActionLog(long id) {

        Cursor cursor = database.query(DatabaseHelper.TABLE_ACTION_LOG, allColumns, DatabaseHelper.ACTION_LOG_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ActionLog a = new ActionLog(
                Long.parseLong(cursor.getString(0)),
                Long.parseLong(cursor.getString(1)),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                ActionType.valueOf(cursor.getString(5)),
                ActionStatus.valueOf(cursor.getString(6))
                );

        // return
        return a;
    }


    public List<ActionLog> getAllActionLogs(int userId) {

        Cursor cursor;

        if (userId != -1)
            cursor = database.query(DatabaseHelper.TABLE_ACTION_LOG, allColumns, DatabaseHelper.USER_ID + "=?",  new String[]{String.valueOf(userId)}, null, null, null);
        else
            cursor = database.query(DatabaseHelper.TABLE_ACTION_LOG, allColumns, null,  null, null, null, null);

        List<ActionLog> actionLogs = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                ActionLog actionLog = new ActionLog();
                actionLog.setActionLogId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.ACTION_LOG_ID)));
                actionLog.setUserId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.USER_ID)));
                actionLog.setActionDateTime(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ACTION_DATE_TIME)));
                actionLog.setCreationDateTime(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CREATION_DATE_TIME)));
                actionLog.setLastUpdateDateTime(cursor.getString(cursor.getColumnIndex(DatabaseHelper.LAST_UPDATE_DATE_TIME)));
                actionLog.setActionType(ActionType.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ACTION_TYPE))));
                actionLog.setActionStatus(ActionStatus.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ACTION_STATUS))));

                actionLogs.add(actionLog);
            }
        }
        return actionLogs;
    }


    // Updating actionLog
    public int updateActionLog(ActionLog actionLog) {


        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.USER_ID, actionLog.getUserId());
        values.put(DatabaseHelper.ACTION_DATE_TIME, actionLog.getActionDateTime());
        values.put(DatabaseHelper.CREATION_DATE_TIME, actionLog.getCreationDateTime());
        values.put(DatabaseHelper.LAST_UPDATE_DATE_TIME, actionLog.getLastUpdateDateTime());
        values.put(DatabaseHelper.ACTION_TYPE, actionLog.getActionType().name());
        values.put(DatabaseHelper.ACTION_STATUS, actionLog.getActionStatus().name());

        // updating row
        return database.update(DatabaseHelper.TABLE_ACTION_LOG, values,
                DatabaseHelper.ACTION_LOG_ID + "=?", new String[]{String.valueOf(actionLog.getActionLogId())});
    }

    // Deleting actionLog
    public void removeActionLog(ActionLog actionLog) {

        database.delete(DatabaseHelper.TABLE_ACTION_LOG, DatabaseHelper.ACTION_LOG_ID + "=" + actionLog.getActionLogId(), null);
    }


}
