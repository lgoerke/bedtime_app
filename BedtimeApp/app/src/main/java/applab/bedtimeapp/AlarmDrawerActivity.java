package applab.bedtimeapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.scalified.fab.ActionButton;

import java.util.Calendar;

import applab.bedtimeapp.db.DatabaseHelper;
import applab.bedtimeapp.db.ResultOperations;
import applab.bedtimeapp.utils.AlarmReceiver;
import applab.bedtimeapp.utils.utils;


public class AlarmDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private AlarmManager alarmManager;

    private static final String TAG = "AlarmDrawerActivity";

    private DatabaseHelper database;

    private static String ALARM_TYPE = "ALARM_TYPE";
    public static int current_bedtime = 21;
    public static int current_bedtime_m = 0;

    public static float last_degree = 0;

    public static int current_alarm = 6;
    public static int current_alarm_m = 0;

    public static boolean first_time = true;

    private static int REQUEST_CODE = 1;

    private boolean showDailyAlert = true;
    private boolean showWeeklyAlert = true;
    private int whichLanding = 0;
    // Sheep = 1, Cat = 2
    private int whichIcon = 1;

    private static int LANDING_ALARM = 1;
    private static int LANDING_PROGRESS = 2;
    private static int LANDING_HOME = 3;

    private PendingIntent alarmIntent;

    private static AlarmDrawerActivity inst;

    public static AlarmDrawerActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity_alarm);


        //get showAlert bool from other Activity
        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null) {
            for (String key : b.keySet()) {
                if (key.equals("showDailyAlert")) {
                    showDailyAlert = (boolean) b.get(key);
                } else if (key.equals("showWeeklyAlert")) {
                    showWeeklyAlert = (boolean) b.get(key);
                } else if (key.equals("whichLanding")) {
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
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        int userID = getPrefs.getInt("userID", 0);
        tv.setText((userID>0)?String.valueOf(userID):"");
        ImageView iv = (ImageView) hv.findViewById(R.id.avatarIcon);
        if (whichIcon == 1) {
            iv.setImageResource(R.drawable.sheep);
        } else if (whichIcon == 2) {
            iv.setImageResource(R.drawable.cat);
        }

        LinearLayout ac = (LinearLayout) findViewById(R.id.alarm_content);
        DonutProgress pb = (DonutProgress) ac.findViewById(R.id.progressBar);

        int savedMM = getPrefs.getInt("morningMinute", 0);
        int savedMH = getPrefs.getInt("morningHour", 6);
        int savedBM = getPrefs.getInt("bedtimeMinute", 0);
        int savedBH = getPrefs.getInt("bedtimeHour", 21);


        tv = (TextView) findViewById(R.id.clockBedtime);
        tv.setText(utils.getFullTime(savedBH, savedBM));

        tv = (TextView) findViewById(R.id.clockMorning);
        tv.setText(utils.getFullTime(savedMH, savedMM));



        Float starting_angle = utils.getStartingAngle(savedBH, savedBM);
        pb.setStartingDegree(Math.round(starting_angle));
        Float stop_progress = utils.getProgress(savedBH, savedBM, savedMH, savedMM);
        pb.setProgress(stop_progress);
        int prog = Math.round(pb.getProgress());
        changeSleepDuration(prog);


//        pb.setStartingDegree(180);
//        pb.setProgress(270);


        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (checkForSecondWeek()){
            Intent intent_settings = new Intent(this, CoachActivity.class);
            intent_settings.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent_settings.putExtra("showWeeklyAlert", showWeeklyAlert);
            intent_settings.putExtra("showDailyAlert", showDailyAlert);
            intent_settings.putExtra("whichLanding", whichLanding);
            intent_settings.putExtra("whichIcon", whichIcon);
            startActivity(intent_settings);
        }

    }

    public void setAlarm(Calendar calendar){
        alarmManager = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        Intent myIntent = new Intent(getActivity(), AlarmReceiver.class);
//
//        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }

    public void changeSleepDuration(int prog) {
        int hour = prog / 30;
        int m = ((prog % 30) * 2);
        TextView tv = (TextView) findViewById(R.id.sleep_duration);
        tv.setText("You will sleep " + Integer.toString(hour) + " hours and " + Integer.toString(m) + " minutes");
    }

    public void showTimePicker(View v) {
        DialogFragment PickerFragment = new TimePickerFragment();
        Bundle bdl = new Bundle(1);
        String message;
        if (v.getId() == R.id.clockBedtime) {
            message = "bedtime";
        } else {
            message = "morning";
        }
        bdl.putString(ALARM_TYPE, message);
        PickerFragment.setArguments(bdl);
        PickerFragment.show(getFragmentManager(), "TimePicker");
    }

    // handle extras from QuestionnaireActivity if wanted
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


    }

    public void openQuestionnaire() {
        Intent intent_question = new Intent(this, QuestionnaireActivity.class);
        intent_question.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent_question, REQUEST_CODE);
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
            intent_progress.putExtra("showWeeklyAlert", showWeeklyAlert);
            intent_progress.putExtra("showDailyAlert", showDailyAlert);
            intent_progress.putExtra("whichLanding", whichLanding);
            intent_progress.putExtra("whichIcon", whichIcon);
            startActivity(intent_progress);
        } else if (id == R.id.nav_settings) {
            finish();
            Intent intent_settings = new Intent(this, SettingsDrawerActivity.class);
            intent_settings.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent_settings.putExtra("showWeeklyAlert", showWeeklyAlert);
            intent_settings.putExtra("showDailyAlert", showDailyAlert);
            intent_settings.putExtra("whichLanding", whichLanding);
            intent_settings.putExtra("whichIcon", whichIcon);
            startActivity(intent_settings);

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

    @Override
    protected void onStop() {
        //database.close();
        super.onStop();
    }

    public boolean isShowWeeklyAlert() {
        return showWeeklyAlert;
    }

    public boolean isShowDailyAlert() {
        return showDailyAlert;
    }

    public int getWhichLanding() {
        return whichLanding;
    }

    public int getWhichIcon() {
        return whichIcon;
    }

    public void setMorningMinute(int pref){
        //  Initialize SharedPreferences
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        int savedMin = getPrefs.getInt("morningMinute", 0);

        //  Make a new preferences editor
        SharedPreferences.Editor e = getPrefs.edit();

        //  Edit preference to make it false because we don't want this to run again
        e.putInt("morningMinute", pref);

        //  Apply changes
        e.apply();
    }

    public void setMorningHour(int pref){
        //  Initialize SharedPreferences
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        int savedH = getPrefs.getInt("morningHour", 0);

        //  Make a new preferences editor
        SharedPreferences.Editor e = getPrefs.edit();

        //  Edit preference to make it false because we don't want this to run again
        e.putInt("morningHour", pref);

        //  Apply changes
        e.apply();
    }
    public void setBedtimeMinute(int pref){
        //  Initialize SharedPreferences
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        int savedMin = getPrefs.getInt("bedtimeMinute", 0);

        //  Make a new preferences editor
        SharedPreferences.Editor e = getPrefs.edit();

        //  Edit preference to make it false because we don't want this to run again
        e.putInt("bedtimeMinute", pref);

        //  Apply changes
        e.apply();
    }

    public void setBedtimeHour(int pref){
        //  Initialize SharedPreferences
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        int savedH = getPrefs.getInt("bedtimeHour", 0);

        //  Make a new preferences editor
        SharedPreferences.Editor e = getPrefs.edit();

        //  Edit preference to make it false because we don't want this to run again
        e.putInt("bedtimeHour", pref);

        //  Apply changes
        e.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        showDailyAlert = checkForTodaysQuestionnaire();
        showWeeklyAlert = checkForThisWeeksSelfEfficacy();

        final ActionButton actionButton = (ActionButton) findViewById(R.id.alert);
        Log.d("btn", actionButton.toString());
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

    private boolean checkForSecondWeek() {

        if (utils.getDayId(this) < 8)
            return false;
        else
            return true;
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

    public void openSelfEfficacy() {
        Intent intent_self_efficacy = new Intent(this, SelfEfficacyActivity.class);
        intent_self_efficacy.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent_self_efficacy, REQUEST_CODE);
    }


}
