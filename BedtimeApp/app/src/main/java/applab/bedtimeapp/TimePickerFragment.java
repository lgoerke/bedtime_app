package applab.bedtimeapp;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.IntentService;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;

import applab.bedtimeapp.utils.utils;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    private String message;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        message = getArguments().getString("ALARM_TYPE");

        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        //Do something with the user chosen time
        //Get reference of host activity (XML Layout File) TextView widget
        TextView tv;
        if (message == "bedtime") {
            tv  = (TextView) getActivity().findViewById(R.id.clockBedtime);
        } else {
            tv = (TextView) getActivity().findViewById(R.id.clockMorning);
        }
        Log.d("Hour asdf",Integer.toString(hourOfDay));
        Log.d("Hour minute",Integer.toString(minute));
        tv.setText(utils.getFullTime(hourOfDay,minute));
    }
}