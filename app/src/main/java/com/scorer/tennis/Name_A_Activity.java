
package com.scorer.tennis;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Name_A_Activity extends Activity {

    DBAdapter myDb;

    // ***********************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_a);

        myDb = new DBAdapter(this);
        myDb.open();
    }

    // ***********************************

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDb.close();
    }

    // ***********************************

    public void onClick(View view) {
        Finish();
    }

    // ***********************************

    public void Finish() {
        EditText text = (EditText) findViewById(R.id.name_a);
        String strString = text.getText().toString();

        strString = strString.trim();

        if (strString.length() != 0) {
            myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_NAME_A, strString);
        }

        finish();
    }
}
