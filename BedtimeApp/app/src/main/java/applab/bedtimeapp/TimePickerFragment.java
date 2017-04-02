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
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;

import applab.bedtimeapp.utils.utils;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private String message;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        message = getArguments().getString("ALARM_TYPE");

        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

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
        ProgressBar pb = (ProgressBar) ac.findViewById(R.id.progressBar);
        if (message == "bedtime") {
            AnimationSet animSet = new AnimationSet(true);
            animSet.setInterpolator(new DecelerateInterpolator());
            animSet.setFillAfter(true);
            animSet.setFillEnabled(true);

//            Float angle = utils.getRotAngleClockBegin(hourOfDay, minute, AlarmDrawerActivity.current_bedtime, AlarmDrawerActivity.current_bedtime_m);
            AlarmDrawerActivity.current_bedtime = hourOfDay;
            AlarmDrawerActivity.current_bedtime_m = minute;

            // Is not continous, always in relation to 21:00
            final RotateAnimation animRotate = new RotateAnimation(0.0f, 90.0f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);

            animRotate.setDuration(1500);
            animRotate.setFillAfter(true);
            animSet.addAnimation(animRotate);

            pb.startAnimation(animSet);
            int prog = pb.getProgress();
//            prog = prog - Math.round(angle);
            prog = prog - 90;
            Log.d("progress",Integer.toString(prog));
            pb.setProgress(prog);
        } else {
//            pb.setProgress(utils.getProgressFromTime(hourOfDay, minute));
        }

    }
}