package applab.bedtimeapp;

import android.app.AlarmManager;
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
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.scalified.fab.ActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import applab.bedtimeapp.db.DatabaseHelper;
import applab.bedtimeapp.db.ResultOperations;
import applab.bedtimeapp.utils.AlarmReceiver;
import applab.bedtimeapp.utils.Constants;
import applab.bedtimeapp.utils.NotificationHelper;
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
    private static int LANDING_HOME = 3;
    private static int REQUEST_CODE = 1;

    private boolean successfulSending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // notify at every 18:00
        dailyEveningNotifications();
        utils.showDB(getBaseContext());


        setContentView(R.layout.drawer_activity_main);

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

        showDailyAlert = checkForTodaysQuestionnaire();
        showWeeklyAlert = checkForThisWeeksSelfEfficacy();

        //  Initialize SharedPreferences
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        int savedLanding = getPrefs.getInt("whichLanding", 0);
        int savedIcon = getPrefs.getInt("whichIcon", 1);


        whichLanding = savedLanding;
        whichIcon = savedIcon;



        int userID = getPrefs.getInt("userID", 0);


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
        tv.setText((userID>0)?String.valueOf(userID):"");

        ImageView iv = (ImageView) hv.findViewById(R.id.avatarIcon);
        if (whichIcon == 1) {
            iv.setImageResource(R.drawable.sheep);
        } else if (whichIcon == 2) {
            iv.setImageResource(R.drawable.cat);
        }


    }

    public boolean sendData(){
        DatabaseHelper database = new DatabaseHelper(this);
        JSONObject ary = null;
        try {
            ary = database.getResults(this);
            String str = ary.toString();
            String str_komma = str + ",";

            StringEntity entity = null;
            try {
                entity = new StringEntity(str_komma);
                entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            } catch (Exception e) {
                //Exception
                Log.e("no","4");
                successfulSending = false;
            }

            RestClient.post(null, "/test", entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.e("yes","yes");
                    successfulSending = true;
                }

                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e("yes","1");
                    successfulSending = false;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e("yes","2");
                    successfulSending = true;
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("no","3");
            successfulSending = false;
        }

        return successfulSending;

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
        } else if (id == R.id.nav_home) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void dailyEveningNotifications() {
        int delayForNotification = utils.getDelay(Constants.EVENING_NOTIFICATION_HOUR, Constants.EVENING_NOTIFICATION_MINUTE);
        Log.d("Delay: ", String.valueOf(delayForNotification));
        NotificationHelper.scheduleNotification(this, NotificationHelper.getNotification(this, "Please set your bed time and alarm time", AlarmDrawerActivity.class), delayForNotification, 24);
    }
}
