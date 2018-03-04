package com.scorer.tennis;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultsActivity extends Activity {

    private TextView t;
    private DBAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        myDb = new DBAdapter(this);
        myDb.open();
    }

// ******************************************************************************

    @Override
    protected void onResume() {
        super.onResume();

        myDb.close();
        myDb.open();

        String  s = myDb.readSystemStr(DBAdapter.KEY_SYSTEM_NAME_A);
        t = (TextView) findViewById(R.id.name_h);
        t.setText(s);

        s = myDb.readSystemStr(DBAdapter.KEY_SYSTEM_NAME_B);
        t = (TextView) findViewById(R.id.name_v);
        t.setText(s);

        int i = myDb.readSystem(DBAdapter.KEY_SYSTEM_SET_1_H);
        int I = myDb.readSystem(DBAdapter.KEY_SYSTEM_SET_1_V);

        if ((i > 0) || (I > 0)) {
            t = (TextView) findViewById(R.id.set_1_h);
            t.setText(Integer.toString(i));

            t = (TextView) findViewById(R.id.set_1_v);
            t.setText(Integer.toString(I));
        }

        i = myDb.readSystem(DBAdapter.KEY_SYSTEM_SET_2_H);
        I = myDb.readSystem(DBAdapter.KEY_SYSTEM_SET_2_V);

        if ((i > 0) || (I > 0)) {
            t = (TextView) findViewById(R.id.set_2_h);
            t.setText(Integer.toString(i));

            t = (TextView) findViewById(R.id.set_2_v);
            t.setText(Integer.toString(I));
        }

        i = myDb.readSystem(DBAdapter.KEY_SYSTEM_SET_3_H);
        I = myDb.readSystem(DBAdapter.KEY_SYSTEM_SET_3_V);

        if ((i > 0) || (I > 0)) {
            t = (TextView) findViewById(R.id.set_3_h);
            t.setText(Integer.toString(i));

            t = (TextView) findViewById(R.id.set_3_v);
            t.setText(Integer.toString(I));
        }

        i = myDb.readSystem(DBAdapter.KEY_SYSTEM_SET_4_H);
        I = myDb.readSystem(DBAdapter.KEY_SYSTEM_SET_4_V);

        if ((i > 0) || (I > 0)) {
            t = (TextView) findViewById(R.id.set_4_h);
            t.setText(Integer.toString(i));

            t = (TextView) findViewById(R.id.set_4_v);
            t.setText(Integer.toString(I));
        }


        i = myDb.readSystem(DBAdapter.KEY_SYSTEM_SET_5_H);
        I = myDb.readSystem(DBAdapter.KEY_SYSTEM_SET_5_V);

        if ((i > 0) || (I > 0)) {
            t = (TextView) findViewById(R.id.set_5_h);
            t.setText(Integer.toString(i));

            t = (TextView) findViewById(R.id.set_5_v);
            t.setText(Integer.toString(I));
        }
    }

// ***********************************

    @Override
    protected void onDestroy() {

        myDb.close();

        super.onDestroy();
    }

// ***********************************

    public void onClick(View view) {
        finish();
    }
}
