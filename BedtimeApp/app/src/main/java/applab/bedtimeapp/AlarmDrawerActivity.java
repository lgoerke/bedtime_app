package applab.bedtimeapp;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AlarmDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static String ALARM_TYPE = "ALARM_TYPE";
    public static int current_bedtime = 21;
    public static int current_bedtime_m = 0;
    public static float last_degree = 0;
    public static int current_alarm = 6;
    public static int current_alarm_m = 0;
    public static boolean first_time = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity_alarm);
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

        LinearLayout ac = (LinearLayout) findViewById(R.id.alarm_content);
        ProgressBar pb = (ProgressBar) ac.findViewById(R.id.progressBar);
        pb.setProgress(270);

    }

    public void changeSleepDuration(int prog){
        int hour = prog/30;
        int m = ((prog%30)*2);
        TextView tv = (TextView) findViewById(R.id.sleep_duration);
        tv.setText("You will sleep " + Integer.toString(hour) + " hours and " + Integer.toString(m) + " minutes");
    }

    public void showTimePicker(View v){
        DialogFragment PickerFragment = new TimePickerFragment();
        Bundle bdl = new Bundle(1);
        String message;
        if (v.getId() == R.id.clockBedtime){
            message = "bedtime";
        } else {
            message = "morning";
        }
        bdl.putString(ALARM_TYPE, message);
        PickerFragment.setArguments(bdl);
        PickerFragment.show(getFragmentManager(),"TimePicker");
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
            // Handle the action
            finish();
            Intent intent_progress = new Intent(this, ProgressDrawerActivity.class);
            intent_progress.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent_progress);
        } else if (id == R.id.nav_settings) {
            finish();
            Intent intent_settings = new Intent(this, SettingsDrawerActivity.class);
            intent_settings.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent_settings);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
