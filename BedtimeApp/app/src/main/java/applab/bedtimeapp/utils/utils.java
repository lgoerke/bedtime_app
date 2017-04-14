package applab.bedtimeapp.utils;

import android.util.Log;

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
