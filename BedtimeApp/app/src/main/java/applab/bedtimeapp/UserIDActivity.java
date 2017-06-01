package applab.bedtimeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import applab.bedtimeapp.db.ResultOperations;
import applab.bedtimeapp.model.Result;

public class UserIDActivity extends AppCompatActivity {

    private int ID;
    private static final int STUDY_TIME = 14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_id);

        final EditText et = (EditText) findViewById(R.id.editID);
        et.setHint("Type here...");

        final Button btn = (Button) findViewById(R.id.confirmID);

        et.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    btn.setEnabled(false);
                } else{
                    btn.setEnabled(true);
                    ID = Integer.parseInt(et.getText().toString());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() == 0) {
                    btn.setEnabled(false);
                } else{
                    btn.setEnabled(true);
                    ID = Integer.parseInt(et.getText().toString());
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                int savedID = getPrefs.getInt("userID", 0);

                //  Make a new preferences editor
                SharedPreferences.Editor e = getPrefs.edit();

                //  Edit preference to make it false because we don't want this to run again
                e.putInt("userID", ID);

                // Put start date to preferences
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                Calendar currentDateCal = Calendar.getInstance();
                String startDate = format1.format(currentDateCal.getTime());
                e.putString("startDate", startDate);

                //  Apply changes
                e.apply();

                createFourteen(ID,startDate);

                finish();
            }
        });



    }

    private void createFourteen(int userId,String startDate) {

        ResultOperations ro = new ResultOperations(this);
        ro.open();
        for(int i = 1; i<= STUDY_TIME; i++){
            Result r = new Result();
            r.setUserId(userId);
            r.setDayId(((long) i));
            r.setCreationDate(startDate);
            ro.addResult(r);
        }
        ro.close();

    }
}
