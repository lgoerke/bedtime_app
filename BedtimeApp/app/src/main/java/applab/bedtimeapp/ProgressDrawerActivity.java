package applab.bedtimeapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class ProgressDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity_progress);
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

        // in this example, a LineChart is initialized from xml
        LineChart chart = (LineChart) findViewById(R.id.chart);

        List<Entry> entries = new ArrayList<Entry>();

        entries.add(new Entry(1, 1));
        entries.add(new Entry(2, 3));
        entries.add(new Entry(3, 2));
        entries.add(new Entry(4, 4));
        entries.add(new Entry(5, 5));
        entries.add(new Entry(6, 3));
        entries.add(new Entry(7, 2));

        LineDataSet dataSet = new LineDataSet(entries, "Your data"); // add entries to dataset

        dataSet.setColor(Color.rgb(240, 238, 70));
        dataSet.setLineWidth(2.5f);
        dataSet.setCircleColor(Color.rgb(240, 238, 70));
        dataSet.setCircleRadius(5f);
        dataSet.setFillColor(Color.rgb(240, 238, 70));
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawValues(true);
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(Color.DKGRAY);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        Description d = new Description();
        d.setText("Sleep feedback last week");
        chart.setDescription(d);

        final String[] sentiment = new String[]{
                "Very bad", "Bad", "A bit off", "Quite OK", "Good", "Very good"
        };

        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setAxisMaximum(10f);
        yAxisLeft.setGranularity(1f);
        yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return sentiment[(int) value % sentiment.length];
            }
        });

        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setAxisMinimum(0f);
        yAxisRight.setAxisMaximum(10f);
        yAxisRight.setGranularity(1f);

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

        chart.invalidate(); // refresh

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
            startActivity(intent_settings);
        } else if (id == R.id.nav_alarm) {
            finish();
            Intent intent_alarm = new Intent(this, AlarmDrawerActivity.class);
            intent_alarm.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent_alarm);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
