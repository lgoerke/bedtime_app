package applab.bedtimeapp.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import applab.bedtimeapp.R;

public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);

     /*   NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        //.setVibrate(new long[]{500, 1000})
                        .setSmallIcon(R.drawable.alarm)
                        .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setContentText("COMPAT");

        NotificationManager notifyMgr = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifyMgr.notify(context.getResources().getString(R.string.app_name), id, mBuilder.build());*/

        //notificationManager.cancel(intent.getExtras().getInt("id"));
        //Toast.makeText(context, "HEY HEY HEY", Toast.LENGTH_LONG).show();
        //intent= new Intent(context, QuestionnaireActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ////intent.putExtra("attachMedia",true);
        //context.startActivity(intent);
    }
}