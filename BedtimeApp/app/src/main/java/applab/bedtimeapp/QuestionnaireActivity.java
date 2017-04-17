package applab.bedtimeapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.ToggleButton;

import applab.bedtimeapp.db.DatabaseHelper;

public class QuestionnaireActivity extends AppCompatActivity {

    private boolean metBedtime = false;
    private DatabaseHelper database;
    private boolean rested = false;
    private boolean busy = false;
    private boolean time = false;
    private boolean reasons = false;
    private int starsRested = 0;
    private int starsBusy = 0;

    private static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        // get database
        database = new DatabaseHelper(QuestionnaireActivity.this);

        RatingBar mBar = (RatingBar) findViewById(R.id.ratingBarRested);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rested = true;
                starsRested = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });


        mBar = (RatingBar) findViewById(R.id.ratingBarBusy);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                busy = true;
                starsBusy = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });

    }


    public void onBtnClicked(View view) {
        // Check which btn was clicked
        ToggleButton btn;
        switch (view.getId()) {
            case R.id.btn_yes:
                btn = (ToggleButton) findViewById(R.id.btn_yes);
                btn.setChecked(true);
                btn = (ToggleButton) findViewById(R.id.btn_no);
                btn.setChecked(false);
                metBedtime = true;
                time = true;
                reasons = true;
                SQLiteDatabase db_write = database.getWritableDatabase();
                // do stuff to write question3 information in database

                checkComplete();
                break;
            case R.id.btn_no:
                btn = (ToggleButton) findViewById(R.id.btn_no);
                btn.setChecked(true);
                btn = (ToggleButton) findViewById(R.id.btn_yes);
                btn.setChecked(false);
                metBedtime = false;
                time = true;
                reasons = false;
                checkComplete();
                goToReasons();
                break;
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
            reasons = true;
            checkComplete();
        }
    }

    private void goToReasons(){
        Intent intent = new Intent(this, ReasonsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent,REQUEST_CODE);
    }

    private void checkComplete(){
        if (time && busy && rested) {
            Button btn = (Button) findViewById(R.id.complete);
            btn.setEnabled(true);
        }
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
