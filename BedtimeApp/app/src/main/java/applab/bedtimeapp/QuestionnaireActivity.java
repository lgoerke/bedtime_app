package applab.bedtimeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

public class QuestionnaireActivity extends AppCompatActivity {

    private boolean metBedtime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

    }


    public void onBtnClicked(View view) {
        // Check which btn was clicked
        Button btn;
        switch (view.getId()) {
            case R.id.btn_yes:
                btn = (Button) findViewById(R.id.btn_yes);
                btn.setPressed(true);
                btn = (Button) findViewById(R.id.btn_no);
                btn.setPressed(false);
                metBedtime = true;
                break;
            case R.id.btn_no:
                btn = (Button) findViewById(R.id.btn_no);
                btn.setPressed(true);
                btn = (Button) findViewById(R.id.btn_yes);
                btn.setPressed(false);
                metBedtime = false;
                break;
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
