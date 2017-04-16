package applab.bedtimeapp.utils;

import android.util.Log;

import java.util.Calendar;

import static java.lang.Math.abs;

public class utils {

    public static String getFullTime(int hourOfDay, int minute) {
        String hour;
        String min;
        if (hourOfDay < 10) {
            hour = "0" + Integer.toString(hourOfDay);
        } else {
            hour = Integer.toString(hourOfDay);
        }

        if (minute < 10) {
            min = "0" + Integer.toString(minute);
        } else {
            min = Integer.toString(minute);
        }

        return hour + ":" + min;

    }

    public static int getDelay(int hour, int minute){

        final Calendar c;
        int current_h;
        int current_m;
        int delay=0;
        c = java.util.Calendar.getInstance();
        current_h = c.get(Calendar.HOUR_OF_DAY);
        current_m = c.get(Calendar.MINUTE);

        Log.e("Hour",Integer.toString(hour));
        Log.e("Hour Cur",Integer.toString(current_h));
        Log.e("Min",Integer.toString(minute));
        Log.e("Min Cur",Integer.toString(current_m));

        //bedtime in future
        if (hour >= current_h) {
            //e.g. current_h - hour
            //e.g. 18:05 - 22:16
            //e.g 02:11 - 06:05
            delay =  (hour - current_h)*60;
            delay = delay - current_m + minute;
        } else {
            //e.g 18:05 - 1:11
            //e.g 06:11 - 02:05
            delay = (hour + 24 - current_h)*60;
            delay = delay - current_m + minute;
        }
        return delay*60000;
    }

    //normal starting degree is 15:00/03:00
    public static Float getStartingAngle(int hourOfDay, int minute) {
        Float target_degree = 0.0f;
        if (hourOfDay > 15) {
            target_degree = (hourOfDay - 15) * 30.0f;
        } else if (hourOfDay < 3) {
            target_degree = (hourOfDay + 9) * 30.0f;
        } else {
            target_degree = (hourOfDay - 3) * 30.0f;
        }
        target_degree = target_degree + minute * 0.5f;
        return target_degree;
    }

    public static Float getProgress(int hourOfDay, int minute, int current_alarm, int current_alarm_m){

        Float prog = Math.abs(24 - hourOfDay + current_alarm)*30.0f;
        prog = prog - minute*0.5f;
        prog = prog + current_alarm_m*0.5f;
        return prog;
    }

    public static Float getRotAngleClockBegin(int hourOfDay, int minute, int current_hour,
                                              int current_minute) {
        Float target_degree = (hourOfDay - current_hour) * 30.0f - current_minute * 0.5f;
        target_degree = target_degree + minute * 0.5f;

        return target_degree;
    }


}
