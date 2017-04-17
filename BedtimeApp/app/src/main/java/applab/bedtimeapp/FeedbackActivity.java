package  applab.bedtimeapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import applab.bedtimeapp.db.FeedbackOperations;
import applab.bedtimeapp.model.Feedback;


public class FeedbackActivity extends AppCompatActivity{

    private Button addFeedbackButton;
    private Button editFeedbackButton;
    private Button deleteFeedbackButton;
    private Button viewAllFeedbackButton;
    private FeedbackOperations feedbackOps;
    private static final String EXTRA_FB_ID = "fbId";
    private static final String EXTRA_ADD_UPDATE = "add_update";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);
        addFeedbackButton = (Button) findViewById(R.id.button_add_feedback);
        editFeedbackButton = (Button) findViewById(R.id.button_edit_feedback);
        deleteFeedbackButton = (Button) findViewById(R.id.button_delete_feedback);
        viewAllFeedbackButton = (Button)findViewById(R.id.button_view_feedbacks);



        addFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FeedbackActivity.this,AddUpdateFeedback.class);
                i.putExtra(EXTRA_ADD_UPDATE, "Add");
                startActivity(i);
            }
        });
        editFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFbIdAndUpdateFb();
            }
        });
        deleteFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFbIdAndRemoveFb();
            }
        });
        viewAllFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FeedbackActivity.this, ViewAllFeedbacks.class);
                startActivity(i);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feedback_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void getFbIdAndUpdateFb(){

        LayoutInflater li = LayoutInflater.from(this);
        View getEmpIdView = li.inflate(R.layout.dialog_get_fb_id, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(getEmpIdView);

        final EditText userInput = (EditText) getEmpIdView.findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // get user input and set it to result
                        // edit text
                        Intent i = new Intent(FeedbackActivity.this,AddUpdateFeedback.class);
                        i.putExtra(EXTRA_ADD_UPDATE, "Update");
                        i.putExtra(EXTRA_FB_ID, Long.parseLong(userInput.getText().toString()));
                        startActivity(i);
                    }
                }).create()
                .show();

    }


    public void getFbIdAndRemoveFb(){

        LayoutInflater li = LayoutInflater.from(this);
        View getEmpIdView = li.inflate(R.layout.dialog_get_fb_id, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set dialog_get_emp_id.xml to alertdialog builder
        alertDialogBuilder.setView(getEmpIdView);

        final EditText userInput = (EditText) getEmpIdView.findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // get user input and set it to result
                        // edit text
                        feedbackOps = new FeedbackOperations(FeedbackActivity.this);
                        feedbackOps.open();
                        feedbackOps.removeFeedback(feedbackOps.getFeedback(Long.parseLong(userInput.getText().toString())));
                        Toast t = Toast.makeText(FeedbackActivity.this,"Feedback removed successfully!",Toast.LENGTH_SHORT);
                        t.show();
                    }
                }).create()
                .show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        feedbackOps = new FeedbackOperations(FeedbackActivity.this);
        feedbackOps.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        feedbackOps.close();

    }
}