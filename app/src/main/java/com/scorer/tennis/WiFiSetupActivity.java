
package com.scorer.tennis;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.scorer.tennis.GlobalClass.getChannel;
import static com.scorer.tennis.GlobalClass.getSSID;
import static com.scorer.tennis.GlobalClass.setChannel;
import static com.scorer.tennis.GlobalClass.setSSID;

public class WiFiSetupActivity extends Activity {

    DBAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        myDb = new DBAdapter(this);
        myDb.open();

        EditText text = (EditText) findViewById(R.id.inputSSID);
        text.setText("" + getSSID());
        text = (EditText) findViewById(R.id.inputChannel);
        text.setText("" + getChannel());

        myDb.K_Log("WiFi Setup");
    }
    // ******************************************************************************

    @Override
    protected void onDestroy() {

        super.onDestroy();
        myDb.close();
    }
    // ******************************************************************************

     public void onClick(View view) {

        EditText text = (EditText) findViewById(R.id.inputSSID);
        if (!text.getText().toString().equals("")) {
            Integer intValue = Integer.parseInt(text.getText().toString());
            if (intValue != 0) {
                myDb.K_Log("SSID " + intValue);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_SSID, intValue);
                setSSID(intValue);
            }
        }
        text = (EditText) findViewById(R.id.inputChannel);
        if (!text.getText().toString().equals("")) {
            Integer intValue = Integer.parseInt(text.getText().toString());
            if (intValue != 0) {
                myDb.K_Log("Channel " + intValue);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_CHANNEL, intValue);
                setChannel(intValue);
            }
        }

        finish();
    }
}
