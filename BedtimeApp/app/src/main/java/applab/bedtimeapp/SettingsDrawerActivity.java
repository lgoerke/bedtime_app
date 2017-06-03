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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scalified.fab.ActionButton;

public class SettingsDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "SettingsDrawerActivity";
    private static int REQUEST_CODE = 1;

    private boolean showDailyAlert = true;
    private boolean showWeeklyAlert = true;
    private int whichLanding = 0;
    // Sheep = 1, Cat = 2
    private int whichIcon = 1;

    private static int LANDING_ALARM = 1;
    private static int LANDING_PROGRESS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity_settings);

        //get showAlert bool from other Activity
        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null) {
            for (String key : b.keySet()) {
                if (key.equals("showDailyAlert")) {
                    showDailyAlert = (boolean) b.get(key);
                } else if (key.equals("showWeeklyAlert")) {
                    showWeeklyAlert = (boolean) b.get(key);
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
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        int userID = getPrefs.getInt("userID", 0);
        tv.setText((userID>0)?String.valueOf(userID):"");
        ImageView iv = (ImageView) hv.findViewById(R.id.avatarIcon);
        if (whichIcon == 1) {
            iv.setImageResource(R.drawable.sheep);
        } else if (whichIcon == 2 ) {
            iv.setImageResource(R.drawable.cat);
        }


        CheckBox cb;
        if (whichLanding == LANDING_ALARM){
            cb = (CheckBox) findViewById(R.id.checkBoxAlarm);
            cb.setChecked(true);
        } else if (whichLanding == LANDING_PROGRESS){
            cb = (CheckBox) findViewById(R.id.checkBoxProgress);
            cb.setChecked(true);
        }


        Spinner iconSpinner = (Spinner) findViewById(R.id.spinner);
        if (whichIcon == 2) {
            iconSpinner.setSelection(1);
        }
        iconSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedIcon = adapterView.getItemAtPosition(i).toString();
                //writing name and alias in drawer header
                NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
                LinearLayout hv = (LinearLayout) nv.getHeaderView(0);
                ImageView iv = (ImageView) hv.findViewById(R.id.avatarIcon);
                if (selectedIcon.equals("Sheep")) {
                    iv.setImageResource(R.drawable.sheep);
                    whichIcon = 1;
                } else if (selectedIcon.equals("Cat")) {
                    iv.setImageResource(R.drawable.cat);
                    whichIcon = 2;
                }
                setIconPreference(whichIcon);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.checkBoxProgress:
                if (checked) {
                    CheckBox cb = (CheckBox) findViewById(R.id.checkBoxAlarm);
                    cb.setChecked(false);
                    whichLanding = LANDING_PROGRESS;
                    setLandingPreference(LANDING_PROGRESS);
                    //  Initialize SharedPreferences
                    SharedPreferences getPrefs = PreferenceManager
                            .getDefaultSharedPreferences(getBaseContext());

                    //  Create a new boolean and preference and set it to true
                    int savedLanding = getPrefs.getInt("whichLanding", 0);
                } else {
                    CheckBox cb = (CheckBox) findViewById(R.id.checkBoxProgress);
                    cb.setChecked(true);
                }
                break;
            case R.id.checkBoxAlarm:
                if (checked) {
                    CheckBox cb = (CheckBox) findViewById(R.id.checkBoxProgress);
                    cb.setChecked(false);
                    whichLanding = LANDING_ALARM;
                    setLandingPreference(LANDING_ALARM);
                    //  Initialize SharedPreferences
                    SharedPreferences getPrefs = PreferenceManager
                            .getDefaultSharedPreferences(getBaseContext());

                    //  Create a new boolean and preference and set it to true
                    int savedLanding = getPrefs.getInt("whichLanding", 0);
                } else {
                    CheckBox cb = (CheckBox) findViewById(R.id.checkBoxAlarm);
                    cb.setChecked(true);
                }
                break;

        }
    }

    public void setLandingPreference(int pref){
        //  Initialize SharedPreferences
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        int savedLanding = getPrefs.getInt("whichLanding", 0);

        //  Make a new preferences editor
        SharedPreferences.Editor e = getPrefs.edit();

        //  Edit preference to make it false because we don't want this to run again
        e.putInt("whichLanding", pref);

        //  Apply changes
        e.apply();
    }

    public void setIconPreference(int pref){
        //  Initialize SharedPreferences
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        int savedIcon = getPrefs.getInt("whichIcon", 0);

        //  Make a new preferences editor
        SharedPreferences.Editor e = getPrefs.edit();

        //  Edit preference to make it false because we don't want this to run again
        e.putInt("whichIcon", pref);

        //  Apply changes
        e.apply();
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
