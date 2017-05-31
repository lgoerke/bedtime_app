package applab.bedtimeapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    public static int getDelay(int hour, int minute) {

        final Calendar c;
        int current_h;
        int current_m;
        int current_s;
        int current_ms;
        int delay = 0;
        c = java.util.Calendar.getInstance();
        current_h = c.get(Calendar.HOUR_OF_DAY);
        current_m = c.get(Calendar.MINUTE);
        current_s = c.get(Calendar.SECOND);
        current_ms = c.get(Calendar.MILLISECOND);


        Log.e("Hour", Integer.toString(hour));
        Log.e("Hour Cur", Integer.toString(current_h));
        Log.e("Min", Integer.toString(minute));
        Log.e("Min Cur", Integer.toString(current_m));

        //bedtime in future
        if (hour >= current_h) {
            //e.g. current_h - hour
            //e.g. 18:05 - 22:16
            //e.g 02:11 - 06:05
            delay = (hour - current_h) * Constants.MINUTES_IN_AN_HOUR;
            delay = delay - current_m + minute;
        } else {
            //e.g 18:05 - 1:11
            //e.g 06:11 - 02:05
            delay = (hour + Constants.HOURS_IN_A_DAY - current_h) * Constants.MINUTES_IN_AN_HOUR;
            delay = delay - current_m + minute;
        }
        return (delay * Constants.MILLISECONDS_IN_A_SECOND * Constants.SECONDS_IN_A_MINUTE)
                - (current_s * Constants.MILLISECONDS_IN_A_SECOND)
                - current_ms;
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

    public static Float getProgress(int hourOfDay, int minute, int current_alarm, int current_alarm_m) {

        Float prog = Math.abs(24 - hourOfDay + current_alarm) * 30.0f;
        prog = prog - minute * 0.5f;
        prog = prog + current_alarm_m * 0.5f;
        return prog;
    }

    public static Float getRotAngleClockBegin(int hourOfDay, int minute, int current_hour,
                                              int current_minute) {
        Float target_degree = (hourOfDay - current_hour) * 30.0f - current_minute * 0.5f;
        target_degree = target_degree + minute * 0.5f;

        return target_degree;
    }

    public static String getCurrentTimeString(String dateFormat){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat(dateFormat);
        return format1.format(cal.getTime());
    }

    public static String[] getWeekDays(){
        Calendar cal = Calendar.getInstance();
        int dayOfTheWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayOfTheWeek ==1)
            return new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        else if (dayOfTheWeek ==2)
            return new String[]{"Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Mon"};
        else if (dayOfTheWeek ==3)
            return new String[]{"Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue"};
        else if (dayOfTheWeek ==4)
            return new String[]{"Thu", "Fri", "Sat", "Sun", "Mon", "Tue", "Wed"};
        else if (dayOfTheWeek ==5)
            return new String[]{"Fri","Sat", "Sun", "Mon", "Tue", "Wed", "Thu"};
        else if (dayOfTheWeek ==6)
            return new String[]{"Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri"};
        else if (dayOfTheWeek ==7)
            return new String[]{"Sun","Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        else
            return new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

    }

    public static long getDayId(Context context) {

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

        Calendar currentDateCal = Calendar.getInstance();

        String currentDate = format1.format(currentDateCal.getTime());

        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String startDate = getPrefs.getString("startDate", currentDate);


        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format1.parse(startDate);
            date2 = format1.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = date2.getTime() - date1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;

    }
}
