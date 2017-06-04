package applab.bedtimeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import applab.bedtimeapp.db.DatabaseHelper;
import applab.bedtimeapp.db.ResultOperations;
import applab.bedtimeapp.model.Feedback;
import applab.bedtimeapp.model.Result;
import applab.bedtimeapp.utils.RestClient;
import applab.bedtimeapp.utils.utils;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class QuestionnaireActivity extends AppCompatActivity {

    public final static String EXTRA_REASON = "EXTRA_REASON";
    public final static String EXTRA_DURATION = "EXTRA_DURATION";
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
    private Result newFeedback;
    List<Feedback> feedbacks;

    private RatingBar ratingBarRested;
    private RatingBar ratingBarMood;
    private RatingBar ratingBarConcentration;
    private RatingBar ratingBarBusy;
    private EditText refusalReasonEditText;
    String extraReason = "";
    int extraDuration = 0;

    private boolean successfulSending;

    private ResultOperations feedbackData;

    private static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        feedbackData = new ResultOperations(this);

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
                feedbackData.open();
                newFeedback = new Result();
                newFeedback.setUpdateType('F');
                newFeedback.setFeedbackDate(utils.getCurrentTimeString("yyyy-MM-dd HH:mm"));
                newFeedback.setQuestionBusy(Integer.valueOf(((int) ratingBarBusy.getRating())));
                newFeedback.setQuestionConcentration(Integer.valueOf(((int) ratingBarConcentration.getRating())));
                newFeedback.setQuestionRested(Integer.valueOf(((int) ratingBarRested.getRating())));
                newFeedback.setQuestionMood(Integer.valueOf(((int) ratingBarMood.getRating())));
                newFeedback.setRefusalReason(extraReason);
                newFeedback.setProcrastinationDuration(extraDuration);

                SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

                int userID = getPrefs.getInt("userID", 0);
                int updateFeedbackDay = feedbackData.getFeedbackDayId(getBaseContext());

                feedbackData.updateResult(newFeedback, updateFeedbackDay);

                feedbackData.close();

                utils.showDB(getBaseContext());

                sendData();
//                if (!sendData()){
//                    displayError();
//                } else{
//                    goToMain(v);
//                }

            }
        });

    }

    private void goBack(boolean successfulSending){
        if (successfulSending){
            goToMain();
        } else {
            displayError();
        }
    }

    private void displayError() {
        TextView tv = (TextView) findViewById(R.id.errormessage);
        tv.setText("Please check your internet connection and try again.");
    }

    public void sendData(){
        successfulSending = false;
        Log.e("successfulSending", successfulSending?"true":"false");
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
                goBack(successfulSending);
            }

            Log.e("successfulSending", successfulSending?"true":"false");
            RestClient.post(null, "/test", entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.e("yes","yes");
                    successfulSending = true;
                    goBack(successfulSending);
                }

                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e("yes","1");
                    successfulSending = false;
                    goBack(successfulSending);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e("yes","2");
                    successfulSending = true;
                    goBack(successfulSending);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("no","3");
            successfulSending = false;
            goBack(successfulSending);
        }
//
//        Log.e("Final successfulSending", successfulSending?"true":"false");
//        return successfulSending;

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
            extraDuration = data.getIntExtra(EXTRA_DURATION,0);
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
     * @param
     */
    public void goToMain() {
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
