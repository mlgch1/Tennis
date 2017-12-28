
package com.scorer.tennis;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class RulesActivity extends Activity {

    DBAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        myDb = new DBAdapter(this);
        myDb.open();

        // No of Sets
        if (myDb.readSystem(DBAdapter.KEY_SYSTEM_SET_NO_1) == 1) {
            RadioButton rad_b = (RadioButton) findViewById(R.id.radioButton01);
            rad_b.setChecked(true);
        }

        if (myDb.readSystem(DBAdapter.KEY_SYSTEM_SET_NO_3) == 1) {
            RadioButton rad_b = (RadioButton) findViewById(R.id.radioButton02);
            rad_b.setChecked(true);
        }

        if (myDb.readSystem(DBAdapter.KEY_SYSTEM_SET_NO_5) == 1) {
            RadioButton rad_b = (RadioButton) findViewById(R.id.radioButton03);
            rad_b.setChecked(true);
        }

        // Set Type

        if (myDb.readSystem(DBAdapter.KEY_SYSTEM_SET_TYPE) == 0) {
            RadioButton rad_b = (RadioButton) findViewById(R.id.radioButton11);
            rad_b.setChecked(true);
            rad_b = (RadioButton) findViewById(R.id.radioButton12);
            rad_b.setChecked(false);
        } else {
            RadioButton rad_b = (RadioButton) findViewById(R.id.radioButton12);
            rad_b.setChecked(true);
            rad_b = (RadioButton) findViewById(R.id.radioButton11);
            rad_b.setChecked(false);

            TextView t = (TextView) findViewById(R.id.head_2);
            t.setVisibility(View.VISIBLE);

            RadioGroup rad = (RadioGroup) findViewById(R.id.radioGroup2);
            rad.setVisibility(View.VISIBLE);

            if (myDb.readSystem(DBAdapter.KEY_SYSTEM_LAST_SET) == 0) {
                RadioButton rad_c = (RadioButton) findViewById(R.id.radioButton21);
                rad_c.setChecked(true);
            } else {
                RadioButton rad_c = (RadioButton) findViewById(R.id.radioButton22);
                rad_c.setChecked(true);
            }
        }

//        // Match Tie Break
//        if (myDb.readSystem(DBAdapter.KEY_SYSTEM_MATCH_TB) == 0) {
//            CheckBox chkMtb = (CheckBox) findViewById(R.id.checkBox3);
//            chkMtb.setChecked(false);
//        } else {
//            CheckBox chkMtb = (CheckBox) findViewById(R.id.checkBox3);
//            chkMtb.setChecked(true);
//
//            RadioButton rad_b = (RadioButton) findViewById(R.id.radioButton12);
//            rad_b.setChecked(true);
//
////            TextView t = (TextView) findViewById(R.id.head_2);
////            t.setVisibility(View.VISIBLE);
//
//            RadioGroup rad = (RadioGroup) findViewById(R.id.radioGroup3);
//            rad.setVisibility(View.VISIBLE);
//
//            if (myDb.readSystem(DBAdapter.KEY_SYSTEM_MTB_7) == 1) {
//                RadioButton rad_c = (RadioButton) findViewById(R.id.radioButton31);
//                rad_c.setChecked(true);
//            }
//
//            if (myDb.readSystem(DBAdapter.KEY_SYSTEM_MTB_10) == 1) {
//                RadioButton rad_c = (RadioButton) findViewById(R.id.radioButton32);
//                rad_c.setChecked(true);
//            }
//        }

        // No Advantage
        CheckBox noAdv = (CheckBox) findViewById(R.id.checkBox1);
        noAdv.setChecked((myDb.readSystem(DBAdapter.KEY_SYSTEM_NO_ADV) != 0));

        // Short Sets
        CheckBox ss = (CheckBox) findViewById(R.id.checkBox2);
        ss.setChecked((myDb.readSystem(DBAdapter.KEY_SYSTEM_SHORT_SETS) != 0));

//        // Match Tie Break
//        CheckBox mtb = (CheckBox) findViewById(R.id.checkBox3);
//        mtb.setChecked((myDb.readSystem(DBAdapter.KEY_SYSTEM_MATCH_TB) != 0));

//        // FAST4
//        CheckBox f4 = (CheckBox) findViewById(R.id.checkBox4);
//        f4.setChecked((myDb.readSystem(DBAdapter.KEY_SYSTEM_FAST4) != 0));

//        if (f4.isChecked()) {
//            Fast4();
//        }
    }

    // ***********************************

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // ***********************************

    @Override
    public void finish() {
        super.finish();
        myDb.close();
    }

    // ***********************************

    public void onClick_TieBreak(View view) {

        TextView t = (TextView) findViewById(R.id.head_2);
        t.setVisibility(View.VISIBLE);

        RadioGroup rad = (RadioGroup) findViewById(R.id.radioGroup2);
        rad.setVisibility(View.VISIBLE);
    }

    // ***********************************

    public void onClick_Advantage(View view) {

        RadioGroup rad = (RadioGroup) findViewById(R.id.radioGroup2);
        rad.setVisibility(View.INVISIBLE);

        TextView t = (TextView) findViewById(R.id.head_2);
        t.setVisibility(View.INVISIBLE);
    }

