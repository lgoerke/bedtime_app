package applab.bedtimeapp;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;

import applab.bedtimeapp.db.FeedbackOperations;
import applab.bedtimeapp.model.Feedback;

public class AddUpdateFeedback extends AppCompatActivity {

    private static final String EXTRA_FB_ID = "fbId";
    private static final String EXTRA_ADD_UPDATE = "add_update";
    private static final String DIALOG_DATE = "DialogDate";
    private ImageView calendarImage;
    private EditText userIdText;
    private EditText dateEditText;
    private EditText question1EditText;
    private EditText question2EditText;
    private EditText question3EditText;
    private EditText morningAlarmEditText;
    private EditText eveningAlarmEditText;
    private EditText numberOfSnoozesEditText;
    private EditText landingPageEditText;
    private EditText refusalReasonEditText;

    private Button addUpdateButton;

    private Feedback newFeedback;
    private Feedback oldFeedback;
    private String mode;
    private long fbId;
    private FeedbackOperations feedbackData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_feedback);
        newFeedback = new Feedback();
        oldFeedback = new Feedback();

        userIdText = (EditText)findViewById(R.id.edit_text_user_id);

        dateEditText = (EditText)findViewById(R.id.edit_text_date);
        calendarImage = (ImageView)findViewById(R.id.image_view_date);

        question1EditText = (EditText)findViewById(R.id.edit_text_question1);
        question2EditText = (EditText)findViewById(R.id.edit_text_question2);
        question3EditText = (EditText)findViewById(R.id.edit_text_question3);
        morningAlarmEditText = (EditText)findViewById(R.id.edit_text_morning_alarm);
        eveningAlarmEditText = (EditText)findViewById(R.id.edit_text_evening_alarm);
        numberOfSnoozesEditText = (EditText)findViewById(R.id.edit_text_number_of_snoozes);
        landingPageEditText = (EditText)findViewById(R.id.edit_text_landing_page);
        refusalReasonEditText = (EditText)findViewById(R.id.edit_text_refusal_reason);

        addUpdateButton = (Button)findViewById(R.id.button_add_update_feedback);
        
        feedbackData = new FeedbackOperations(this);
        feedbackData.open();


        mode = getIntent().getStringExtra(EXTRA_ADD_UPDATE);
        if(mode.equals("Update")){

            addUpdateButton.setText("Update Feedback");
            fbId = getIntent().getLongExtra(EXTRA_FB_ID,0);

            initializeFeedback(fbId);

        }

        calendarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //FragmentManager manager = getSupportFragmentManager();
                //DatePickerFragment dialog = new DatePickerFragment();
                //dialog.show(manager, DIALOG_DATE);
            }
        });


        addUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mode.equals("Add")) {
                    newFeedback.setUserId(Long.parseLong(userIdText.getText().toString()));
                    newFeedback.setDate(dateEditText.getText().toString());
                    newFeedback.setQuestionBusy(Integer.parseInt(question1EditText.getText().toString()));
                    newFeedback.setQuestionRested(Integer.parseInt(question2EditText.getText().toString()));
                    newFeedback.setQuestionConcentration(Integer.parseInt(question3EditText.getText().toString()));
                    newFeedback.setRefusalReason(refusalReasonEditText.getText().toString());

                    feedbackData.addFeedback(newFeedback);
                    Toast t = Toast.makeText(AddUpdateFeedback.this, "Feedback "+ newFeedback.toString() + "has been added successfully !", Toast.LENGTH_SHORT);
                    t.show();
                    Intent i = new Intent(AddUpdateFeedback.this, FeedbackActivity.class);
                    startActivity(i);
                }else {

                    oldFeedback.setUserId(Long.parseLong(userIdText.getText().toString()));
                    oldFeedback.setDate(dateEditText.getText().toString());
                    oldFeedback.setQuestionBusy(Integer.parseInt(question1EditText.getText().toString()));
                    oldFeedback.setQuestionRested(Integer.parseInt(question2EditText.getText().toString()));
                    oldFeedback.setQuestionConcentration(Integer.parseInt(question3EditText.getText().toString()));
                    oldFeedback.setRefusalReason(refusalReasonEditText.getText().toString());

                    feedbackData.updateFeedback(oldFeedback);
                    Toast t = Toast.makeText(AddUpdateFeedback.this, "Feedback "+ oldFeedback.toString() + " has been updated successfully !", Toast.LENGTH_SHORT);
                    t.show();
                    Intent i = new Intent(AddUpdateFeedback.this,FeedbackActivity.class);
                    startActivity(i);

                }


            }
        });


    }

    private void initializeFeedback(long fbId) {
        oldFeedback = feedbackData.getFeedback(fbId);

        userIdText.setText(String.valueOf(oldFeedback.getUserId()));
        dateEditText.setText(oldFeedback.getDate());
        question1EditText.setText(String.valueOf(oldFeedback.getQuestionBusy()));
        question2EditText.setText(String.valueOf(oldFeedback.getQuestionRested()));
        question3EditText.setText(String.valueOf(oldFeedback.getQuestionConcentration()));
        refusalReasonEditText.setText(oldFeedback.getRefusalReason());


    }


   /* @Override
    public void onFinishDialog(Date date) {
        dateEditText.setText(formatDate(date));

    }*/

    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String hireDate = sdf.format(date);
        return hireDate;
    }

}