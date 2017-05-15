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

        setResultCode(Activity.RESULT_OK);
        Intent intent_alarm = new Intent(AlarmDrawerActivity.instance(), AlarmSnoozeActivity.class);
        intent_alarm.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent_alarm.putExtra("showAlert", AlarmDrawerActivity.instance().isShowAlert());
        intent_alarm.putExtra("whichLanding", AlarmDrawerActivity.instance().getWhichLanding());
        intent_alarm.putExtra("whichIcon", AlarmDrawerActivity.instance().getWhichIcon());
        AlarmDrawerActivity.instance().startActivity(intent_alarm);

    }
}
