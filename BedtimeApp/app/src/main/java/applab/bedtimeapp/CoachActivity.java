package applab.bedtimeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class CoachActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach);

        ImageView iv = (ImageView) findViewById(R.id.owl);
        int height=iv.getHeight();
        Log.d("height",Integer.toString(height));
    }
}
