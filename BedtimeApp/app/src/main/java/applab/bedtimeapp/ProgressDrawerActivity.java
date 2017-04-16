package applab.bedtimeapp;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
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
import android.widget.LinearLayout;
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
import java.util.List;

public class ProgressDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ProgressDrawerActivity";

    private boolean showAlert = true;
    private int whichLanding = 0;

    private static int LANDING_ALARM = 1;
    private static int LANDING_PROGRESS = 2;

    private static int REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity_progress);

        //get showAlert bool from other Activity
        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null) {
            for (String key : b.keySet()) {
                if (key.equals("showAlert")) {
                    showAlert = (boolean) b.get(key);
                } else if (key.equals("whichLanding")){
                    whichLanding = (int) b.get(key);
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

        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_sleep_quality);
        ll.setBackgroundColor(getResources().getColor(R.color.moon));
        ll.setAlpha(0.8f);

        // in this example, a LineChart is initialized from xml
        LineChart chart = (LineChart) findViewById(R.id.sleep_quality);

        List<Entry> entries = new ArrayList<Entry>();

        entries.add(new Entry(0, 1));
        entries.add(new Entry(1, 0));
        entries.add(new Entry(2, 2));
        entries.add(new Entry(3, 1));
        entries.add(new Entry(4, 3));
        entries.add(new Entry(5, 4));
        entries.add(new Entry(6, 2));

        LineDataSet dataSet = new LineDataSet(entries, "Goodness of sleep"); // add entries to dataset

        putStyle(dataSet,getResources().getColor(R.color.gold));

        // use the interface ILineDataSet
        List<ILineDataSet> lineData = new ArrayList<ILineDataSet>();
        lineData.add(dataSet);

        entries = new ArrayList<Entry>();

        entries.add(new Entry(0, 0));
        entries.add(new Entry(1, 2));
        entries.add(new Entry(2, 1));
        entries.add(new Entry(3, 0));
        entries.add(new Entry(4, 1));
        entries.add(new Entry(5, 2));
        entries.add(new Entry(6, 2));


        dataSet = new LineDataSet(entries, "Busy-ness of days"); // add entries to dataset

        putStyle(dataSet,getResources().getColor(R.color.alert));

        lineData.add(dataSet);

        LineData data = new LineData(lineData);
        chart.setData(data);

        Description d = new Description();
        d.setText("Sleep feedback last week");
        chart.setDescription(d);

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

        final String[] busyNess = new String[]{
                "Very busy", "Busy", "Normal", "Relaxed", "Very relaxed"
        };

        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setAxisMinimum(0f);
        yAxisRight.setAxisMaximum(4f);
        yAxisRight.setGranularity(1f);
        yAxisRight.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return busyNess[(int) value % busyNess.length];
            }
        });


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

        chart.setDrawBorders(true);
//        Paint p = new Paint(0);
//        p.setColor(getResources().getColor(R.color.moon));
//        p.setAlpha(150);
//        chart.setPaint(p, Chart.PAINT_GRID_BACKGROUND);
        chart.setDrawGridBackground(true);

        chart.getXAxis().setTextColor(getResources().getColor(R.color.darkblue));
        chart.getAxisLeft().setTextColor(getResources().getColor(R.color.darkblue));
        chart.getAxisRight().setTextColor(getResources().getColor(R.color.darkblue));
        chart.getLegend().setTextColor(getResources().getColor(R.color.darkblue));
        chart.invalidate(); // refresh

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

    }

    private void putStyle(LineDataSet dataSet, int color){
        dataSet.setColor(color);
        dataSet.setLineWidth(2.5f);
        dataSet.setCircleColor(color);
        dataSet.setCircleRadius(5f);
        dataSet.setFillColor(color);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawValues(false);
        dataSet.setValueTextColor(getResources().getColor(R.color.darkblue));
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
            startActivity(intent_settings);
        } else if (id == R.id.nav_alarm) {
            finish();
            Intent intent_alarm = new Intent(this, AlarmDrawerActivity.class);
            intent_alarm.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent_alarm.putExtra("showAlert", showAlert);
            intent_alarm.putExtra("whichLanding", whichLanding);
            startActivity(intent_alarm);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
