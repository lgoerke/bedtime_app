package applab.bedtimeapp.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

import applab.bedtimeapp.AlarmDrawerActivity;
import applab.bedtimeapp.AlarmSnoozeActivity;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        setResultCode(Activity.RESULT_OK);
        Intent intent_alarm = new Intent(AlarmDrawerActivity.instance(), AlarmSnoozeActivity.class);
        intent_alarm.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        AlarmDrawerActivity.instance().startActivity(intent_alarm);

    }
}
