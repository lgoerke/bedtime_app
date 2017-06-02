package applab.bedtimeapp.utils;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

import applab.bedtimeapp.MainDrawerActivity;
import applab.bedtimeapp.QuestionnaireActivity;
import applab.bedtimeapp.db.ResultOperations;

public class RebootService extends IntentService {

    public RebootService(String name) {
        super(name);
    }

    public RebootService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String intentType = intent.getExtras().getString("caller");
        if (intentType == null) return;
        if (intentType.equals("RebootReceiver"))
            createNotifications();
            createAlarms();

    }

    // TODO  GET THE ALARM TIME AND SET THEM AGAIN AFTER REBOOT
    private void createAlarms() {
        ResultOperations alarmData;
        alarmData = new ResultOperations(this);
        alarmData.open();
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        int userID = getPrefs.getInt("userID", 0);

        int hourMinute[] = alarmData.getAlarmTime(userID, getBaseContext()) ;

        alarmData.close();

        if(hourMinute[0] != -1) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourMinute[0]);
            calendar.set(Calendar.MINUTE, hourMinute[1]);
            Intent myIntent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);
            AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);

            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    // TODO:  control all notifications
    public  void createNotifications() {

        ResultOperations feedbackData;
        feedbackData = new ResultOperations(this);
        feedbackData.open();

        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        int userID = getPrefs.getInt("userID", 0);
        int hourMinute[] = feedbackData.getNotificationTime(userID, getBaseContext()) ;
        feedbackData.close();

        if(hourMinute[0] != -1) {
            int delayForNotification = utils.getDelay(hourMinute[0], hourMinute[1]);
            Log.d("Delay: ", String.valueOf(delayForNotification));
            NotificationHelper.scheduleNotification(this, NotificationHelper.getNotification(this, "Please fill in the Questionnaire", QuestionnaireActivity.class), delayForNotification);
        }

    }



}