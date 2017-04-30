package applab.bedtimeapp;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import applab.bedtimeapp.utils.Constants;
import applab.bedtimeapp.utils.NotificationHelper;

public class TutorialIntro extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Put notification for one week later
        NotificationHelper.scheduleNotification(this, NotificationHelper.getNotification(this,"From Time Picker", SelfEfficacyActivity.class), Constants.DAYS_IN_A_WEEK * Constants.HOURS_IN_A_DAY * Constants.MINUTES_IN_AN_HOUR * Constants.SECONDS_IN_A_MINUTE * Constants.MILLISECONDS_IN_A_SECOND);
        //NotificationHelper.scheduleNotification(this, NotificationHelper.getNotification(this,"From Time Picker", SelfEfficacyActivity.class), Constants.SECONDS_IN_A_MINUTE * Constants.MILLISECONDS_IN_A_SECOND);

        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(FragmentSlide.newInstance(R.layout.intro_slide01));
        addSlide(FragmentSlide.newInstance(R.layout.intro_slide02));
        addSlide(FragmentSlide.newInstance(R.layout.intro_slide03));
        addSlide(FragmentSlide.newInstance(R.layout.intro_slide04));
        addSlide(FragmentSlide.newInstance(R.layout.intro_slide05));

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#5bc09a"));
        setSeparatorColor(Color.parseColor("#5bc09a"));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
        Intent i = new Intent(TutorialIntro.this, MainDrawerActivity.class);
        startActivity(i);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
        Intent i = new Intent(TutorialIntro.this, MainDrawerActivity.class);
        startActivity(i);

    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}