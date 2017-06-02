package applab.bedtimeapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import applab.bedtimeapp.MainDrawerActivity;

public class RebootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, RebootService.class);
        serviceIntent.putExtra("caller", "RebootReceiver");
        context.startService(serviceIntent);

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            serviceIntent = new Intent(context, MainDrawerActivity.class);
            context.startService(serviceIntent);
        }

    }
}