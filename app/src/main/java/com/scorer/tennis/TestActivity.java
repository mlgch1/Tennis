
package com.scorer.tennis;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;

import static com.scorer.tennis.GlobalClass.setStartTesting;
import static com.scorer.tennis.GlobalClass.setTest;

public class TestActivity extends Activity {

    Button myButton;
    Animation anim = new AlphaAnimation(0.0f, 1.0f);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        myButton = (Button) findViewById(R.id.b_test);
    }

    // ***********************************

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // ***********************************

    @Override
    public void finish() {
        myButton.clearAnimation();

        setStartTesting(false);
        super.finish();
    }

    // ***********************************

    public void onClick_Test(View view) {

            anim.setDuration(250); //You can manage the time of the blink with this parameter
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            myButton.startAnimation(anim);

            setStartTesting(true);
            setTest(true);
    }

    // ***********************************

    public void onClick_stop_Test(View view) {
        myButton.clearAnimation();

        setStartTesting(false);
    }

    // ***********************************

    public void onClick(View view) {
        finish();
    }
}
