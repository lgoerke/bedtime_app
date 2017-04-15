package applab.bedtimeapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import com.scalified.fab.ActionButton;

public class MainDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainDrawerActivity";
    private boolean showAlert = true;
    private int whichLanding = 0;

    private static int LANDING_ALARM = 1;
    private static int LANDING_PROGRESS = 2;
    private static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


        //get showAlert bool from other Activity
        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null) {
            for (String key : b.keySet()) {
                if (key.equals("showAlert")) {
                    showAlert = (boolean) b.get(key);
                }else if (key.equals("whichLanding")){
                    whichLanding = (int) b.get(key);
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
        Log.d("yo",Integer.toString(savedLanding));

        whichLanding = savedLanding;

        if (whichLanding == LANDING_ALARM) {
            finish();
            Intent intent_alarm = new Intent(this, AlarmDrawerActivity.class);
            intent_alarm.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent_alarm.putExtra("showAlert", showAlert);
            intent_alarm.putExtra("whichLanding", whichLanding);
            startActivity(intent_alarm);
        } else if (whichLanding == LANDING_PROGRESS) {
            finish();
            Intent intent_progress = new Intent(this, ProgressDrawerActivity.class);
            intent_progress.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent_progress.putExtra("showAlert", showAlert);
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

    /**
     * Get data from questionnaire intent
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

    public void openQuestionnaire(){
        Intent intent_question = new Intent(this, QuestionnaireActivity.class);
        intent_question.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent_question,REQUEST_CODE);
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
            intent_progress.putExtra("showAlert", showAlert);
            intent_progress.putExtra("whichLanding", whichLanding);
            startActivity(intent_progress);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notification_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_5:
                scheduleNotification(getNotification("5 second delay"), 5000);
                return true;
            case R.id.action_10:
                scheduleNotification(getNotification("10 second delay"), 10000);
                return true;
            case R.id.action_30:
                scheduleNotification(getNotification("30 second delay"), 30000);
                return true;
            default:
                //return true;
                return super.onOptionsItemSelected(item);
        }
    }
    private void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.icon);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        builder.setSound(uri);
        return builder.build();
    }
}
