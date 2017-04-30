package applab.bedtimeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import applab.bedtimeapp.db.DatabaseHelper;

public class SelfEfficacyActivity extends AppCompatActivity {

    private boolean metBedtime = false;
    private DatabaseHelper database;
    private boolean replied[] = new boolean[QUESTIONS];
    private int answers[] = new int[QUESTIONS];

    private static int REQUEST_CODE = 1;
    private static int QUESTIONS = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_efficacy);

        // get database
        database = new DatabaseHelper(SelfEfficacyActivity.this);

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
        Button btn = (Button) findViewById(R.id.complete);
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
        database.close();
        super.onStop();
    }


}
