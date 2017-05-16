package applab.bedtimeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.util.Calendar;
import java.util.List;

import applab.bedtimeapp.db.DatabaseHelper;
import applab.bedtimeapp.db.FeedbackOperations;
import applab.bedtimeapp.model.Feedback;

public class QuestionnaireActivity extends AppCompatActivity {

    public final static String EXTRA_REASON = "EXTRA_REASON";
    private boolean metBedtime = false;
    private DatabaseHelper database;
    private boolean rested = false;
    private boolean busy = false;
    private boolean time = false;
    private boolean mood = false;
    private boolean concentration = false;
    private boolean reasons = false;
    private int starsRested = 0;
    private int starsBusy = 0;
    private int starsMood = 0;
    private int starsConcentration = 0;
    private Button completeButton;
    private Feedback newFeedback;
    List<Feedback> feedbacks;

    private RatingBar ratingBarRested;
    private RatingBar ratingBarMood;
    private RatingBar ratingBarConcentration;
    private RatingBar ratingBarBusy;
    private EditText refusalReasonEditText;
    String extraReason = "";
    private FeedbackOperations feedbackData;





    private static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        feedbackData = new FeedbackOperations(this);
        feedbackData.open();

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

        mBar = (RatingBar) findViewById(R.id.ratingBarMood);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mood = true;
                starsMood = Math.round(v);
                Log.d("bar", Float.toString(Math.round(v)));
                checkComplete();
            }
        });

        mBar = (RatingBar) findViewById(R.id.ratingBarConcentration);
        mBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                concentration = true;
                starsConcentration = Math.round(v);
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

        ratingBarConcentration = (RatingBar) findViewById(R.id.ratingBarConcentration);
        ratingBarBusy = (RatingBar) findViewById(R.id.ratingBarBusy);
        ratingBarMood = (RatingBar) findViewById(R.id.ratingBarMood);
        ratingBarRested = (RatingBar) findViewById(R.id.ratingBarRested);
        refusalReasonEditText = (EditText) findViewById(R.id.editReason);


        completeButton = (Button) findViewById(R.id.complete);

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFeedback = new Feedback();

                // TODO user id creation
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                int userID = getPrefs.getInt("userID", 0);

                newFeedback.setUserId(userID);
                newFeedback.setDate(Calendar.getInstance().getTime().toString());
                newFeedback.setQuestionBusy(Integer.valueOf(((int) ratingBarBusy.getRating())));
                newFeedback.setQuestionConcentration(Integer.valueOf(((int) ratingBarConcentration.getRating())));
                newFeedback.setQuestionRested(Integer.valueOf(((int) ratingBarRested.getRating())));
                newFeedback.setQuestionMood(Integer.valueOf(((int) ratingBarMood.getRating())));
                newFeedback.setRefusalReason(extraReason);

                feedbackData.addFeedback(newFeedback);
//                Toast t = Toast.makeText(QuestionnaireActivity.this, "Your feedback has been added successfully !", Toast.LENGTH_LONG);
//                t.show();
//                Intent i = new Intent(QuestionnaireActivity.this, QuestionnaireActivity.class);
//                startActivity(i);

//                setContentView(R.layout.activity_view_all_feedbacks);

//                feedbacks = feedbackData.getAllFeedbacks();
                feedbackData.close();

//                // write all
//                for(int j=0; j<feedbacks.size();j++ ){
//                    System.err.println(feedbacks.get(j).toString());
//                }
                goToMain(v);

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
//                SQLiteDatabase db_write = database.getWritableDatabase();
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
            extraReason = data.getStringExtra(EXTRA_REASON);
            checkComplete();
        }
    }

    private void goToReasons(){
        Intent intent = new Intent(this, ReasonsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent,REQUEST_CODE);
    }

    private void checkComplete(){
        if (time && busy && rested && mood && concentration) {
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
        super.onStop();
    }


}
