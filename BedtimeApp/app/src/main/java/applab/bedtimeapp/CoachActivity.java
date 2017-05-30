package applab.bedtimeapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import applab.bedtimeapp.db.ResultOperations;
import applab.bedtimeapp.model.Alarm;
import applab.bedtimeapp.model.Feedback;


public class CoachActivity extends AppCompatActivity {

    private int height;
    private ResultOperations feedbackData;
    private ResultOperations alarmData;
    private final int DATES = 1;
    private final int RATING = 2;

    private Date getDateFromString(String str) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach);

        feedbackData = new ResultOperations(this);
        feedbackData.open();

        ArrayList<Integer> restednesses = new ArrayList<Integer>();
        ArrayList<Integer> busynesses = new ArrayList<Integer>();
        ArrayList<Integer> moods = new ArrayList<Integer>();
        ArrayList<Integer> concentrations = new ArrayList<Integer>();
        ArrayList<Date> datesF = new ArrayList<Date>();

        ArrayList<Feedback> feedbacks = new ArrayList<>(feedbackData.getAllFeedbacks(-1));
        feedbackData.close();

        for (int i = 0; i < feedbacks.size(); i++) {
            datesF.add(getDateFromString(feedbacks.get(i).getDate()));
            restednesses.add(feedbacks.get(i).getQuestionRested());
            busynesses.add(feedbacks.get(i).getQuestionBusy());
            moods.add(feedbacks.get(i).getQuestionMood());
            concentrations.add(feedbacks.get(i).getQuestionConcentration());
        }

        alarmData = new ResultOperations(this);
        alarmData.open();


        //  Initialize SharedPreferences
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        int userID = getPrefs.getInt("userID", 0);

        ArrayList<Alarm> alarms = new ArrayList<>(alarmData.getAllAlarms(userID));
        ArrayList<Date> datesA = new ArrayList<Date>();
        ArrayList<Integer> sleeprates = new ArrayList<Integer>();
        alarmData.close();

        for (int i = 0; i < alarms.size(); i++) {
            datesA.add(getDateFromString(alarms.get(i).getDate()));
            sleeprates.add(alarms.get(i).getSleepRate());
        }

//        ArrayList<Date> datesACleaned = (ArrayList<Date>) cleanData(datesA, sleeprates, DATES);
//        ArrayList<Integer> sleepratesCleaned = (ArrayList<Integer>) cleanData(datesA, sleeprates, RATING);

        ArrayList<Date> datesFCleaned = (ArrayList<Date>) cleanData(datesF, restednesses, DATES);
        ArrayList<Integer> restednessCleaned = (ArrayList<Integer>) cleanData(datesF, restednesses, RATING);
        ArrayList<Integer> busynessCleaned = (ArrayList<Integer>) cleanData(datesF, busynesses, RATING);
        ArrayList<Integer> moodsCleaned = (ArrayList<Integer>) cleanData(datesF, moods, RATING);
        ArrayList<Integer> concentrationCleaned = (ArrayList<Integer>) cleanData(datesF, concentrations, RATING);


        Log.e("Busy", busynessCleaned.toString());
        Log.e("Rested",restednessCleaned.toString());

        SimpleRegression regr = getRegression(restednessCleaned,busynessCleaned);
        Log.e("Slope",Double.toString(regr.getSlope()));
        Log.e("Significance",Double.toString(regr.getSignificance()));
        Log.e("MSE",Double.toString(regr.getMeanSquareError()));

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

    public SimpleRegression getRegression(ArrayList<Integer> firstList, ArrayList<Integer> secondList) {

        SimpleRegression regression = new SimpleRegression();

        for (int i = 0; i < firstList.size(); i++){
            regression.addData(((double) firstList.get(i)), ((double) secondList.get(i)));
        }

        return regression;
    }

    public ArrayList cleanData(ArrayList<Date> dates, ArrayList<Integer> rating, int mode) {

        ArrayList<Date> datesCleaned = new ArrayList<Date>();
        ArrayList<Integer> ratingCleaned = new ArrayList<Integer>();
        ArrayList<Integer> stash = new ArrayList<Integer>();
        Date currentDate = dates.get(0);
        for (int i = 0; i < dates.size(); i++) {
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();

            c1.setTime(dates.get(i));
            c2.setTime(currentDate);

            int yearDiff = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
            int monthDiff = c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
            int dayDiff = c1.get(Calendar.DAY_OF_MONTH) - c2.get(Calendar.DAY_OF_MONTH);

            if (!(yearDiff == 0 && monthDiff == 0 && dayDiff == 0)) {
                datesCleaned.add(dates.get(i - 1));
                stash.removeAll(Collections.singleton(null));
                ratingCleaned.add(stash.get(0));
                stash = new ArrayList<Integer>();
                stash.add(rating.get(i));
                currentDate = dates.get(i);
            } else {
                stash.add(rating.get(i));
            }
        }

        datesCleaned.add(dates.get(dates.size() - 1));
        stash.removeAll(Collections.singleton(null));
        ratingCleaned.add(stash.get(0));

        if (mode == DATES) {
            return dates;
        } else if (mode == RATING) {
            return rating;
        }

        return null;

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
