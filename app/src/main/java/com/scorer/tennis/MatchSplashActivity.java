
package com.scorer.tennis;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class MatchSplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_splash);

        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method

            @Override
            public void run() {

                // close this activity
                finish();
            }
        }, 2000); // wait for 2 seconds
    }

    // ***********************************

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
