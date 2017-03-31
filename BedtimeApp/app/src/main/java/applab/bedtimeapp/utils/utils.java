package applab.bedtimeapp.utils;

public class utils {

    public static String getFullTime(int hourOfDay,int minute){
        String hour;
        String min;
        if (hourOfDay < 10){
            hour = "0" + Integer.toString(hourOfDay);
        } else {
            hour = Integer.toString(hourOfDay);
        }

        if (minute < 10 ){
            min = "0" + Integer.toString(minute);
        } else {
            min = Integer.toString(minute);
        }

        return hour + ":" + min;

    }


}
