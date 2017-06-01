package applab.bedtimeapp;

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
import android.widget.Toast;

import com.loopj.android.http.*;
import com.scalified.fab.ActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import applab.bedtimeapp.db.DatabaseHelper;
import applab.bedtimeapp.db.ResultOperations;
import applab.bedtimeapp.model.Result;
import applab.bedtimeapp.model.SelfEfficacy;
import applab.bedtimeapp.utils.RestClient;
import applab.bedtimeapp.utils.utils;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;


public class MainDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainDrawerActivity";
    private boolean showDailyAlert = true;
    private boolean showWeeklyAlert = true;
    private int whichLanding = 0;
    // Sheep = 1, Cat = 2
    private int whichIcon = 1;

    private static int LANDING_ALARM = 1;
    private static int LANDING_PROGRESS = 2;
    private static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drawer_activity_main);
        showDailyAlert = checkForTodaysQuestionnaire();
        showWeeklyAlert = checkForThisWeeksSelfEfficacy();
        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    Intent i = new Intent(MainDrawerActivity.this, TutorialIntro.class);
                    startActivity(i);

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();


        //get showDailyAlert bool from other Activity
        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null) {
            for (String key : b.keySet()) {
                if (key.equals("showDailyAlert")) {
                    showDailyAlert = (boolean) b.get(key);
                } else if (key.equals("showWeeklyAlert")) {
                    showWeeklyAlert = (boolean) b.get(key);
                }else if (key.equals("whichLanding")) {
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

        //  Initialize SharedPreferences
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        int savedLanding = getPrefs.getInt("whichLanding", 0);
        int savedIcon = getPrefs.getInt("whichIcon", 1);


        whichLanding = savedLanding;
        whichIcon = savedIcon;


        ResultOperations feedbackData = new ResultOperations(this);
        feedbackData.open();
        int userID = getPrefs.getInt("userID", 0);


        feedbackData.open();
        List<Result> rL = feedbackData.getAllResults(userID);
        for(int i = 0; i< rL.size(); i++){
            System.err.println(rL.get(i).toString());
        }
        feedbackData.close();

        if (whichLanding == LANDING_ALARM) {
            finish();
            Intent intent_alarm = new Intent(this, AlarmDrawerActivity.class);
            intent_alarm.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent_alarm.putExtra("showDailyAlert", showDailyAlert);
            intent_alarm.putExtra("showWeeklyAlert", showWeeklyAlert);
            intent_alarm.putExtra("whichIcon", whichIcon);
            intent_alarm.putExtra("whichLanding", whichLanding);
            startActivity(intent_alarm);
        } else if (whichLanding == LANDING_PROGRESS) {
            finish();
            Intent intent_progress = new Intent(this, ProgressDrawerActivity.class);
            intent_progress.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent_progress.putExtra("showDailyAlert", showDailyAlert);
            intent_progress.putExtra("showWeeklyAlert", showWeeklyAlert);
            intent_progress.putExtra("whichIcon", whichIcon);
            intent_progress.putExtra("whichLanding", whichLanding);
            startActivity(intent_progress);
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
        } else if (whichIcon == 2) {
            iv.setImageResource(R.drawable.cat);
        }

        final ActionButton actionButton = (ActionButton) findViewById(R.id.alert);
        Log.d("btn", actionButton.toString());
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

        result = !fbOp.isFeedbackGivenToday(userID) ;

        fbOp.close();

        return result;
    }

    private boolean checkForThisWeeksSelfEfficacy() {

        if(utils.getDayId(this) < 8)
            return false;

        boolean result;
        ResultOperations selfEfficacyOperations = new ResultOperations(this);
        selfEfficacyOperations.open();
        //  Initialize SharedPreferences

        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        int userID = getPrefs.getInt("userID", 0);

        result = !selfEfficacyOperations.isQuestionairreFilled(userID) ;
        selfEfficacyOperations.close();

        return result;
    }

    /**
     * Get data from questionnaire intent
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            //mViewMode = data.getIntExtra(VIEW_MODE_STR, VIEW_MODE_CLEAR);
        }
    }

    public void openQuestionnaire() {
        Intent intent_question = new Intent(this, QuestionnaireActivity.class);
        intent_question.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent_question, REQUEST_CODE);
    }

    public void openSelfEfficacy() {
        Intent intent_self_efficacy = new Intent(this, SelfEfficacyActivity.class);
        intent_self_efficacy.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent_self_efficacy, REQUEST_CODE);
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
            intent_progress.putExtra("showDailyAlert", showDailyAlert);
            intent_progress.putExtra("showWeeklyAlert", showWeeklyAlert);
            intent_progress.putExtra("whichLanding", whichLanding);
            startActivity(intent_progress);
        } else if (id == R.id.nav_settings) {
            finish();
            Intent intent_settings = new Intent(this, SettingsDrawerActivity.class);
            intent_settings.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent_settings.putExtra("showDailyAlert", showDailyAlert);
            intent_settings.putExtra("showWeeklyAlert", showWeeklyAlert);
            intent_settings.putExtra("whichLanding", whichLanding);
            startActivity(intent_settings);

        } else if (id == R.id.nav_alarm) {
            finish();
            Intent intent_alarm = new Intent(this, AlarmDrawerActivity.class);
            intent_alarm.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent_alarm.putExtra("showDailyAlert", showDailyAlert);
            intent_alarm.putExtra("showWeeklyAlert", showWeeklyAlert);
            intent_alarm.putExtra("whichLanding", whichLanding);
            startActivity(intent_alarm);
        } else if (id == R.id.nav_coach) {
            finish();
            Intent intent_alarm = new Intent(this, SelfEfficacyActivity.class);
            intent_alarm.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent_alarm.putExtra("showDailyAlert", showDailyAlert);
            intent_alarm.putExtra("showWeeklyAlert", showWeeklyAlert);
            intent_alarm.putExtra("whichLanding", whichLanding);
            startActivity(intent_alarm);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.notification_menu, menu);
//        return true;
//    }
}
