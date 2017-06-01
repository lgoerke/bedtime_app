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

        LineDataSet dataSet = new LineDataSet(entries, "Mood"); // add entries to dataset

        putStyleDataSet(dataSet, getResources().getColor(R.color.foregroundMountain));
        /* END Example mood */
        lineData.add(dataSet);

        /* START example concentraition */
        entries = feedbackOperations.getField(userID, utils.getDayId(this), DatabaseHelper.QUESTION_CONCENTRATION);
        entries = reverse(entries);
        dataSet = new LineDataSet(entries, "Concentration"); // add entries to dataset

        putStyleDataSet(dataSet, getResources().getColor(R.color.backgroundMountain));
        /* END example concentration */
        lineData.add(dataSet);


        /* Example duration data */
//        entries = new ArrayList<Entry>();
//
//        entries.add(new Entry(0, 3.5f));
//        entries.add(new Entry(1, 3.0f));
//        entries.add(new Entry(2, 2.5f));
//        entries.add(new Entry(3, 3.0f));
//        entries.add(new Entry(4, 2.5f));
//        entries.add(new Entry(5, 3.5f));
//        entries.add(new Entry(6, 3.0f));

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

        /* Example sleep quality data */
//        entries = new ArrayList<Entry>();
//
//        entries.add(new Entry(0, 1));
//        entries.add(new Entry(1, 0));
//        entries.add(new Entry(2, 2));
//        entries.add(new Entry(3, 1));
//        entries.add(new Entry(4, 3));
//        entries.add(new Entry(5, 4));
//        entries.add(new Entry(6, 2));

          /* Add to data set */
        lineData = new ArrayList<ILineDataSet>();
        /* END add data set */

        // get mood data from the db
        entries = feedbackOperations.getField(userID, utils.getDayId(this), DatabaseHelper.QUESTION_RESTED);
        entries = reverse(entries);
        // Collections.reverse(entries);

        dataSet = new LineDataSet(entries, "Restedness"); // add entries to dataset

        putStyleDataSet(dataSet, getResources().getColor(R.color.foregroundMountain));
        /* END Example mood */
        lineData.add(dataSet);

        /* START example quality */
        entries = feedbackOperations.getField(userID, utils.getDayId(this), DatabaseHelper.SLEEP_RATE);
        entries = reverse(entries);
        dataSet = new LineDataSet(entries, "Sleep quality"); // add entries to dataset

        putStyleDataSet(dataSet, getResources().getColor(R.color.colorPrimaryDark));
        /* END example quality */
        lineData.add(dataSet);


        /* Example busy-ness data */
//        entries = new ArrayList<Entry>();
//
//        entries.add(new Entry(0, (7.5f/10f)*4f));
//        entries.add(new Entry(1, (7f/10f)*4f));
//        entries.add(new Entry(2, (8f/10f)*4f));
//        entries.add(new Entry(3, (6f/10f)*4f));
//        entries.add(new Entry(4, (6f/10f)*4f));
//        entries.add(new Entry(5, (8.5f/10f)*4f));
//        entries.add(new Entry(6, (7f/10f)*4f));
//
//        Log.d("duration", entries.toString());
        //here

        List alarmentries = feedbackOperations.getFieldString(userID, utils.getDayId(this), DatabaseHelper.MORNING_ALARM);
        List bedtimeentries = feedbackOperations.getFieldString(userID, utils.getDayId(this), DatabaseHelper.EVENING_ALARM);
        List<Entry> procentries = feedbackOperations.getField(userID, utils.getDayId(this), DatabaseHelper.PROCRASTINATION_DURATION);

        Log.d("alarm", alarmentries.toString());
        Log.d("bed", bedtimeentries.toString());
        Log.d("proc", procentries.toString());

        // entries = reverse(entries);
        // bedtimeentries = reverse(bedtimeentries);
        //  procentries = reverse(procentries);
        entries = getSleepDuration(alarmentries, bedtimeentries, procentries);
        entries = reverse(entries);
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

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                R.layout.reason_item,
                ary);

        lv.setAdapter(arrayAdapter);

        /* Connect FAB to opening the pending questionnaire */
        final ActionButton actionButton = (ActionButton) findViewById(R.id.alert);
        Log.d("btn",actionButton.toString());
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
        /* END Connect FAB */

    }

    private List<Entry> getSleepDuration(List alarmentries, List bedtimeentries, List procentries) {
        List<Entry> result = new ArrayList<>();
        for (int i = 0; i < alarmentries.size(); i++) {
            Log.e("a",alarmentries.get(i).toString());
            Log.e("b",bedtimeentries.get(i).toString());
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
