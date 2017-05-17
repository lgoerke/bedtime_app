package applab.bedtimeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.Chart;
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
import java.util.HashSet;
import java.util.List;

import applab.bedtimeapp.db.FeedbackOperations;
import applab.bedtimeapp.db.ReasonOperations;
import applab.bedtimeapp.model.Reason;

public class ProgressDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ProgressDrawerActivity";

    private boolean showAlert = true;
    private int whichLanding = 0;
    // Sheep = 1, Cat = 2
    private int whichIcon = 1;

    private static int LANDING_ALARM = 1;
    private static int LANDING_PROGRESS = 2;

    private static int REQUEST_CODE = 1;
    private ReasonOperations reasonData;


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
                if (key.equals("showAlert")) {
                    showAlert = (boolean) b.get(key);
                } else if (key.equals("whichLanding")){
                    whichLanding = (int) b.get(key);
                } else if (key.equals("whichIcon")){
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
        tv.setText("Yoo Lisa");
        ImageView iv = (ImageView) hv.findViewById(R.id.avatarIcon);
        if (whichIcon == 1) {
            iv.setImageResource(R.drawable.sheep);
        } else if (whichIcon == 2 ) {
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

        /* Example sleep quality data */
        List<Entry> entries = new ArrayList<Entry>();

        entries.add(new Entry(0, 1));
        entries.add(new Entry(1, 0));
        entries.add(new Entry(2, 2));
        entries.add(new Entry(3, 1));
        entries.add(new Entry(4, 3));
        entries.add(new Entry(5, 4));
        entries.add(new Entry(6, 2));

        LineDataSet dataSet = new LineDataSet(entries, "Goodness of sleep"); // add entries to dataset

        dataSet = putStyleDataSet(dataSet,getResources().getColor(R.color.gold));
        /* END Example sleep quality */

        /* Add to data set */
        List<ILineDataSet> lineData = new ArrayList<ILineDataSet>();
        lineData.add(dataSet);
        /* END add data set */

        /* Example duration data */
        entries = new ArrayList<Entry>();

        entries.add(new Entry(0, 3.5f));
        entries.add(new Entry(1, 3.0f));
        entries.add(new Entry(2, 2.5f));
        entries.add(new Entry(3, 3.0f));
        entries.add(new Entry(4, 2.5f));
        entries.add(new Entry(5, 3.5f));
        entries.add(new Entry(6, 3.0f));

        dataSet = new LineDataSet(entries, "Sleep duration"); // add entries to dataset

        dataSet = putStyleDataSet(dataSet,getResources().getColor(R.color.alert));
        /* END Example duration */

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
        yAxisRight.setAxisMaximum(9f);
        /* END Put busy-ness */

        /* Put weekdays on X Axis of Graph */
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setGranularity(1f);
        final String[] weekdays = new String[]{
                "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
        };
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
        entries = new ArrayList<Entry>();

        entries.add(new Entry(0, 1));
        entries.add(new Entry(1, 0));
        entries.add(new Entry(2, 2));
        entries.add(new Entry(3, 1));
        entries.add(new Entry(4, 3));
        entries.add(new Entry(5, 4));
        entries.add(new Entry(6, 2));

        dataSet = new LineDataSet(entries, "Goodness of sleep"); // add entries to dataset

        putStyleDataSet(dataSet,getResources().getColor(R.color.gold));
        /* END Example sleep quality */

        /* Add to data set */
        lineData = new ArrayList<ILineDataSet>();
        lineData.add(dataSet);
        /* END add data set */

        /* Example busy-ness data */
       /* entries = new ArrayList<Entry>();

        entries.add(new Entry(0, 0));
        entries.add(new Entry(1, 2));
        entries.add(new Entry(2, 1));
        entries.add(new Entry(3, 0));
        entries.add(new Entry(4, 1));
        entries.add(new Entry(5, 2));
        entries.add(new Entry(6, 2));*/

        // get busyness data from the db
        FeedbackOperations feedbackOperations = new FeedbackOperations(this);
        feedbackOperations.open();
        entries = feedbackOperations.getBusyness(userID);
        feedbackOperations.close();

        dataSet = new LineDataSet(entries, "Busy-ness of days"); // add entries to dataset

        putStyleDataSet(dataSet,getResources().getColor(R.color.alert));
        /* END Example busy-ness */

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
        final String[] busyNess = new String[]{
                "Very busy", "Busy", "Normal", "Relaxed", "Very relaxed"
        };

        yAxisRight = chart.getAxisRight();
        yAxisRight.setAxisMinimum(0f);
        yAxisRight.setAxisMaximum(4f);
        yAxisRight.setGranularity(1f);
        yAxisRight.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return busyNess[(int) value % busyNess.length];
            }
        });
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
        reasonData = new ReasonOperations(this);

        reasonData.open();
        //TODO add user id

        List<Reason> rL = reasonData.getAllReasons(userID);
        reasonData.close();

        for (int i = 0; i < rL.size(); i++) {
            ary.add(rL.get(i).getReason());
            System.err.println(rL.get(i).getReason());
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
                ary );

        lv.setAdapter(arrayAdapter);

        /* Connect FAB to opening the pending questionnaire */
        final ActionButton actionButton = (ActionButton) findViewById(R.id.alert);
        Log.d("btn",actionButton.toString());
        if (showAlert) {
            actionButton.setVisibility(View.VISIBLE);
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionButton.setVisibility(View.INVISIBLE);
                    showAlert = false;
                    openQuestionnaire();

                }
            });
        } else {
            actionButton.setVisibility(View.INVISIBLE);
        }
        /* END Connect FAB */

    }

    private void putStyleChart(LineChart chart){
        chart.setDrawBorders(true);
        chart.setDrawGridBackground(true);
        chart.getXAxis().setTextColor(getResources().getColor(R.color.darkblue));
        chart.getAxisLeft().setTextColor(getResources().getColor(R.color.darkblue));
        chart.getAxisRight().setTextColor(getResources().getColor(R.color.darkblue));
        chart.getLegend().setTextColor(getResources().getColor(R.color.darkblue));
        chart.invalidate(); // refresh
    }

    private LineDataSet putStyleDataSet(LineDataSet dataSet, int color){
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



    public void openQuestionnaire(){
        Intent intent_question = new Intent(this, QuestionnaireActivity.class);
        intent_question.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent_question,REQUEST_CODE);
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
            intent_settings.putExtra("showAlert", showAlert);
            intent_settings.putExtra("whichLanding", whichLanding);
            intent_settings.putExtra("whichIcon", whichIcon);
            startActivity(intent_settings);
        } else if (id == R.id.nav_alarm) {
            finish();
            Intent intent_alarm = new Intent(this, AlarmDrawerActivity.class);
            intent_alarm.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent_alarm.putExtra("showAlert", showAlert);
            intent_alarm.putExtra("whichLanding", whichLanding);
            intent_alarm.putExtra("whichIcon", whichIcon);
            startActivity(intent_alarm);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
