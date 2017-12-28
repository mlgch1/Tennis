
package com.scorer.tennis;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ManualActivity extends Activity {

    DBAdapter myDb;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_manual);

        myDb = new DBAdapter (this);
        myDb.open ();
    }

    // ***********************************

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        myDb.close ();
    }

    // ***********************************

    @Override
    public void finish () {
        super.finish ();
    }

    // ***********************************

    public void onClick (View view) {

        // Points

        EditText t = (EditText) findViewById (R.id.points_no_a);
        Integer intValue_a = Integer.parseInt (t.getText ().toString ());

        t = (EditText) findViewById (R.id.points_no_b);
        Integer intValue_b = Integer.parseInt (t.getText ().toString ());

        if ((intValue_a == 4) && (intValue_b != 3)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Adv. reduced to 40.", Toast.LENGTH_LONG);
            toast.show();

            intValue_a = 3;
        }
        if ((intValue_b == 4) && (intValue_a != 3)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Adv. reduced to 40.", Toast.LENGTH_LONG);
            toast.show();

            intValue_b = 3;
        }

        myDb.updateSystemStr (DBAdapter.KEY_SYSTEM_POINTS_A, String.valueOf (intValue_a));
        myDb.updateSystemStr (DBAdapter.KEY_SYSTEM_POINTS_B, String.valueOf (intValue_b));

        // Games

        t = (EditText) findViewById (R.id.games_no_a);
        Integer intValue = Integer.parseInt (t.getText ().toString ());
        myDb.updateSystemStr (DBAdapter.KEY_SYSTEM_GAMES_A, String.valueOf (intValue));

        t = (EditText) findViewById (R.id.games_no_b);
        intValue = Integer.parseInt (t.getText ().toString ());
        myDb.updateSystemStr (DBAdapter.KEY_SYSTEM_GAMES_B, String.valueOf (intValue));

        // Sets

        t = (EditText) findViewById (R.id.sets_no_a);
        intValue = Integer.parseInt (t.getText ().toString ());
        myDb.updateSystemStr (DBAdapter.KEY_SYSTEM_SETS_A, String.valueOf (intValue));

        t = (EditText) findViewById (R.id.sets_no_b);
        intValue = Integer.parseInt (t.getText ().toString ());
        myDb.updateSystemStr (DBAdapter.KEY_SYSTEM_SETS_B, String.valueOf (intValue));

        finish ();
    }
}
