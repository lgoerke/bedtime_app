package applab.bedtimeapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CoachActivity extends AppCompatActivity {

    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach);

//        RelativeLayout rl = (RelativeLayout) findViewById(R.id.coach_view);
//        final ImageView iv = (ImageView) rl.findViewById(R.id.owl);
//
//
//        iv.getViewTreeObserver().addOnPreDrawListener(
//                new ViewTreeObserver.OnPreDrawListener() {
//                    public boolean onPreDraw() {
//                        height = iv.getMeasuredHeight();
//                        Log.d("height",Integer.toString(height));
//                        // Do your work here
//                        return true;
//                    }
//                });
//
//        final TextView tv = (TextView) rl.findViewById(R.id.advice);
//
//        tv.getViewTreeObserver().addOnPreDrawListener(
//                new ViewTreeObserver.OnPreDrawListener(){
//                    public boolean onPreDraw(){
//                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                                RelativeLayout.LayoutParams.WRAP_CONTENT,
//                                RelativeLayout.LayoutParams.WRAP_CONTENT
//                        );
//                        params.setMargins(16,height*(2/3),16,0);
//                        tv.setLayoutParams(params);
//
//                        return true;
//                    }
//                });



    }

}
