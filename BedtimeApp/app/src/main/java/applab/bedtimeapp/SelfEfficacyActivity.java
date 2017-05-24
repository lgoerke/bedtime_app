package applab.bedtimeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import applab.bedtimeapp.db.SelfEfficacyOperations;
import applab.bedtimeapp.model.SelfEfficacy;
import applab.bedtimeapp.utils.utils;

public class SelfEfficacyActivity extends AppCompatActivity {

    private boolean metBedtime = false;
    private boolean replied[] = new boolean[QUESTIONS];
    private int answers[] = new int[QUESTIONS];

    private static int REQUEST_CODE = 1;
    private static int QUESTIONS = 10;

    private Button completeButton;
    private RatingBar   ratingBarQ1,
                        ratingBarQ2,
                        ratingBarQ3,
                        ratingBarQ4,
                        ratingBarQ5,
                        ratingBarQ6,
                        ratingBarQ7,
                        ratingBarQ8,
                        ratingBarQ9,
                        ratingBarQ10;

    private SelfEfficacyOperations selfEfficacyData;

    List<SelfEfficacy> selfEfficacies;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_efficacy);

        selfEfficacyData = new SelfEfficacyOperations(this);
        selfEfficacyData.open();

        RatingBar mBar = (RatingBar) findViewById(R.id.ratingBarQ1);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                replied[0] = true;
                answers[0] = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });

        mBar = (RatingBar) findViewById(R.id.ratingBarQ2);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                replied[1] = true;
                answers[1] = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });

        mBar = (RatingBar) findViewById(R.id.ratingBarQ3);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                replied[2] = true;
                answers[2] = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });

        mBar = (RatingBar) findViewById(R.id.ratingBarQ4);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                replied[3] = true;
                answers[3] = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });

        mBar = (RatingBar) findViewById(R.id.ratingBarQ5);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                replied[4] = true;
                answers[4] = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });

        mBar = (RatingBar) findViewById(R.id.ratingBarQ6);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                replied[5] = true;
                answers[5] = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });

        mBar = (RatingBar) findViewById(R.id.ratingBarQ7);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                replied[6] = true;
                answers[6] = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });

        mBar = (RatingBar) findViewById(R.id.ratingBarQ8);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                replied[7] = true;
                answers[7] = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });

        mBar = (RatingBar) findViewById(R.id.ratingBarQ9);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                replied[8] = true;
                answers[8] = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });

        mBar = (RatingBar) findViewById(R.id.ratingBarQ10);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                replied[9] = true;
                answers[9] = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });
        /*
        mBar = (RatingBar) findViewById(R.id.ratingBarQ11);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                replied[10] = true;
                answers[10] = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });

        mBar = (RatingBar) findViewById(R.id.ratingBarQ12);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                replied[11] = true;
                answers[11] = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });

        mBar = (RatingBar) findViewById(R.id.ratingBarQ13);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                replied[12] = true;
                answers[12] = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });

        mBar = (RatingBar) findViewById(R.id.ratingBarQ14);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                replied[13] = true;
                answers[13] = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });
        mBar = (RatingBar) findViewById(R.id.ratingBarQ15);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                replied[14] = true;
                answers[14] = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });

        mBar = (RatingBar) findViewById(R.id.ratingBarQ16);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                replied[15] = true;
                answers[15] = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });

        mBar = (RatingBar) findViewById(R.id.ratingBarQ17);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                replied[16] = true;
                answers[16] = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });

        mBar = (RatingBar) findViewById(R.id.ratingBarQ18);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                replied[17] = true;
                answers[17] = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });

        mBar = (RatingBar) findViewById(R.id.ratingBarQ19);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                replied[18] = true;
                answers[18] = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });

        mBar = (RatingBar) findViewById(R.id.ratingBarQ20);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                replied[19] = true;
                answers[19] = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });

        mBar = (RatingBar) findViewById(R.id.ratingBarQ21);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                replied[20] = true;
                answers[20] = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });*/


        completeButton = (Button) findViewById(R.id.completeSelfEfficacy);

        ratingBarQ1 = (RatingBar) findViewById(R.id.ratingBarQ1);
        ratingBarQ2 = (RatingBar) findViewById(R.id.ratingBarQ2);
        ratingBarQ3 = (RatingBar) findViewById(R.id.ratingBarQ3);
        ratingBarQ4 = (RatingBar) findViewById(R.id.ratingBarQ4);
        ratingBarQ5 = (RatingBar) findViewById(R.id.ratingBarQ5);
        ratingBarQ6 = (RatingBar) findViewById(R.id.ratingBarQ6);
        ratingBarQ7 = (RatingBar) findViewById(R.id.ratingBarQ7);
        ratingBarQ8 = (RatingBar) findViewById(R.id.ratingBarQ8);
        ratingBarQ9 = (RatingBar) findViewById(R.id.ratingBarQ9);
        ratingBarQ10 = (RatingBar) findViewById(R.id.ratingBarQ10);


        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelfEfficacy newSelfEfficacy = new SelfEfficacy();

                // TODO user id creation
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                int userID = getPrefs.getInt("userID", 0);
                newSelfEfficacy.setUserId(userID);
                newSelfEfficacy.setDate(utils.getCurrentTimeString("yyyy-MM-dd HH:mm"));
                newSelfEfficacy.setQ1(Integer.valueOf(((int) ratingBarQ1.getRating())));
                newSelfEfficacy.setQ2(Integer.valueOf(((int) ratingBarQ2.getRating())));
                newSelfEfficacy.setQ3(Integer.valueOf(((int) ratingBarQ3.getRating())));
                newSelfEfficacy.setQ4(Integer.valueOf(((int) ratingBarQ4.getRating())));
                newSelfEfficacy.setQ5(Integer.valueOf(((int) ratingBarQ5.getRating())));
                newSelfEfficacy.setQ6(Integer.valueOf(((int) ratingBarQ6.getRating())));
                newSelfEfficacy.setQ7(Integer.valueOf(((int) ratingBarQ7.getRating())));
                newSelfEfficacy.setQ8(Integer.valueOf(((int) ratingBarQ8.getRating())));
                newSelfEfficacy.setQ9(Integer.valueOf(((int) ratingBarQ9.getRating())));
                newSelfEfficacy.setQ10(Integer.valueOf(((int) ratingBarQ10.getRating())));

                selfEfficacyData.addSelfEfficacy(newSelfEfficacy);
                Toast t = Toast.makeText(SelfEfficacyActivity.this, "Your feedback has been added successfully !", Toast.LENGTH_LONG);
                t.show();
                Intent i = new Intent(SelfEfficacyActivity.this, SelfEfficacyActivity.class);
                startActivity(i);


                selfEfficacies = selfEfficacyData.getAllSelfEfficacies();
                selfEfficacyData.close();

                // write all
                for(int j=0; j<selfEfficacies.size();j++ ){
                    System.err.println(selfEfficacies.get(j).toString());
                }

            }
        });
    }




    /**
     * Get data from questionnaire intent
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        checkComplete();
    }

    private void goToReasons(){
        Intent intent = new Intent(this, ReasonsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent,REQUEST_CODE);
    }

    private void checkComplete(){
        for(int i=0;i<QUESTIONS;i++){
            if (!replied[i])
                return;
        }
        Button btn = (Button) findViewById(R.id.completeSelfEfficacy);
        btn.setEnabled(true);
    }

    /**
     * Finish this activity and return to main activity
     *
     * @param view
     */
    public void goToMain(View view) {
        // Create intent to deliver some kind of result data
        Intent output = new Intent();
        setResult(RESULT_OK, output);
        finish();
    }

    @Override
    protected void onStop() {
        //database.close();
        super.onStop();
    }


}
