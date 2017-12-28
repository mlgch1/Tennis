
package com.scorer.tennis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class PasswordActivity extends Activity {

    DBAdapter myDb;

    // ***********************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

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
        EditText text = (EditText) findViewById(R.id.passWord);
        String strString = text.getText().toString();

        if (strString.equals("GJS")) {
            Intent s_intent = new Intent(PasswordActivity.this, ParamsActivity.class);
            startActivity(s_intent);
            myDb.K_Log("Password OK");
        } else {
            myDb.K_Log("Password not OK " + strString);
        }
        super.finish();
    }
}