//    // ***********************************
//
//    public void onClick_Mtb(View view) {
//
//        RadioGroup rad = (RadioGroup) findViewById(R.id.radioGroup3);
//
//        CheckBox mtb = (CheckBox) findViewById(R.id.checkBox3);
//
//        if (mtb.isChecked()) {
//            rad.setVisibility(View.VISIBLE);
//        }else{
//            rad.setVisibility(View.INVISIBLE);
//        }
//    }

    // ***********************************

//    public void onClick_Fast4(View view) {
//        Fast4();
//    }

    // ***********************************

//    public void Fast4() {
//        CheckBox f4 = (CheckBox) findViewById(R.id.checkBox4);
//
//        TextView t1 = (TextView) findViewById(R.id.head_0);
//        TextView t2 = (TextView) findViewById(R.id.head_1);
//        TextView t3 = (TextView) findViewById(R.id.head_2);
//
//        RadioGroup rad0 = (RadioGroup) findViewById(R.id.radioGroup0);
//        RadioGroup rad1 = (RadioGroup) findViewById(R.id.radioGroup1);
//        RadioGroup rad2 = (RadioGroup) findViewById(R.id.radioGroup2);
//
//        RadioButton rb22 = (RadioButton) findViewById(R.id.radioButton22);
//
//
//        RadioGroup rad3 = (RadioGroup) findViewById(R.id.radioGroup3);
//
//        CheckBox c1 = (CheckBox) findViewById(R.id.checkBox1);
//        CheckBox c2 = (CheckBox) findViewById(R.id.checkBox2);
//        CheckBox c3 = (CheckBox) findViewById(R.id.checkBox3);
//
//        if (!f4.isChecked()) {
//            t1.setVisibility(View.VISIBLE);
//            t2.setVisibility(View.VISIBLE);
//            t3.setVisibility(View.VISIBLE);
//
//            rad0.setVisibility(View.VISIBLE);
//            rad1.setVisibility(View.VISIBLE);
//
//            if (rb22.isChecked()) {
//                rad2.setVisibility(View.VISIBLE);
//            }
//
//            if (c3.isChecked()) {
//                rad3.setVisibility(View.VISIBLE);
//            }
//
//            c1.setVisibility(View.VISIBLE);
//            c2.setVisibility(View.VISIBLE);
//            c3.setVisibility(View.VISIBLE);
//        }else{
//            t1.setVisibility(View.INVISIBLE);
//            t2.setVisibility(View.INVISIBLE);
//            t3.setVisibility(View.INVISIBLE);
//
//            rad0.setVisibility(View.INVISIBLE);
//            rad1.setVisibility(View.INVISIBLE);
//            rad2.setVisibility(View.INVISIBLE);
//            rad3.setVisibility(View.INVISIBLE);
//
//            c1.setVisibility(View.INVISIBLE);
//            c2.setVisibility(View.INVISIBLE);
//            c3.setVisibility(View.INVISIBLE);
//        }
//    }
//
    // ***********************************

    public void onClick_Click(View view) {
    }

    // ***********************************

    public void onClick(View view) {
        Finish();
    }

    // ***********************************

    public void Finish() {

        // No of sets
        RadioGroup rad0 = (RadioGroup) findViewById(R.id.radioGroup0);

        // get selected radio button from radioGroup 0
        int selectedId = rad0.getCheckedRadioButtonId();

        switch (selectedId) {
            case R.id.radioButton01:
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_SET_NO_1, 1);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_SET_NO_3, 0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_SET_NO_5, 0);
                break;

            case R.id.radioButton02:
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_SET_NO_1, 0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_SET_NO_3, 1);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_SET_NO_5, 0);
                break;

            case R.id.radioButton03:
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_SET_NO_1, 0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_SET_NO_3, 0);
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_SET_NO_5, 1);
                break;
        }

        // Type of Set
        RadioGroup rad1 = (RadioGroup) findViewById(R.id.radioGroup1);

        // get selected radio button from radioGroup 1
        selectedId = rad1.getCheckedRadioButtonId();

        switch (selectedId) {
            case R.id.radioButton11:
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_SET_TYPE, 0);
                break;

            case R.id.radioButton12:
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_SET_TYPE, 1);
                break;
        }

        // Last Set
        RadioGroup rad2 = (RadioGroup) findViewById(R.id.radioGroup2);

        // get selected radio button from radioGroup 2
        selectedId = rad2.getCheckedRadioButtonId();

        switch (selectedId) {
            case R.id.radioButton21:
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_LAST_SET, 0);
                break;

            case R.id.radioButton22:
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_LAST_SET, 1);
                break;
        }

        // No Advantage
        CheckBox chkNoAdv = (CheckBox) findViewById(R.id.checkBox1);
        myDb.updateSystem(DBAdapter.KEY_SYSTEM_NO_ADV, (chkNoAdv.isChecked() ? 1 : 0));

        // Short Sets
        CheckBox chkSs = (CheckBox) findViewById(R.id.checkBox2);
        myDb.updateSystem(DBAdapter.KEY_SYSTEM_SHORT_SETS, (chkSs.isChecked() ? 1 : 0));

