
package com.scorer.tennis;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class RulesActivity extends Activity {

    DBAdapter myDb;

    private int set_no_1;
    private int set_no_3 = 1;
    private int set_no_5;
    private int set_type;
    private int last_set_type;
    private int fast4;
    private int no_adv;
    private int short_sets;
    private int match_tb_game;
    private int mtb_7;
    private int mtb_10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        myDb = new DBAdapter(this);
        myDb.open();

//        set_no_1 = myDb.readSystem(DBAdapter.KEY_SYSTEM_SET_NO_1);
//        set_no_3 = myDb.readSystem(DBAdapter.KEY_SYSTEM_SET_NO_3);
//        set_no_5 = myDb.readSystem(DBAdapter.KEY_SYSTEM_SET_NO_5);
//        set_type = myDb.readSystem(DBAdapter.KEY_SYSTEM_SET_TYPE);
//        last_set_type = myDb.readSystem(DBAdapter.KEY_SYSTEM_LAST_SET);
//        no_adv = myDb.readSystem(DBAdapter.KEY_SYSTEM_NO_ADV);
//        fast4 = myDb.readSystem(DBAdapter.KEY_SYSTEM_FAST4);
//        short_sets = myDb.readSystem(DBAdapter.KEY_SYSTEM_SHORT_SETS);
//        match_tb_game = myDb.readSystem(DBAdapter.KEY_SYSTEM_MATCH_TB);
//        mtb_7 = myDb.readSystem(DBAdapter.KEY_SYSTEM_MTB_7);
//        mtb_10 = myDb.readSystem(DBAdapter.KEY_SYSTEM_MTB_10);
//
        display();
    }

    // ***********************************

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDb.close();
    }

    // ***********************************

    @Override
    public void finish() {
        super.finish();
    }

    // ***********************************

    public void display() {

        TextView t_head_no_sets = (TextView) findViewById(R.id.head_No_Sets);
        TextView t_head_type_of_set = (TextView) findViewById(R.id.head_Type_of_Set);
        TextView t_head_last_set_type = (TextView) findViewById(R.id.head_last_set_type);

        RadioGroup rg_no_sets = (RadioGroup) findViewById(R.id.radioGroup_No_Sets);
        RadioGroup rg_type_of_set = (RadioGroup) findViewById(R.id.radioGroup_Type_of_Set);
        RadioGroup rg_last_set_type = (RadioGroup) findViewById(R.id.radioGroup_last_set_type);
        RadioGroup rg_match_tb_game = (RadioGroup) findViewById(R.id.radioGroup_Match_Tb_Game);

        RadioButton rb_1_set = (RadioButton) findViewById(R.id.radioButton_1_Set);
        RadioButton rb_3_set = (RadioButton) findViewById(R.id.radioButton_3_Set);
        RadioButton rb_5_set = (RadioButton) findViewById(R.id.radioButton_5_Set);
        RadioButton rb_adv_set = (RadioButton) findViewById(R.id.radioButton_Adv_Set);
        RadioButton rb_tb_set = (RadioButton) findViewById(R.id.radioButton_Tb_Set);
        RadioButton rb_ls_adv_set = (RadioButton) findViewById(R.id.radioButton_LS_Adv_Set);
        RadioButton rb_ls_tb_set = (RadioButton) findViewById(R.id.radioButton_LS_Tb_Set);
        RadioButton rb_7_points = (RadioButton) findViewById(R.id.radioButton_7_Points);
        RadioButton rb_10_points = (RadioButton) findViewById(R.id.radioButton_10_Points);

        CheckBox cb_fast4 = (CheckBox) findViewById(R.id.checkBox_Fast4);
        CheckBox cb_no_adv = (CheckBox) findViewById(R.id.checkBox_No_Adv);
        CheckBox cb_short_sets = (CheckBox) findViewById(R.id.checkBox_Short_Sets);
        CheckBox cb_match_tb_game = (CheckBox) findViewById(R.id.checkBox_Match_Tb_Game);

        // No of Sets
        rb_1_set.setChecked(set_no_1 == 1);
        rb_3_set.setChecked(set_no_3 == 1);
        rb_5_set.setChecked(set_no_5 == 1);

        // FAST4
        cb_fast4.setChecked((fast4) == 1);

        if (cb_fast4.isChecked()) {
            t_head_no_sets.setVisibility(View.VISIBLE);
            t_head_type_of_set.setVisibility(View.INVISIBLE);
            t_head_last_set_type.setVisibility(View.INVISIBLE);

            rg_no_sets.setVisibility(View.VISIBLE);
            rg_type_of_set.setVisibility(View.INVISIBLE);
            rg_last_set_type.setVisibility(View.INVISIBLE);
            rg_match_tb_game.setVisibility(View.INVISIBLE);

            cb_no_adv.setVisibility(View.INVISIBLE);
            cb_short_sets.setVisibility(View.INVISIBLE);
            cb_match_tb_game.setVisibility(View.INVISIBLE);

        } else {
            t_head_no_sets.setVisibility(View.VISIBLE);
            t_head_type_of_set.setVisibility(View.VISIBLE);
            t_head_last_set_type.setVisibility(View.VISIBLE);

            rg_no_sets.setVisibility(View.VISIBLE);
            rg_type_of_set.setVisibility(View.VISIBLE);
            rg_last_set_type.setVisibility(View.VISIBLE);
            rg_match_tb_game.setVisibility(View.VISIBLE);

            cb_no_adv.setVisibility(View.VISIBLE);
            cb_short_sets.setVisibility(View.VISIBLE);
            cb_match_tb_game.setVisibility(View.VISIBLE);

            // Set Type

            if (no_adv == 1) {
                rg_last_set_type.setVisibility(View.INVISIBLE);
                t_head_last_set_type.setVisibility(View.INVISIBLE);

                rg_type_of_set.setVisibility(View.INVISIBLE);
                t_head_type_of_set.setVisibility(View.INVISIBLE);
            } else {
                if (set_type == 0) {
                    rg_last_set_type.setVisibility(View.INVISIBLE);
                    t_head_last_set_type.setVisibility(View.INVISIBLE);
                    rb_adv_set.setChecked(true);
                    rb_tb_set.setChecked(false);
                } else {
                    if (!(set_no_1 == 1)) {
                        t_head_last_set_type.setVisibility(View.VISIBLE);
                        rg_last_set_type.setVisibility(View.VISIBLE);
                    } else {
                        t_head_last_set_type.setVisibility(View.INVISIBLE);
                        rg_last_set_type.setVisibility(View.INVISIBLE);
                    }
                    rb_adv_set.setChecked(false);
                    rb_tb_set.setChecked(true);

                    rb_ls_adv_set.setChecked(last_set_type == 0);
                    rb_ls_tb_set.setChecked(last_set_type == 1);
                }
            }
            // Match Tie Break
            cb_match_tb_game.setChecked(match_tb_game != 0);

            if (match_tb_game == 0) {
                rg_match_tb_game.setVisibility(View.INVISIBLE);
            } else {
                rg_match_tb_game.setVisibility(View.VISIBLE);

                rb_7_points.setChecked(mtb_7 == 1);
                rb_10_points.setChecked(mtb_10 == 1);
            }

            // No Advantage
            cb_no_adv.setChecked((no_adv) == 1);

            // Short Sets
            cb_short_sets.setChecked((short_sets) == 1);

            if (short_sets == 1) {
                t_head_last_set_type.setVisibility(View.INVISIBLE);
                rg_last_set_type.setVisibility(View.INVISIBLE);

                t_head_type_of_set.setVisibility(View.INVISIBLE);
                rg_type_of_set.setVisibility(View.INVISIBLE);

                cb_fast4.setVisibility(View.INVISIBLE);
                cb_match_tb_game.setVisibility(View.INVISIBLE);

                last_set_type = 1;
            }else{
                cb_fast4.setVisibility(View.VISIBLE);
                cb_match_tb_game.setVisibility(View.VISIBLE);
            }
        }
    }

    // ***********************************

    public void onClick_1_Set(View view) {

        RadioGroup rad = (RadioGroup) findViewById(R.id.radioGroup_last_set_type);
        rad.setVisibility(View.INVISIBLE);

        TextView t = (TextView) findViewById(R.id.head_last_set_type);
        t.setVisibility(View.INVISIBLE);

        set_no_1 = 1;
        set_no_3 = 0;
        set_no_5 = 0;

        display();
    }

    // ***********************************

    public void onClick_3_Set(View view) {
        set_no_1 = 0;
        set_no_3 = 1;
        set_no_5 = 0;

        display();
    }

    // ***********************************

    public void onClick_5_Set(View view) {
        set_no_1 = 0;
        set_no_3 = 0;
        set_no_5 = 1;

        display();
    }

    // ***********************************

    public void onClick_Advantage(View view) {
        set_type = 0;

        RadioGroup rad = (RadioGroup) findViewById(R.id.radioGroup_last_set_type);
        rad.setVisibility(View.INVISIBLE);

        TextView t = (TextView) findViewById(R.id.head_last_set_type);
        t.setVisibility(View.INVISIBLE);

        display();
    }

    // ***********************************

    public void onClick_TieBreak(View view) {
        set_type = 1;

        if (!(set_no_1 == 1)) {
            TextView t = (TextView) findViewById(R.id.head_last_set_type);
            t.setVisibility(View.VISIBLE);

            RadioGroup rad = (RadioGroup) findViewById(R.id.radioGroup_last_set_type);
            rad.setVisibility(View.VISIBLE);
        } else {
            TextView t = (TextView) findViewById(R.id.head_last_set_type);
            t.setVisibility(View.INVISIBLE);

            RadioGroup rad = (RadioGroup) findViewById(R.id.radioGroup_last_set_type);
            rad.setVisibility(View.INVISIBLE);
        }

        display();
    }

    // ***********************************

    public void onClick_Ls_Advantage(View view) {
        last_set_type = 0;

        display();
    }

    // ***********************************

    public void onClick_Ls_TieBreak(View view) {
        last_set_type = 1;

        display();
    }

    // ***********************************

    public void onClick_No_Adv(View view) {
        CheckBox cb_no_adv = (CheckBox) findViewById(R.id.checkBox_No_Adv);
        no_adv = (cb_no_adv.isChecked() ? 1 : 0);

        display();
    }
    // ***********************************

    public void onClick_Fast4(View view) {
        CheckBox cb_fast4 = (CheckBox) findViewById(R.id.checkBox_Fast4);
        fast4 = (cb_fast4.isChecked() ? 1 : 0);

        display();
    }
    // ***********************************

    public void onClick_Short_Sets(View view) {
        CheckBox cb_short_sets = (CheckBox) findViewById(R.id.checkBox_Short_Sets);
        short_sets = (cb_short_sets.isChecked() ? 1 : 0);

        if (short_sets == 1) {
            set_type = 1;
            last_set_type = 1;
        } else {
            set_type = 0;
            last_set_type = 0;
        }

        display();
    }

    // ***********************************

    public void onClick_Mtb(View view) {
        CheckBox cb_match_tb_game = (CheckBox) findViewById(R.id.checkBox_Match_Tb_Game);
        match_tb_game = (cb_match_tb_game.isChecked() ? 1 : 0);

        RadioGroup rg_match_tb_game = (RadioGroup) findViewById(R.id.radioGroup_Match_Tb_Game);

        if (cb_match_tb_game.isChecked()) {
            rg_match_tb_game.setVisibility(View.VISIBLE);
            mtb_7 = 0;
        } else {
            rg_match_tb_game.setVisibility(View.INVISIBLE);
            mtb_7 = 0;
            mtb_10 = 0;
        }

        display();
    }

    // ***********************************

    public void onClick_7_Points(View view) {
        RadioButton rb_7_points = (RadioButton) findViewById(R.id.radioButton_7_Points);
        RadioButton rb_10_points = (RadioButton) findViewById(R.id.radioButton_10_Points);
        mtb_7 = (rb_7_points.isChecked() ? 1 : 0);
        mtb_10 = (rb_10_points.isChecked() ? 1 : 0);

        display();
    }

    // ***********************************

    public void onClick_10_Points(View view) {
        RadioButton rb_7_points = (RadioButton) findViewById(R.id.radioButton_7_Points);
        RadioButton rb_10_points = (RadioButton) findViewById(R.id.radioButton_10_Points);
        mtb_7 = (rb_7_points.isChecked() ? 1 : 0);
        mtb_10 = (rb_10_points.isChecked() ? 1 : 0);

        display();
    }

    // ***********************************

    public void onClick_Store(View view) {
        Finish();
    }

    // ***********************************

    public void Finish() {
        RadioButton rb_1_set = (RadioButton) findViewById(R.id.radioButton_1_Set);
        RadioButton rb_3_set = (RadioButton) findViewById(R.id.radioButton_3_Set);
        RadioButton rb_5_set = (RadioButton) findViewById(R.id.radioButton_5_Set);
        RadioButton rb_7_points = (RadioButton) findViewById(R.id.radioButton_7_Points);
        RadioButton rb_10_points = (RadioButton) findViewById(R.id.radioButton_10_Points);

        CheckBox cb_fast4 = (CheckBox) findViewById(R.id.checkBox_Fast4);
        CheckBox cb_no_adv = (CheckBox) findViewById(R.id.checkBox_No_Adv);
        CheckBox cb_short_sets = (CheckBox) findViewById(R.id.checkBox_Short_Sets);
        CheckBox cb_match_tb_game = (CheckBox) findViewById(R.id.checkBox_Match_Tb_Game);

        // No of sets
        myDb.updateSystem(DBAdapter.KEY_SYSTEM_SET_NO_1, rb_1_set.isChecked() ? 1 : 0);
        myDb.updateSystem(DBAdapter.KEY_SYSTEM_SET_NO_3, rb_3_set.isChecked() ? 1 : 0);
        myDb.updateSystem(DBAdapter.KEY_SYSTEM_SET_NO_5, rb_5_set.isChecked() ? 1 : 0);

        // Type of Set
        RadioGroup rg_type_of_set = (RadioGroup) findViewById(R.id.radioGroup_Type_of_Set);

        // get selected radio button from rg_type_of_set
        int selectedId = rg_type_of_set.getCheckedRadioButtonId();

        switch (selectedId) {
            case R.id.radioButton_Adv_Set:
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_SET_TYPE, 0);
                break;

            case R.id.radioButton_Tb_Set:
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_SET_TYPE, 1);
                break;
        }

        // Last Set
        RadioGroup rg_last_set_type = (RadioGroup) findViewById(R.id.radioGroup_last_set_type);

        // get selected radio button from radioGroup 2
        selectedId = rg_last_set_type.getCheckedRadioButtonId();

        switch (selectedId) {
            case R.id.radioButton_LS_Adv_Set:
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_LAST_SET, 0);
                break;

            case R.id.radioButton_LS_Tb_Set:
                myDb.updateSystem(DBAdapter.KEY_SYSTEM_LAST_SET, 1);
                break;
        }

        // No Advantage
        myDb.updateSystem(DBAdapter.KEY_SYSTEM_NO_ADV, (cb_no_adv.isChecked() ? 1 : 0));

        // Short Sets
        myDb.updateSystem(DBAdapter.KEY_SYSTEM_SHORT_SETS, (cb_short_sets.isChecked() ? 1 : 0));

        // Match Tie Break
        myDb.updateSystem(DBAdapter.KEY_SYSTEM_MATCH_TB, (cb_match_tb_game.isChecked() ? 1 : 0));

        myDb.updateSystem(DBAdapter.KEY_SYSTEM_MTB_7, rb_7_points.isChecked() ? 1 : 0);
        myDb.updateSystem(DBAdapter.KEY_SYSTEM_MTB_10, rb_10_points.isChecked() ? 1 : 0);

        // FAST4
        myDb.updateSystem(DBAdapter.KEY_SYSTEM_FAST4, (cb_fast4.isChecked() ? 1 : 0));

        super.finish();
    }
}
