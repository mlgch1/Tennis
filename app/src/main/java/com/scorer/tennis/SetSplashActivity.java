
package com.scorer.tennis;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class SetSplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_splash);

        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method

            @Override
            public void run() {

                // close this activity
                finish();
            }
        }, 1000); // wait for 1 second
    }

    // ***********************************

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
