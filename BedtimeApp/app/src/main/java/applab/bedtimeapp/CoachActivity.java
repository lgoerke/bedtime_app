package applab.bedtimeapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import applab.bedtimeapp.db.ResultOperations;
import applab.bedtimeapp.model.Alarm;
import applab.bedtimeapp.model.Feedback;
import applab.bedtimeapp.model.Result;
import applab.bedtimeapp.utils.utils;


public class CoachActivity extends AppCompatActivity {

    private int height;
    private ResultOperations resultData;

    private final int DURATION_FIRST_PERIOD = 7;
    private final int DURATION_SECOND_PERIOD = 7;

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

        resultData = new ResultOperations(this);
        resultData.open();

        ArrayList<Integer> restednesses = new ArrayList<Integer>();
        ArrayList<Integer> busynesses = new ArrayList<Integer>();
        ArrayList<Integer> moods = new ArrayList<Integer>();
        ArrayList<Integer> concentrations = new ArrayList<Integer>();
        ArrayList<Integer> procastination = new ArrayList<Integer>();


        //  Initialize SharedPreferences
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        int userID = getPrefs.getInt("userID", 0);

        ArrayList<Result> feedbacks = new ArrayList<>(resultData.getAllResults(userID));
        for (int i = 0; i < feedbacks.size(); i++) {
            Log.e(Integer.toString(i), feedbacks.get(i).toString());
        }

        resultData.close();

        Collections.sort(feedbacks, new Comparator<Result>() {
            @Override
            public int compare(Result result, Result t1) {
                if (result.getDayId() < t1.getDayId()) {
                    return -1;
                } else if (result.getDayId() == t1.getDayId()) {
                    return 0;
                } else if (result.getDayId() > t1.getDayId()) {
                    return +1;
                }
                return 0;
            }
        });

        for (int i = 0; i < DURATION_FIRST_PERIOD; i++) {
            restednesses.add(feedbacks.get(i).getQuestionRested());
            busynesses.add(feedbacks.get(i).getQuestionBusy());
            moods.add(feedbacks.get(i).getQuestionMood());
            concentrations.add(feedbacks.get(i).getQuestionConcentration());
            procastination.add(feedbacks.get(i).getProcrastinationDuration());
        }

        ArrayList<ArrayList> all = new ArrayList<>();
        ArrayList<String> all_names = new ArrayList<>();
        ArrayList<Double> slopes = new ArrayList<>();
        ArrayList<Double> significance = new ArrayList<>();
        ArrayList<Double> mse = new ArrayList<>();
        ArrayList<String> pairs1 = new ArrayList<>();
        ArrayList<String> pairs2 = new ArrayList<>();

        all.add(restednesses);
        all_names.add("restedness");
        all.add(busynesses);
        all_names.add("busyness");
        all.add(moods);
        all_names.add("mood");
        all.add(concentrations);
        all_names.add("concentration");
        all.add(procastination);
        all_names.add("procrastination duration");

        for (int i = 0; i < all.size(); i++) {
            for (int j = 0; j < all.size(); j++) {
                if (i != j) {
                    SimpleRegression regr = getRegression(all.get(i), all.get(j));
                    pairs1.add(all_names.get(i));
                    pairs2.add(all_names.get(j));
                    slopes.add(regr.getSlope());
                    significance.add(regr.getSignificance());
                    mse.add(regr.getMeanSquareError());
                }
            }
        }

        int needed_advises = DURATION_SECOND_PERIOD;
        ArrayList<String> advices = new ArrayList<>();
        // First, search for all significant correlations
        for (int i = 0; i < pairs1.size(); i++) {
            if (needed_advises > 0) {
                if (significance.get(i) <= 0.05) {
                    String s = null;
                    if (slopes.get(i) > 0) {
                        s = "Keep up the good work! Last week your " + pairs1.get(i) + " had a positive influence on your " + pairs2.get(i);
                    } else if (slopes.get(i) < 0) {
                        s = "Last week your " + pairs1.get(i) + " had a negative influence on your " + pairs2.get(i);
                    }
                    advices.add(s);
                    needed_advises -= 1;
                }
            }
        }


        // Then search for weaker correlations
        for (int i = 0; i < pairs1.size(); i++) {
            if (needed_advises > 0) {
                if (significance.get(i) > 0.05) {
                    if (slopes.get(i) < (-0.5) || slopes.get(i) > 0.5) {
                        String s = null;
                        if (slopes.get(i) > 0) {
                            s = "Last week your " + pairs1.get(i) + " had a positive influence on your " + pairs2.get(i);
                        } else if (slopes.get(i) < 0) {
                            s = "Last week your " + " your " + pairs1.get(i) + " had a negative influence on your " + pairs2.get(i);
                        }
                        advices.add(s);
                        needed_advises -= 1;
                    }
                }
            }
        }


        // Then sample from random sentences

        ArrayList<String> random_advice = new ArrayList<>();
        random_advice.add("Keep up the good work!");
        random_advice.add("It does not matter how slowly you go as long as you do not stop. *Confucius");
        random_advice.add("Setting goals is the first step in turning the invisible into the visible. *Tony Robbins");
        random_advice.add("Accept the challenges so that you can feel the exhilaration of victory. *George S. Patton");
        random_advice.add("You can do it.");
        random_advice.add("The secret of getting ahead is getting started. *Mark Twain");
        random_advice.add("Ask yourself if what you're doing today is getting you closer to where you want to be tomorrow.");

        int index = 0;
        while (needed_advises > 0){
            advices.add(random_advice.get(index));
            needed_advises -= 1;
            index += 1;
        }

        Log.e("complete list",advices.toString());

        ArrayList<String> shuffled_advicese = new ArrayList<>();
        shuffled_advicese.add(advices.remove(0));
        Collections.shuffle(advices);
        for (int i = 0; i < advices.size(); i++){
            shuffled_advicese.add(advices.get(i));
        }

        Log.e("complete list",shuffled_advicese.toString());

        int currentDayId = (int) utils.getDayId(this);
        //TODO is this the correct way to get integer day id?
        TextView tv = (TextView) findViewById(R.id.advice);
        tv.setText(shuffled_advicese.get(currentDayId-DURATION_FIRST_PERIOD));

    }

    public SimpleRegression getRegression(ArrayList<Integer> firstList, ArrayList<Integer> secondList) {

        SimpleRegression regression = new SimpleRegression();

        for (int i = 0; i < firstList.size(); i++) {
            regression.addData(((double) firstList.get(i)), ((double) secondList.get(i)));
        }

        return regression;
    }


    /**
     * Finish this activity and return to main activity
     *
     * @param view
     */
    public void goToMain(View view) {
        finish();
    }

}
