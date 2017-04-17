package applab.bedtimeapp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;

import java.util.Calendar;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.lzyzsd.circleprogress.DonutProgress;

import applab.bedtimeapp.db.DatabaseHelper;
import applab.bedtimeapp.utils.NotificationHelper;
import applab.bedtimeapp.utils.utils;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private String message;
    private boolean controlGroup;
    private DatabaseHelper database;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        message = getArguments().getString("ALARM_TYPE");
        controlGroup = getArguments().getBoolean("controlGroup");

        // get database
        //database = new DatabaseHelper(TimePickerFragment.this);
        //SQLiteDatabase db_write = database.getWritableDatabase();

        //Use the current time as the default values for the time picker
        final Calendar c;
        int hour;
        int minute;
        c = java.util.Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //Do something with the user chosen time
        //Get reference of host activity (XML Layout File) TextView widget
        TextView tv;
        if (message == "bedtime") {
            tv = (TextView) getActivity().findViewById(R.id.clockBedtime);
        } else {
            tv = (TextView) getActivity().findViewById(R.id.clockMorning);
        }
        tv.setText(utils.getFullTime(hourOfDay, minute));

        LinearLayout ac = (LinearLayout) getActivity().findViewById(R.id.alarm_content);
        DonutProgress pb = (DonutProgress) ac.findViewById(R.id.progressBar);
        if (message == "bedtime") {
            Log.d("progress before", Integer.toString(Math.round(pb.getProgress())));

            if (hourOfDay == 0) {
                hourOfDay = 24;
            }

            Float starting_angle = utils.getStartingAngle(hourOfDay, minute);
            pb.setStartingDegree(Math.round(starting_angle));
            Float stop_progress = utils.getProgress(hourOfDay, minute, AlarmDrawerActivity.current_alarm, AlarmDrawerActivity.current_alarm_m);
            pb.setProgress(stop_progress);

            AlarmDrawerActivity.current_bedtime = hourOfDay;
            AlarmDrawerActivity.current_bedtime_m = minute;

            int prog = Math.round(pb.getProgress());

            ((AlarmDrawerActivity) getActivity()).changeSleepDuration(prog);

            int delayForNotification = utils.getDelay(hourOfDay,minute);
            Log.d("Delay: ",String.valueOf(delayForNotification));

        } else {

            Log.d("progress before", Integer.toString(Math.round(pb.getProgress())));

            if (hourOfDay == 0) {
                hourOfDay = 24;
            }

            Float prog = pb.getProgress();
            prog = prog + (hourOfDay - AlarmDrawerActivity.current_alarm) * 30.0f;
            prog = prog - AlarmDrawerActivity.current_alarm_m * 0.5f;
            prog = prog + minute * 0.5f;

            pb.setProgress(prog);

            AlarmDrawerActivity.current_alarm = hourOfDay;
            AlarmDrawerActivity.current_alarm_m = minute;

            int p = Math.round(pb.getProgress());

            ((AlarmDrawerActivity) getActivity()).changeSleepDuration(p);

//            pb.setProgress(utils.getProgressFromTime(hourOfDay, minute));
        }

    }
}