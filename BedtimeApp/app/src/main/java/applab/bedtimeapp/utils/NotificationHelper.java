package applab.bedtimeapp.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;

import java.util.Calendar;

import applab.bedtimeapp.QuestionnaireActivity;
import applab.bedtimeapp.R;

public class NotificationHelper {

    public static void scheduleNotification(Context context, Notification notification, int delay) {

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    // with set repeating method, we can add repeating schedule to the alarm if we want
    public static void scheduleNotification(Context context, Notification notification, int delay, int repeatingInHours){
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        //alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        Calendar calendar =  Calendar.getInstance();
        //calendar.set(2014,Calendar.getInstance().get(Calendar.MONTH),Calendar.SUNDAY , 8, 00, 00);

        long when = calendar.getTimeInMillis() + delay;         // notification time
        alarmManager.setRepeating(AlarmManager.RTC, when, AlarmManager.INTERVAL_HOUR * repeatingInHours, pendingIntent);
        //alarmManager.setRepeating(AlarmManager.RTC, when, 60000, pendingIntent);

    }

    public static Notification getNotification(Context context,String content, Class activityClass) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.notification_icon);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);

        // pending activity to call when the notification is clicked
        Intent resultIntent = new Intent(context, activityClass);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        return builder.build();
    }
}
