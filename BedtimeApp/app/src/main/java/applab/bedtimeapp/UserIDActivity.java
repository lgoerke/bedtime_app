package applab.bedtimeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class UserIDActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_id);

        EditText et = (EditText) findViewById(R.id.editID);
        et.setHint("Type here...");

        final Button btn = (Button) findViewById(R.id.confirmID);

        et.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                btn.setEnabled(true);
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });


        Integer.parseInt(et.getText().toString());

    }
}
