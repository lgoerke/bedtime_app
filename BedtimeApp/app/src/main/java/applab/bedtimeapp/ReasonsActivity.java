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

import applab.bedtimeapp.db.ResultOperations;
import applab.bedtimeapp.model.Reason;
import applab.bedtimeapp.utils.TextThumbSeekBar;

public class ReasonsActivity extends AppCompatActivity {


    public final static String EXTRA_REASON = "EXTRA_REASON";
    public final static String EXTRA_DURATION = "EXTRA_DURATION";

    private int minutes;
    private ResultOperations reasonData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reasons);

        EditText et = (EditText) findViewById(R.id.editReason);
        et.setHint("Type here...");

        reasonData = new ResultOperations(this);

        reasonData.open();

        //  Initialize SharedPreferences
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        //  Create a new boolean and preference and set it to true
        int userID = getPrefs.getInt("userID", 0);

        List<String> rL = reasonData.getAllReasons(userID);
        Log.e("rL",rL.toString());

        ArrayList<String> ary = new ArrayList<String>();

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
        TextThumbSeekBar ttsb = (TextThumbSeekBar) findViewById(R.id.thumbseekbar);
        minutes = ttsb.getProgress();
        output.putExtra(EXTRA_DURATION, minutes);
        setResult(RESULT_OK, output);
        finish();
    }


}