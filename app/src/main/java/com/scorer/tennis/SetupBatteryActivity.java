
package com.scorer.tennis;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SetupBatteryActivity extends Activity {
    DBAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        openDB();

        EditText text = (EditText) findViewById(R.id.inputThresh);
        text.setText("" + GlobalClass.getThresh());

        text = (EditText) findViewById(R.id.inputInc);
        text.setText("" + GlobalClass.getInc());
    }

    // ***********************************

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    // ***********************************

    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }

    // ***********************************

    private void closeDB() {
        myDb.close();
    }

    // ***********************************

    public void onClick(View view) {
        Finish();
    }

    // ***********************************

    public void Finish() {

        EditText text = (EditText) findViewById(R.id.inputThresh);

        if (text.getText().toString() != "") {
            Integer intValue = Integer.parseInt(text.getText().toString());

            if (intValue != 0) {
                myDb.K_Log("Battery Threshhold " + intValue);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_BATT_THRESH, intValue);

                GlobalClass.setThresh(intValue);
            }
        }

        text = (EditText) findViewById(R.id.inputInc);

        if (text.getText().toString() != "") {
            Integer intValue = Integer.parseInt(text.getText().toString());

            if (intValue != 0) {
                myDb.K_Log("Battery Inc " + intValue);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_BATT_INC, intValue);

                GlobalClass.setInc(intValue);
            }
        }
        finish();
    }
}
