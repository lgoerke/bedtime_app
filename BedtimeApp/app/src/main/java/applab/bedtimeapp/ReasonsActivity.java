package applab.bedtimeapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class ReasonsActivity extends AppCompatActivity {


    public final static String EXTRA_REASON = "EXTRA_REASON" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reasons);

        EditText et = (EditText) findViewById(R.id.editReason);
        et.setHint("Type here...");

        ArrayList<String> ary = new ArrayList<String>();
        ary.add("Facebook");
        ary.add("Flatmates");
        ary.add("Laundry");

        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
        rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
        for (int i = 0; i < ary.size(); i++) {
            Log.d("why",ary.get(i));
            RadioButton rb = new RadioButton(this);
            rb.setText(ary.get(i));
            rb.setId(i);
            rb.setButtonDrawable(R.drawable.customized_radio_btn);
            rb.setTextColor(getResources().getColor(R.color.darkblue));
            if (Build.VERSION.SDK_INT < 23) {
                rb.setTextAppearance(this,android.R.style.TextAppearance_Medium);
            } else {
                rb.setTextAppearance(android.R.style.TextAppearance_Medium);
            }
            rg.addView(rb);
        }


    }

    public void goToQuestionnaire(View view){
        EditText et = (EditText) findViewById(R.id.editReason);
        Intent output = new Intent();
        output.putExtra(EXTRA_REASON, et.getText().toString());
        setResult(RESULT_OK, output);
        finish();
    }
}
