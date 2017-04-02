package applab.bedtimeapp.utils;

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


    public static Float getRotAngleClockBegin(int hourOfDay, int minute, int current_hour,
                                         int current_minute) {
        Float target_degree = (hourOfDay - current_hour) * 30.0f - current_minute*0.5f;
        target_degree = target_degree + minute * 0.5f;

        return target_degree;
    }


}