//        // Match Tie Break
//        CheckBox chkmtb = (CheckBox) findViewById(R.id.checkBox3);
//        myDb.updateSystem(DBAdapter.KEY_SYSTEM_MATCH_TB, (chkmtb.isChecked() ? 1 : 0));

//        RadioGroup rad3 = (RadioGroup) findViewById(R.id.radioGroup3);
//
//        // get selected radio button from radioGroup 3
//        selectedId = rad3.getCheckedRadioButtonId();

//        switch (selectedId) {
//            case R.id.radioButton31:
//                myDb.updateSystem(DBAdapter.KEY_SYSTEM_MTB_7, 1);
//                myDb.updateSystem(DBAdapter.KEY_SYSTEM_MTB_10, 0);
//                break;
//
//            case R.id.radioButton32:
//                myDb.updateSystem(DBAdapter.KEY_SYSTEM_MTB_7, 0);
//                myDb.updateSystem(DBAdapter.KEY_SYSTEM_MTB_10, 1);
//                break;
//        }

//        // FAST4
//        CheckBox chkf4 = (CheckBox) findViewById(R.id.checkBox4);
//        myDb.updateSystem(DBAdapter.KEY_SYSTEM_FAST4, (chkf4.isChecked() ? 1 : 0));

        super.finish();
    }
}
