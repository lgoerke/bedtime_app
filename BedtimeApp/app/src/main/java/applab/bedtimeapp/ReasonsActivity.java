package applab.bedtimeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import applab.bedtimeapp.db.ReasonOperations;
import applab.bedtimeapp.model.Reason;
import applab.bedtimeapp.utils.TextThumbSeekBar;

public class ReasonsActivity extends AppCompatActivity {


    public final static String EXTRA_REASON = "EXTRA_REASON";

    private int minutes;
    private ReasonOperations reasonData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reasons);

        TextThumbSeekBar ttsb = (TextThumbSeekBar) findViewById(R.id.thumbseekbar);
        ttsb.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                minutes  = ((TextThumbSeekBar) view).getProgress();
                return true;
            }
        });

        EditText et = (EditText) findViewById(R.id.editReason);
        et.setHint("Type here...");

        reasonData = new ReasonOperations(this);

        reasonData.open();

        //  Initialize SharedPreferences
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        int userID = getPrefs.getInt("userID", 0);

        List<Reason> rL = reasonData.getAllReasons(userID);

        ArrayList<String> ary = new ArrayList<String>();

        for (int i = 0; i < rL.size(); i++) {
            ary.add(rL.get(i).getReason());
            System.err.println(rL.get(i).getReason());
        }
        HashSet<String> uniques = new HashSet<>(ary);
        ary = new ArrayList<String>(uniques);

        reasonData.close();

        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroupReasons);
        rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
        for (int i = 0; i < ary.size(); i++) {
            Log.d("why", ary.get(i));
            RadioButton rb = new RadioButton(this);
            rb.setText(ary.get(i));
            rb.setId(i);
            rb.setButtonDrawable(R.drawable.customized_radio_btn);
            rb.setTextColor(getResources().getColor(R.color.darkblue));

            final int finalI = i;
            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText et = (EditText) findViewById(R.id.editReason);
                    et.setText(((RadioButton) findViewById(finalI)).getText());
                }
            });
            if (Build.VERSION.SDK_INT < 23) {
                rb.setTextAppearance(this, android.R.style.TextAppearance_Medium);
            } else {
                rb.setTextAppearance(android.R.style.TextAppearance_Medium);
            }
            rg.addView(rb);
        }


    }

    public void goToQuestionnaire(View view) {
        EditText et = (EditText) findViewById(R.id.editReason);
        Intent output = new Intent(this, QuestionnaireActivity.class);
        output.putExtra(EXTRA_REASON, et.getText().toString());
        //TODO save minutes
        saveReason(et.getText().toString());
        setResult(RESULT_OK, output);
        finish();
    }

    private void saveReason(String text) {
        reasonData.open();
        Reason reason = new Reason();
        // SET USER ID
        //  Initialize SharedPreferences
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        int userID = getPrefs.getInt("userID", 0);
        reason.setUserId(userID);
        reason.setReason(text);
        reasonData.addReason(reason);
        reasonData.close();
    }
}
