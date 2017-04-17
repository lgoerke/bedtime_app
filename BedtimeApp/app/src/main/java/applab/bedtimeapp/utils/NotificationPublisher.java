package applab.bedtimeapp.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);

        //notificationManager.cancel(intent.getExtras().getInt("id"));
        //Toast.makeText(context, "HEY HEY HEY", Toast.LENGTH_LONG).show();
        //intent= new Intent(context, QuestionnaireActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ////intent.putExtra("attachMedia",true);
        //context.startActivity(intent);
    }
}