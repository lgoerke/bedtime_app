package applab.bedtimeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.ToggleButton;

public class QuestionnaireActivity extends AppCompatActivity {

    private boolean metBedtime = false;
    private boolean rested = false;
    private boolean busy = false;
    private boolean time = false;
    private int starsRested = 0;
    private int starsBusy = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

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
                checkComplete();
                break;
            case R.id.btn_no:
                btn = (ToggleButton) findViewById(R.id.btn_no);
                btn.setChecked(true);
                btn = (ToggleButton) findViewById(R.id.btn_yes);
                btn.setChecked(false);
                metBedtime = false;
                time = true;
                checkComplete();
                break;
        }

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


}