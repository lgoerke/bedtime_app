package applab.bedtimeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import applab.bedtimeapp.db.AlarmOperations;
import applab.bedtimeapp.db.FeedbackOperations;
import applab.bedtimeapp.model.Alarm;
import applab.bedtimeapp.model.Feedback;


public class CoachActivity extends AppCompatActivity {

    private int height;
    private FeedbackOperations feedbackData;
    private AlarmOperations alarmData;

    private Date getDateFromString(String str) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("yo",date.toString());
        return date;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach);

        feedbackData = new FeedbackOperations(this);
        feedbackData.open();

        List<Integer> restednesses = new ArrayList<Integer>();
        List<Integer> busynesses = new ArrayList<Integer>();
        List<Integer> moods = new ArrayList<Integer>();
        List<Integer> concentrations = new ArrayList<Integer>();
        List<Date> datesF = new ArrayList<Date>();

        List<Feedback> feedbacks = feedbackData.getAllFeedbacks();
        feedbackData.close();

        for (int i = 0; i < feedbacks.size(); i++) {
            datesF.add(getDateFromString(feedbacks.get(i).getDate()));
            restednesses.add(feedbacks.get(i).getQuestionRested());
            busynesses.add(feedbacks.get(i).getQuestionRested());
            moods.add(feedbacks.get(i).getQuestionRested());
            concentrations.add(feedbacks.get(i).getQuestionRested());
        }

        alarmData = new AlarmOperations(this);
        alarmData.open();

        List<Integer> sleeprates = new ArrayList<Integer>();

        //  Initialize SharedPreferences
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        int userID = getPrefs.getInt("userID", 0);

        List<Alarm> alarms = alarmData.getAllAlarms(userID);
        List<Date> datesA = new ArrayList<Date>();
        alarmData.close();

        for (int i = 0; i < alarms.size(); i++) {
            datesA.add(getDateFromString(alarms.get(i).getDate()));
            sleeprates.add(alarms.get(i).getSleepRate());
        }

        Date d = new Date(2017,05,19);
        datesF.add(d);
        datesF.add(d);
        busynesses.add(null);
        busynesses.add(2);


        Log.e("dates",datesF.toString());
        Log.e("busyness",busynesses.toString());

        ArrayList<Date> datesFcleaned = new ArrayList<Date>();
        ArrayList<Integer> busyCleaned = new ArrayList<Integer>();
        ArrayList<Integer> stash = new ArrayList<Integer>();
        Date currentDate = datesF.get(0);
        for (int i = 0; i < datesF.size(); i++) {
            Log.e("ith data",datesF.get(i).toString());
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();

            c1.setTime(datesF.get(i));
            c2.setTime(currentDate);

            int yearDiff = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
            int monthDiff = c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
            int dayDiff = c1.get(Calendar.DAY_OF_MONTH) - c2.get(Calendar.DAY_OF_MONTH);

            if (!(yearDiff == 0 && monthDiff == 0 && dayDiff == 0)){
                datesFcleaned.add(datesF.get(i));
                Log.e("stash",stash.toString());
                stash.removeAll(Collections.singleton(null));
                Log.e("stash",stash.toString());
                busyCleaned.add(stash.get(0));
                stash = new ArrayList<Integer>();
                stash.add(busynesses.get(i));
                currentDate = datesF.get(i);
            } else {
                stash.add(busynesses.get(i));
            }

            Log.e("dates",datesFcleaned.toString());
            Log.e("busy",busyCleaned.toString());

        }


//        RelativeLayout rl = (RelativeLayout) findViewById(R.id.coach_view);
//        final ImageView iv = (ImageView) rl.findViewById(R.id.owl);
//
//
//        iv.getViewTreeObserver().addOnPreDrawListener(
//                new ViewTreeObserver.OnPreDrawListener() {
//                    public boolean onPreDraw() {
//                        height = iv.getMeasuredHeight();
//                        Log.d("height",Integer.toString(height));
//                        // Do your work here
//                        return true;
//                    }
//                });
//
//        final TextView tv = (TextView) rl.findViewById(R.id.advice);
//
//        tv.getViewTreeObserver().addOnPreDrawListener(
//                new ViewTreeObserver.OnPreDrawListener(){
//                    public boolean onPreDraw(){
//                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                                RelativeLayout.LayoutParams.WRAP_CONTENT,
//                                RelativeLayout.LayoutParams.WRAP_CONTENT
//                        );
//                        params.setMargins(16,height*(2/3),16,0);
//                        tv.setLayoutParams(params);
//
//                        return true;
//                    }
//                });



    }


    /**
     * Finish this activity and return to main activity
     *
     * @param view
     */
    public void goToMain(View view) {
        // TODO are we going to the right activity
        finish();
    }

}
