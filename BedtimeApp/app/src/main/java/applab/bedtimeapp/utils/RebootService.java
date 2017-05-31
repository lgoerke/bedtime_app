package applab.bedtimeapp.utils;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import applab.bedtimeapp.QuestionnaireActivity;

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

    }

    public void createNotifications() {

        // TODO:  control all notifications - alarms
        int hourOfDay = 01;
        int minute = 10;

        int delayForNotification = utils.getDelay(hourOfDay, minute);
        Log.d("Delay: ", String.valueOf(delayForNotification));
        NotificationHelper.scheduleNotification(this, NotificationHelper.getNotification(this, "From Time Picker", QuestionnaireActivity.class), delayForNotification);

    }

}