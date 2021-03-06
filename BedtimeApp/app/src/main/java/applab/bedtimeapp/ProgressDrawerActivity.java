package applab.bedtimeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.scalified.fab.ActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import applab.bedtimeapp.db.DatabaseHelper;
import applab.bedtimeapp.db.ResultOperations;
import applab.bedtimeapp.model.Reason;
import applab.bedtimeapp.utils.utils;

public class ProgressDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ProgressDrawerActivity";

    private boolean showDailyAlert = true;
    private boolean showWeeklyAlert = true;
    private int whichLanding = 0;
    // Sheep = 1, Cat = 2
    private int whichIcon = 1;

    private static int LANDING_ALARM = 1;
    private static int LANDING_PROGRESS = 2;
    private static int LANDING_HOME = 3;

    private static int REQUEST_CODE = 1;
    private ResultOperations reasonData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity_progress);

        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        int userID = getPrefs.getInt("userID", 0);

        //get showAlert bool from other Activity
        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null) {
            for (String key : b.keySet()) {
                if (key.equals("showDailyAlert")) {
                    showDailyAlert = (boolean) b.get(key);
                } else if (key.equals("showWeeklyAlert")) {
                    showWeeklyAlert = (boolean) b.get(key);
                }  else if (key.equals("whichLanding")){

                    whichLanding = (int) b.get(key);
                } else if (key.equals("whichIcon")) {
                    whichIcon = (int) b.get(key);
                } else {
                    Object value = b.get(key);
                    Log.d(TAG, String.format("%s %s (%s)", key,
                            value.toString(), value.getClass().getName()));
                }
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //writing name and alias in drawer header
        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
        LinearLayout hv = (LinearLayout) nv.getHeaderView(0);
        TextView tv = (TextView) hv.findViewById(R.id.textViewName);
        tv.setText((userID>0)?String.valueOf(userID):"");
        ImageView iv = (ImageView) hv.findViewById(R.id.avatarIcon);
        if (whichIcon == 1) {
            iv.setImageResource(R.drawable.sheep);
        } else if (whichIcon == 2) {
            iv.setImageResource(R.drawable.cat);
        }



        /* FIRST GRAPH */
        /* Set background color of the graph to be slightly transparent */
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_sleep_quality_duration);
        ll.setBackgroundColor(getResources().getColor(R.color.moon));
        ll.setAlpha(0.8f);
        /* END Set background */

        /* Create chart */
        LineChart chart = (LineChart) findViewById(R.id.sleep_quality_duration);
        /* END Create chart */

        /* Add to data set */
        ArrayList lineData = new ArrayList<ILineDataSet>();
        /* END add data set */

        // get mood data from the db
        ResultOperations feedbackOperations = new ResultOperations(this);
        feedbackOperations.open();
        List entries = feedbackOperations.getField(userID, utils.getDayId(this), DatabaseHelper.QUESTION_MOOD);
        entries = reverse(entries);
        entries = decrementRange(entries,1);

        LineDataSet dataSet = new LineDataSet(entries, "Mood"); // add entries to dataset

        putStyleDataSet(dataSet, getResources().getColor(R.color.foregroundMountain));
        /* END Example mood */
        lineData.add(dataSet);

        /* START example concentraition */
        entries = feedbackOperations.getField(userID, utils.getDayId(this), DatabaseHelper.QUESTION_CONCENTRATION);
        entries = reverse(entries);
        entries = decrementRange(entries,1);

        dataSet = new LineDataSet(entries, "Concentration"); // add entries to dataset

        putStyleDataSet(dataSet, getResources().getColor(R.color.backgroundMountain));
        /* END example concentration */
        lineData.add(dataSet);


        /* Example duration data */

        entries = feedbackOperations.getField(userID, utils.getDayId(this), DatabaseHelper.PROCRASTINATION_DURATION);
        entries = reverse(entries);
        Log.e("proc b", entries.toString());
        entries = changeRange(entries, 120, 4);
        Log.e("proc a", entries.toString());

        dataSet = new LineDataSet(entries, "Procrastination duration"); // add entries to dataset

        dataSet = putStyleDataSet(dataSet, getResources().getColor(R.color.alert));
        /* END Example procrastionation duration */

        /* Add to data set and put in graph */
        lineData.add(dataSet);
        LineData data = new LineData(lineData);
        chart.setData(data);

        Description d = new Description();
        d.setText("Sleep feedback last week");
        chart.setDescription(d);
        /* END add data set */

        /* Put sleep quality on left X Axis */
        final String[] sentiment = new String[]{
                "Very bad", "Bad", "Normal", "Good", "Very good"
        };

        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setAxisMaximum(4f);
        yAxisLeft.setGranularity(1f);
        yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return sentiment[(int) value % sentiment.length];
            }
        });
        /* END Put sleep quality */

        /* Put busy-ness on right Y Axis */
        /* 4 = 10h sleep, 3 = 8h sleep, ...*/
        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setAxisMinimum(0f);
        yAxisRight.setAxisMaximum(120f);
        /* END Put busy-ness */

        /* Put weekdays on X Axis of Graph */
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setGranularity(1f);
        final String[] weekdays = utils.getWeekDays();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return weekdays[(int) value % weekdays.length];
            }
        });
        /* END Put weekdays */

        putStyleChart(chart);


        /* SECOND GRAPH */
        /* Set background color of the graph to be slightly transparent */
        ll = (LinearLayout) findViewById(R.id.ll_sleep_quality_busy);
        ll.setBackgroundColor(getResources().getColor(R.color.moon));
        ll.setAlpha(0.8f);
        /* END Set background */

        /* Create chart */
        chart = (LineChart) findViewById(R.id.sleep_quality_busy);
        /* END Create chart */
        
        /* Add to data set */
        lineData = new ArrayList<ILineDataSet>();
        /* END add data set */

        // get mood data from the db
        entries = feedbackOperations.getField(userID, utils.getDayId(this), DatabaseHelper.QUESTION_RESTED);
        entries = reverse(entries);
        entries = decrementRange(entries,1);

        dataSet = new LineDataSet(entries, "Restedness"); // add entries to dataset

        putStyleDataSet(dataSet, getResources().getColor(R.color.foregroundMountain));
        /* END Example mood */
        lineData.add(dataSet);

        /* START example quality */
        entries = feedbackOperations.getField(userID, utils.getDayId(this), DatabaseHelper.SLEEP_RATE);
        entries = reverse(entries);
        entries = decrementRange(entries,1);

        dataSet = new LineDataSet(entries, "Sleep quality"); // add entries to dataset

        putStyleDataSet(dataSet, getResources().getColor(R.color.colorPrimaryDark));
        /* END example quality */
        lineData.add(dataSet);


        /* Example busy-ness data */
        List alarmentries = feedbackOperations.getFieldString(userID, utils.getDayId(this), DatabaseHelper.MORNING_ALARM);
        List bedtimeentries = feedbackOperations.getFieldString(userID, utils.getDayId(this), DatabaseHelper.EVENING_ALARM);
        List<Entry> procentries = feedbackOperations.getField(userID, utils.getDayId(this), DatabaseHelper.PROCRASTINATION_DURATION);

        Log.d("alarm", alarmentries.toString());
        Log.d("bed", bedtimeentries.toString());
        Log.d("proc", procentries.toString());

        entries = getSleepDuration(alarmentries, bedtimeentries, procentries);
        entries = reverse(entries);
        entries = changeRange(entries, 10, 4);

        dataSet = new LineDataSet(entries, "Sleep duration"); // add entries to dataset

        putStyleDataSet(dataSet, getResources().getColor(R.color.alert));
        /* END Example sleep duration*/

        feedbackOperations.close();

        /* Add to data set and put in graph */
        lineData.add(dataSet);
        data = new LineData(lineData);
        chart.setData(data);

        d = new Description();
        d.setText("Sleep feedback last week");
        chart.setDescription(d);
        /* END add data set */

        /* Put sleep quality on left X Axis */
        yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setAxisMaximum(4f);
        yAxisLeft.setGranularity(1f);
        yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return sentiment[(int) value % sentiment.length];
            }
        });
        /* END Put sleep quality */


       /* Put busy-ness on right Y Axis */
        /* 4 = 10h sleep, 3 = 8h sleep, ...*/
        yAxisRight = chart.getAxisRight();
        yAxisRight.setAxisMinimum(0f);
        yAxisRight.setAxisMaximum(10f);
        /* END Put busy-ness */

        /* Put weekdays on X Axis of Graph */
        xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return weekdays[(int) value % weekdays.length];
            }
        });
        /* END Put weekdays */

        putStyleChart(chart);

        /* Set background color of the graph to be slightly transparent */
        ll = (LinearLayout) findViewById(R.id.ll_proc_reasons);
        ll.setBackgroundColor(getResources().getColor(R.color.moon));
        ll.setAlpha(0.8f);
        /* END Set background */


        List<String> ary = new ArrayList<String>();
        reasonData = new ResultOperations(this);

        reasonData.open();

        List<String> rL = reasonData.getAllReasons(userID);
        reasonData.close();

        for (int i = 0; i < rL.size(); i++) {
            ary.add(rL.get(i));
            System.err.println(rL.get(i));
        }
        HashSet<String> uniques = new HashSet<>(ary);
        ary = new ArrayList<String>(uniques);

        ListView lv = (ListView) findViewById(R.id.lv_reasons);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                R.layout.reason_item,
                ary);

        lv.setAdapter(arrayAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();

        showDailyAlert = checkForTodaysQuestionnaire();
        showWeeklyAlert = checkForThisWeeksSelfEfficacy();

        final ActionButton actionButton = (ActionButton) findViewById(R.id.alert);
        Log.d("btn", actionButton.toString());
        Log.d("daily bool", showDailyAlert==true?"true":"false");
        // to test buttons
        //showDailyAlert = true;
        //showWeeklyAlert = true;
        if (showDailyAlert) {
            actionButton.setVisibility(View.VISIBLE);
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionButton.setVisibility(View.INVISIBLE);
                    showDailyAlert = false;
                    openQuestionnaire();

                }
            });
        } else {
            actionButton.setVisibility(View.INVISIBLE);
        }

        final ActionButton weeklyAlertButton = (ActionButton) findViewById(R.id.alertWeekly);
        Log.d("weeklybtn", weeklyAlertButton.toString());
        Log.d("weekly bool", showWeeklyAlert==true?"true":"false");
        if (showWeeklyAlert) {
            weeklyAlertButton.setVisibility(View.VISIBLE);
            weeklyAlertButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    weeklyAlertButton.setVisibility(View.INVISIBLE);
                    showWeeklyAlert = false;
                    openSelfEfficacy();

                }
            });
        } else {
            weeklyAlertButton.setVisibility(View.INVISIBLE);
        }

    }

    private boolean checkForTodaysQuestionnaire() {
        boolean result;
        ResultOperations fbOp = new ResultOperations(this);
        fbOp.open();
        //  Initialize SharedPreferences
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        int userID = getPrefs.getInt("userID", 0);

        result = !fbOp.isFeedbackGivenToday(userID, getBaseContext());

        fbOp.close();

        //return true;
        return result;
    }

    private boolean checkForThisWeeksSelfEfficacy() {

        if (utils.getDayId(this) < 8)
            return false;

        boolean result;
        ResultOperations selfEfficacyOperations = new ResultOperations(this);
        selfEfficacyOperations.open();
        //  Initialize SharedPreferences

        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        int userID = getPrefs.getInt("userID", 0);

        result = !selfEfficacyOperations.isQuestionairreFilled(userID);
        selfEfficacyOperations.close();

        return result;
    }

    private List<Entry> getSleepDuration(List alarmentries, List bedtimeentries, List procentries) {
        List<Entry> result = new ArrayList<>();
        for (int i = 0; i < alarmentries.size(); i++) {
            if (alarmentries.get(i) != null && !(((String) alarmentries.get(i)).isEmpty())) {
                int hm = Integer.parseInt(((String) alarmentries.get(i)).substring(0, 2));
                int mm = Integer.parseInt(((String) alarmentries.get(i)).substring(3, 5));

                if (bedtimeentries.get(i) != null && !(((String) bedtimeentries.get(i)).isEmpty())) {
                    int hb = Integer.parseInt(((String) bedtimeentries.get(i)).substring(0, 2));
                    int mb = Integer.parseInt(((String) bedtimeentries.get(i)).substring(3, 5));
                    float proc = ((Entry) procentries.get(i)).getY();

                    Calendar bed = Calendar.getInstance();
                    bed.set(Calendar.HOUR_OF_DAY, hb);
                    bed.set(Calendar.MINUTE, mb);

                    if (hb < 5) {
                        bed.add(Calendar.DATE, 1);
                    }

                    Calendar alarm = Calendar.getInstance();
                    alarm.set(Calendar.HOUR_OF_DAY, hm);
                    alarm.set(Calendar.MINUTE, mm);
                    alarm.add(Calendar.DATE, 1);

                    long diff = TimeUnit.MILLISECONDS.toMinutes(
                            Math.abs(alarm.getTimeInMillis() - bed.getTimeInMillis()));

                    float d = TimeUnit.MINUTES.toHours(diff - (long) proc);
                    result.add(new Entry(i, d));
                } else{
                    result.add(new Entry(i,0));
                }
            } else {
                result.add(new Entry(i,0));
            }
        }
        return result;
    }

    private List decrementRange(List entries, float byHowMuch){
        List result = new ArrayList();
        for (int i = 0; i < entries.size(); i++) {
            float newY = ((((Entry) entries.get(i)).getY()) - byHowMuch );
            result.add(new Entry(i, newY));
        }
        return result;
    }

    private List changeRange(List entries, float rangeBefore, float rangeAfter) {
        List result = new ArrayList();
        for (int i = 0; i < entries.size(); i++) {
            float newY = ((((Entry) entries.get(i)).getY()) / rangeBefore) * rangeAfter;
            result.add(new Entry(i, newY));
        }
        return result;
    }

    private List<Entry> reverse(List<Entry> entries) {
        Stack tmp = new Stack();
        for (int i = 0; i < entries.size(); i++) {
            tmp.add(entries.get(i));
        }
        List<Entry> result = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            result.add(new Entry(i, ((Entry) tmp.pop()).getY()));
        }
        return result;
    }

    private void putStyleChart(LineChart chart) {
        chart.setDrawBorders(true);
        chart.setDrawGridBackground(true);
        chart.getXAxis().setTextColor(getResources().getColor(R.color.darkblue));
        chart.getAxisLeft().setTextColor(getResources().getColor(R.color.darkblue));
        chart.getAxisRight().setTextColor(getResources().getColor(R.color.darkblue));
        chart.getLegend().setTextColor(getResources().getColor(R.color.darkblue));
        chart.invalidate(); // refresh
    }

    private LineDataSet putStyleDataSet(LineDataSet dataSet, int color) {
        dataSet.setColor(color);
        dataSet.setLineWidth(2.5f);
        dataSet.setCircleColor(color);
        dataSet.setCircleRadius(5f);
        dataSet.setFillColor(color);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawValues(false);
        dataSet.setValueTextColor(getResources().getColor(R.color.darkblue));
        return dataSet;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public void openQuestionnaire() {
        Intent intent_question = new Intent(this, QuestionnaireActivity.class);
        intent_question.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent_question, REQUEST_CODE);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_progress) {

        } else if (id == R.id.nav_settings) {
            finish();
            Intent intent_settings = new Intent(this, SettingsDrawerActivity.class);
            intent_settings.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent_settings.putExtra("showWeeklyAlert", showWeeklyAlert);
            intent_settings.putExtra("showDailyAlert", showDailyAlert);
            intent_settings.putExtra("whichLanding", whichLanding);
            intent_settings.putExtra("whichIcon", whichIcon);
            startActivity(intent_settings);
        } else if (id == R.id.nav_alarm) {
            finish();
            Intent intent_alarm = new Intent(this, AlarmDrawerActivity.class);
            intent_alarm.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent_alarm.putExtra("showWeeklyAlert", showWeeklyAlert);
            intent_alarm.putExtra("showDailyAlert", showDailyAlert);
            intent_alarm.putExtra("whichLanding", whichLanding);
            intent_alarm.putExtra("whichIcon", whichIcon);
            startActivity(intent_alarm);
        } else if (id == R.id.nav_home) {
            finish();
            Intent intent_settings = new Intent(this, MainDrawerActivity.class);
            intent_settings.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent_settings.putExtra("showWeeklyAlert", showWeeklyAlert);
            intent_settings.putExtra("showDailyAlert", showDailyAlert);
            intent_settings.putExtra("whichLanding", whichLanding);
            intent_settings.putExtra("whichIcon", whichIcon);
            startActivity(intent_settings);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void openSelfEfficacy() {
        Intent intent_self_efficacy = new Intent(this, SelfEfficacyActivity.class);
        intent_self_efficacy.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent_self_efficacy, REQUEST_CODE);
    }
}
