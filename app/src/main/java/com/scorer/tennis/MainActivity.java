
package com.scorer.tennis;

// From pre WiFi version 15/08/2016

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Process;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scorer.tennis.R.id;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

import static android.os.SystemClock.sleep;
import static com.scorer.tennis.Config.context;
import static com.scorer.tennis.GlobalClass.getClub;
import static com.scorer.tennis.GlobalClass.getInc;
import static com.scorer.tennis.GlobalClass.getSSID;
import static com.scorer.tennis.GlobalClass.getStartTesting;
import static com.scorer.tennis.GlobalClass.getTest;
import static com.scorer.tennis.GlobalClass.getThresh;
import static com.scorer.tennis.GlobalClass.setChannel;
import static com.scorer.tennis.GlobalClass.setClub;
import static com.scorer.tennis.GlobalClass.setInc;
import static com.scorer.tennis.GlobalClass.setSSID;
import static com.scorer.tennis.GlobalClass.setTest;
import static com.scorer.tennis.GlobalClass.setThresh;


public class MainActivity extends Activity {

    private AlertDialog ad = null;
    private DBAdapter myDb;
    private TextView t;

    private int c_points_h;         // current counters
    private int c_points_v;
    private int c_games_h;
    private int c_games_v;
    private int c_sets_h;
    private int c_sets_v;

    private int h_points_h;       // historic set of counters
    private int h_points_v;
    private int h_games_h;
    private int h_games_v;
    private int h_sets_h;
    private int h_sets_v;

    private String server = "Z";
    private String h_server;
    private String strPlayerButton = "Z";

    private boolean matchComplete = false;

    // Timers
    private resumeTimer res_counter;
    private boolean res_timerActive = false;

    private flashTimer flash_counter;

    private wifiTimer wifi_counter;
    private String WifiSSID;
    private boolean connect_in_progress = false;

    public boolean wifiConnected = false;
    public boolean wifiEnabled = false;
    private Socket socket;

    private SendThread sendThread;
    private boolean stopSendThread = false;

    private ReceiveThread receiveThread;
    private boolean stopRecvThread = false;

    private boolean stopRecv = false;

    private DataOutputStream dataOut;
    private BufferedReader dataIn;

    private buttonTimer button_counter;

    private batteryTimer battery_counter;
    private int batteryTestLevel;
    private int batteryMessageNo = 5;

    private ImageView i;

    private boolean boolTieBreakGame = false;
    private boolean boolLastSet = false;
    private boolean boolFlipFlag = false;
    private int intFlipCntr = 2;

    private int intNoOfGamesInSet = 0;
    private int intMinGamesToWinSet = 0;
    private int intNextToLastSet = 0;
    private int intSetsToWin = 0;


//    private int int_mtb_points;

    private boolean boolAdvSetType = true;
    private boolean boolAdvLastSet = true;
    private boolean boolNoAdv = false;
    //    private boolean boolMatchTb;
    //    private boolean boolFast4;
    private boolean boolShortSets = false;

// ******************************************************************************

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(MainActivity.this, SplashActivity.class);
        startActivity(intent);
        setContentView(R.layout.activity_main);

        context = this;
        myDb = new DBAdapter(this);
        myDb.open();

        if (!getClubName()) {
            System.exit(0);
        }
        setGlobals();

        myDb.K_Log("Start App");

        start_ResumeTimer();    // Delay onResume() for things to settle

        batteryTestLevel = getThresh();
        start_batteryTimer();

        WifiSSID = "scorer_" + getSSID();

        start_wifiTimer();
    }

// ******************************************************************************

    @Override
    public void onBackPressed() {
    }

// ******************************************************************************

    @Override
    protected void onDestroy() {

        myDb.K_Log("Close App");
        myDb.close();

        stop_batteryTimer();
        stop_wifiTimer();

        stopSendThread = true;
        stopRecvThread = true;

        super.onDestroy();
    }

// ******************************************************************************

    private void setGlobals() {

        setThresh(myDb.readSystem(DBAdapter.KEY_SYSTEM_BATT_THRESH));
        setInc(myDb.readSystem(DBAdapter.KEY_SYSTEM_BATT_INC));

        setSSID(myDb.readSystem(DBAdapter.KEY_SYSTEM_SSID));
        setChannel(myDb.readSystem(DBAdapter.KEY_SYSTEM_CHANNEL));

    }
// ******************************************************************************

    @Override
    protected void onResume() {
        super.onResume();

        if (res_timerActive) return;

        myDb.close();
        myDb.open();

        myDb.K_Log("Resume App");

        setupVariables();

        // *********** Club
        t = (TextView) findViewById(id.club);
        t.setText(getClub());

        // *********** Player Names

        String s = myDb.readSystemStr(DBAdapter.KEY_SYSTEM_NAME_A);
        t = (TextView) findViewById(id.name_a);
        t.setText(s);

        t = (TextView) findViewById(id.A_Team);
        t.setText(s);

        s = myDb.readSystemStr(DBAdapter.KEY_SYSTEM_NAME_B);
        t = (TextView) findViewById(id.name_b);
        t.setText(s);

        t = (TextView) findViewById(id.B_Team);
        t.setText(s);

        // *********** Scores

        s = myDb.readSystemStr(DBAdapter.KEY_SYSTEM_POINTS_A);
        t = (TextView) findViewById(id.points_a);
        if (!boolTieBreakGame) {
            c_points_h = Integer.valueOf(s);
            s = convert_Points(c_points_h);
        }
        t.setText(s);

        s = myDb.readSystemStr(DBAdapter.KEY_SYSTEM_POINTS_B);
        t = (TextView) findViewById(id.points_b);
        if (!boolTieBreakGame) {
            c_points_v = Integer.valueOf(s);
            s = convert_Points(c_points_v);
        }
        t.setText(s);

        s = myDb.readSystemStr(DBAdapter.KEY_SYSTEM_GAMES_A);
        t = (TextView) findViewById(id.games_a);
        t.setText(s);
        c_games_h = Integer.valueOf(s);

        s = myDb.readSystemStr(DBAdapter.KEY_SYSTEM_GAMES_B);
        t = (TextView) findViewById(id.games_b);
        t.setText(s);
        c_games_v = Integer.valueOf(s);

        s = myDb.readSystemStr(DBAdapter.KEY_SYSTEM_SETS_A);
        t = (TextView) findViewById(id.sets_a);
        t.setText(s);
        c_sets_h = Integer.valueOf(s);

        s = myDb.readSystemStr(DBAdapter.KEY_SYSTEM_SETS_B);
        t = (TextView) findViewById(id.sets_b);
        t.setText(s);
        c_sets_v = Integer.valueOf(s);

        // Server

        server = myDb.readSystemStr(DBAdapter.KEY_SYSTEM_SERVER);

        if (server == null) {
            server = "Z";
        }

        switch (server) {
            case "H":
                t = (TextView) findViewById(id.server_mark_a);
                t.setVisibility(View.VISIBLE);

                t = (TextView) findViewById(id.server_mark_b);
                t.setVisibility(View.INVISIBLE);

                break;
            case "V":
                t = (TextView) findViewById(id.server_mark_a);
                t.setVisibility(View.INVISIBLE);

                t = (TextView) findViewById(id.server_mark_b);
                t.setVisibility(View.VISIBLE);

                break;

            default:
                t = (TextView) findViewById(id.server_mark_a);
                t.setVisibility(View.INVISIBLE);

                t = (TextView) findViewById(id.server_mark_b);
                t.setVisibility(View.INVISIBLE);
        }

        // Rules

//        // FAST4
//        t = (TextView) findViewById(id.f4);
//
//        if (myDb.readSystem(DBAdapter.KEY_SYSTEM_FAST4) == 1) {
//            boolFast4 = true;
//            t.setText("FAST4");
//        } else {
//            boolFast4 = false;
//            t.setText("");
//        }
//
//
//        if (!boolFast4) {
//            rules_visible();
//        } else {
//            rules_invisible();
//
//        }

//        // Match Tie Break
//        t = (TextView) findViewById(id.mtb);
//
//        if (myDb.readSystem(DBAdapter.KEY_SYSTEM_MATCH_TB) == 1) {
//            boolMatchTb = true;
//
//            if (myDb.readSystem(DBAdapter.KEY_SYSTEM_MTB_7) == 1) {
//                t.setText("Match Tie Break 7 Points");
//            }
//
//            if (myDb.readSystem(DBAdapter.KEY_SYSTEM_MTB_10) == 1) {
//                t.setText("Match Tie Break 10 Points");
//            }
//        } else {
//            boolMatchTb = false;
//
//            t.setText("");
//        }

        // Short Sets
        t = (TextView) findViewById(id.ss);

        if (boolShortSets) {
            t.setText(R.string.shortsets);
        } else {
            t.setText("");
        }

        // Sudden Death Deuce
        t = (TextView) findViewById(id.no_adv);

        if (boolNoAdv) {
            t.setText(R.string.suddeath);
        } else {
            t.setText("");
        }

        // No of Sets
        t = (TextView) findViewById(id.set_no);

        String setNo = "";

        if (intNoOfGamesInSet == 1) {
            setNo = "1 Set";
        }

        if (intNoOfGamesInSet == 3) {
            setNo = "3 Sets";
        }

        if (intNoOfGamesInSet == 5) {
            setNo = "5 Sets";
        }

        t.setText(setNo);

        // Tie Break Sets
        t = (TextView) findViewById(id.tb);

        if (boolAdvSetType) {
            t.setText(R.string.adv_sets);
        } else {
            t.setText(R.string.tb_sets);
        }

        // Last Set
        t = (TextView) findViewById(id.last_tb);

        if (!boolAdvSetType) {
            if (boolAdvLastSet) {
                t.setText(R.string.adv_l_set);
            } else {
                t.setText(R.string.tb_l_set);
            }
        } else {
            t.setText(" ");
        }
    }

//// ******************************************************************************
//
//    private void rules_visible() {
//        t = (TextView) findViewById(id.mtb);
//        t.setVisibility(View.VISIBLE);
//
//        t = (TextView) findViewById(id.ss);
//        t.setVisibility(View.VISIBLE);
//
//        t = (TextView) findViewById(id.set_no);
//        t.setVisibility(View.VISIBLE);
//
//        t = (TextView) findViewById(id.no_adv);
//        t.setVisibility(View.VISIBLE);
//
//        t = (TextView) findViewById(id.tb);
//        t.setVisibility(View.VISIBLE);
//
//        t = (TextView) findViewById(id.last_tb);
//        t.setVisibility(View.VISIBLE);
//
//    }
//
//// ******************************************************************************
//
//    private void rules_invisible() {
//        t = (TextView) findViewById(id.mtb);
//        t.setVisibility(View.INVISIBLE);
//
//        t = (TextView) findViewById(id.ss);
//        t.setVisibility(View.INVISIBLE);
//
//        t = (TextView) findViewById(id.set_no);
//        t.setVisibility(View.INVISIBLE);
//
//        t = (TextView) findViewById(id.no_adv);
//        t.setVisibility(View.INVISIBLE);
//
//        t = (TextView) findViewById(id.tb);
//        t.setVisibility(View.INVISIBLE);
//
//        t = (TextView) findViewById(id.last_tb);
//        t.setVisibility(View.INVISIBLE);
//
//    }

// ******************************************************************************

    private boolean getClubName() {
        StringBuilder strString;
        File myFile = new File(getString(R.string.sdcard));

//            <string name="sdcard">/sdcard/f87297.azc</string>



        try {
            FileInputStream fIn = new FileInputStream(myFile);

            BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
            String aDataRow;
            strString = new StringBuilder();
            while ((aDataRow = myReader.readLine()) != null) {
                strString.append(aDataRow).append("\n");
            }
            myReader.close();
            if (!strString.toString().contains("Pa6gK3")) {
                return false;
            }
            int intOffset = strString.charAt(1) - 48;
            strString = new StringBuilder(strString.substring(8));
            strString = new StringBuilder(strString.substring(0, strString.length() - 4));
            strString = new StringBuilder(new StringBuffer(strString.toString()).reverse().toString());
            int i = 0;
            StringBuilder strTemp = new StringBuilder();
            while (i < strString.length()) {
                char c = strString.charAt(i);
                c = (char) (c - intOffset);
                strTemp.append(c);
                i++;
            }
            strString = new StringBuilder(strTemp.toString());
            setClub(strString.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

// ******************************************************************************

    public String convert_Points(int points) {
        String ss;

        if (!boolTieBreakGame) {
            switch (points) {
                case 0:
                    ss = "00";
                    break;
                case 1:
                    ss = "15";
                    break;
                case 2:
                    ss = "30";
                    break;
                case 3:
                    ss = "40";
                    break;
                default:
                    ss = "AD";
            }
        } else {
            ss = Integer.toString(points);
        }

        return ss;
    }

// ******************************************************************************

    public String convert_Points_Trans(int points) {
        String ss;

        if (!boolTieBreakGame) {
            switch (points) {
                case 0:
                    ss = "00";
                    break;
                case 1:
                    ss = "15";
                    break;
                case 2:
                    ss = "30";
                    break;
                case 3:
                    ss = "40";
                    break;
                default:
                    ss = "AD";
            }
        } else {
            ss = Integer.toString(points) + "B";
            if (ss.length() > 2) {
                ss = ss.substring(0, 2);

                ss = ss.substring(1, 2) + ss.substring(0, 1);
            }
        }
        return ss;
    }

// ******************************************************************************
// Buttons in Action Bar
// ******************************************************************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // ******************************************************************************

    public void onClick_Reset(View view) {

        myDb.K_Log("Reset");
        resetAlertDialog();
    }

// ******************************************************************************

    public void onClick_Other(View view) {

        Intent intent = new Intent(MainActivity.this, OtherActivity.class);
        startActivity(intent);
        myDb.K_Log("Open Other Menu");
    }

// ******************************************************************************

    public void onClick_Exit(View view) {

        myDb.K_Log("Quit");
        quitAlertDialog();
    }

// ******************************************************************************

    public void onClick_name_a(View view) {

        Intent intent = new Intent(MainActivity.this, Name_A_Activity.class);
        startActivity(intent);

        myDb.K_Log("Open Name A Window");
    }

// ******************************************************************************

    public void onClick_name_b(View view) {

        Intent intent = new Intent(this, Name_B_Activity.class);
        startActivity(intent);
        myDb.K_Log("Open Name B Window");
    }

// ******************************************************************************

    public void onClick_server_a(View view) {

        server = "H";
        myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_SERVER, server);

        t = (TextView) findViewById(id.server_mark_a);
        t.setVisibility(View.VISIBLE);
        t = (TextView) findViewById(id.server_mark_b);
        t.setVisibility(View.INVISIBLE);
    }

// ******************************************************************************

    public void onClick_server_b(View view) {

        server = "V";
        myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_SERVER, server);

        t = (TextView) findViewById(id.server_mark_a);
        t.setVisibility(View.INVISIBLE);
        t = (TextView) findViewById(id.server_mark_b);
        t.setVisibility(View.VISIBLE);
    }

// ******************************************************************************

    public void flip_server() {

        switch (server) {
            case "H":
                server = "V";

                myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_SERVER, server);

                t = (TextView) findViewById(id.server_mark_a);
                t.setVisibility(View.INVISIBLE);

                t = (TextView) findViewById(id.server_mark_b);
                t.setVisibility(View.VISIBLE);
                break;

            case "V":

                server = "H";

                myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_SERVER, server);

                t = (TextView) findViewById(id.server_mark_a);
                t.setVisibility(View.VISIBLE);

                t = (TextView) findViewById(id.server_mark_b);
                t.setVisibility(View.INVISIBLE);

                break;

            default:
        }
    }

// ******************************************************************************

    public void pointsPlus_a() {
        if (!matchComplete) {
            if (serverSet()) {
                Buttons_Off("H");
                history();

                c_points_h++;
                CalculatePoints();
            }
        }
    }

// ******************************************************************************

    public void pointsPlus_b() {
        if (!matchComplete) {
            if (serverSet()) {
                Buttons_Off("V");
                history();

                c_points_v++;
                CalculatePoints();
            }
        }
    }

// ******************************************************************************

    public void onClick_pointsPlus_a(View view) {
        if (!matchComplete) {
            if (serverSet()) {
                Buttons_Off("H");
                history();

                c_points_h++;
                CalculatePoints();
            }
        }
    }

// ******************************************************************************

    public void onClick_pointsNeg_a(View view) {
        if (!matchComplete) {
            if (serverSet()) {
                t = (TextView) findViewById(id.b_pointsNeg_a);
                t.setVisibility(View.INVISIBLE);

                restore();
                CalculatePoints();
            }
        }
    }

// ******************************************************************************

    public void onClick_pointsPlus_b(View view) {
        if (!matchComplete) {
            if (serverSet()) {

                Buttons_Off("V");

                history();

                c_points_v++;
                CalculatePoints();
            }
        }
    }

// ******************************************************************************

    public void onClick_pointsNeg_b(View view) {
        if (!matchComplete) {
            if (serverSet()) {

                t = (TextView) findViewById(id.b_pointsNeg_b);
                t.setVisibility(View.INVISIBLE);

                restore();
                CalculatePoints();
            }
        }
    }

// ******************************************************************************

    public boolean serverSet() {
        switch (server) {
            case "Z":
                Toast toast = Toast.makeText(getApplicationContext(), "Cannot score until 1st. " +
                        "Server is set.", Toast.LENGTH_SHORT);
                toast.show();

                return false;
        }
        return true;
    }

// ******************************************************************************

    public void Buttons_Off(String player) {

        strPlayerButton = player;

        start_ButtonTimer();

        t = (TextView) findViewById(id.b_pointsPlus_a);
        t.setVisibility(View.INVISIBLE);

        t = (TextView) findViewById(id.b_pointsPlus_b);
        t.setVisibility(View.INVISIBLE);

        t = (TextView) findViewById(id.b_pointsNeg_a);
        t.setVisibility(View.INVISIBLE);

        t = (TextView) findViewById(id.b_pointsNeg_b);
        t.setVisibility(View.INVISIBLE);
    }

// ******************************************************************************

    public void AllButtonsOff() {

        stop_ButtonTimer();

        t = (TextView) findViewById(id.b_pointsPlus_a);
        t.setVisibility(View.INVISIBLE);

        t = (TextView) findViewById(id.b_pointsPlus_b);
        t.setVisibility(View.INVISIBLE);

        t = (TextView) findViewById(id.b_pointsNeg_a);
        t.setVisibility(View.INVISIBLE);

        t = (TextView) findViewById(id.b_pointsNeg_b);
        t.setVisibility(View.INVISIBLE);
    }

// ******************************************************************************

    public void ButtonsOn() {

        t = (TextView) findViewById(id.b_pointsPlus_a);
        t.setVisibility(View.VISIBLE);

        t = (TextView) findViewById(id.b_pointsPlus_b);
        t.setVisibility(View.VISIBLE);

        switch (strPlayerButton) {
            case "H":
                t = (TextView) findViewById(id.b_pointsNeg_a);
                t.setVisibility(View.VISIBLE);
                break;
            case "V":
                t = (TextView) findViewById(id.b_pointsNeg_b);
                t.setVisibility(View.VISIBLE);
        }
    }

// ******************************************************************************

    private void history() {

        h_points_h = c_points_h;
        h_points_v = c_points_v;
        h_games_h = c_games_h;
        h_games_v = c_games_v;
        h_sets_h = c_sets_h;
        h_sets_v = c_sets_v;

        h_server = server;
    }

// ******************************************************************************

    private void restore() {
        c_points_h = h_points_h;
        c_points_v = h_points_v;
        c_games_h = h_games_h;
        c_games_v = h_games_v;
        c_sets_h = h_sets_h;
        c_sets_v = h_sets_v;

        server = h_server;

        myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_SERVER, server);
    }

// ******************************************************************************

    private void setupVariables() {

//        int intSetsForDeuce = 0;

        if (myDb.readSystem(DBAdapter.KEY_SYSTEM_SET_NO_1) == 1) {
            intNoOfGamesInSet = 1;
            intSetsToWin = 1;
            intNextToLastSet = 0;
//            intSetsForDeuce = 0;
        }

        if (myDb.readSystem(DBAdapter.KEY_SYSTEM_SET_NO_3) == 1) {
            intNoOfGamesInSet = 3;
            intSetsToWin = 2;
            intNextToLastSet = 1;
//            intSetsForDeuce = 1;
        }

        if (myDb.readSystem(DBAdapter.KEY_SYSTEM_SET_NO_5) == 1) {
            intNoOfGamesInSet = 5;
            intSetsToWin = 3;
            intNextToLastSet = 2;
//            intSetsForDeuce = 2;
        }

        boolShortSets = (myDb.readSystem(DBAdapter.KEY_SYSTEM_SHORT_SETS) == 1);

        if (boolShortSets) {
            intMinGamesToWinSet = 4;
        } else {
            intMinGamesToWinSet = 6;
        }

        boolNoAdv = (myDb.readSystem(DBAdapter.KEY_SYSTEM_NO_ADV) == 1);

        boolAdvSetType = (myDb.readSystem(DBAdapter.KEY_SYSTEM_SET_TYPE) == 0);

        if (!boolAdvSetType) {
            boolAdvLastSet = (myDb.readSystem(DBAdapter.KEY_SYSTEM_LAST_SET) == 0);
        }
    }


// ******************************************************************************

    private void CalculatePoints() {

        if (c_points_h < 0) c_points_h = 0;
        if (c_points_v < 0) c_points_v = 0;

        if (!boolTieBreakGame) {

            // Normal Game

            int res_arr[][] = {{0, 0, 0, 0, 1, 5}, {0, 0, 0, 0, 1, 5}, {0, 0, 0, 0, 1, 5}, {0, 0, 0, 2,
                    3, 1}, {1, 1, 1, 3, 4, 5}, {5, 5, 5, 1, 5, 5}};

            // 0 - Add to score
            // 1 - Win game
            // 2 - Deuce
            // 3 - Advantage
            // 4 - Back to deuce
            // 5 - No action

            int result = res_arr[c_points_h][c_points_v];

            switch (result) {
                case 0:
                    break;
                case 1:
                    Games("Adv");
                    break;
                case 2:
                    Deuce();
                    break;
                case 3:
                    if (boolNoAdv) {
                        Games("Adv");
                    } else {
                        Advantage();
                    }
                    break;
                case 4:
                    Back_to_Deuce();
                    break;
                case 5:
                    break;
            }

        } else {

            // Tie Break Game

            if ((c_points_h >= 7) && (c_points_v <= (c_points_h - 2))) {
                boolFlipFlag = false;
                intFlipCntr = 2;

                Games("Tb");
            }

            if ((c_points_v >= 7) && (c_points_h <= (c_points_v - 2))) {
                boolFlipFlag = false;
                intFlipCntr = 2;

                Games("Tb");
            }

            if (!boolFlipFlag) {
                boolFlipFlag = true;

                flip_server();
            } else {
                intFlipCntr--;
                if (intFlipCntr == 0){
                    intFlipCntr = 2;

                    flip_server();
                }
            }
        }

        myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_POINTS_A, String.valueOf(c_points_h));
        myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_POINTS_B, String.valueOf(c_points_v));

        myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_GAMES_A, String.valueOf(c_games_h));
        myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_GAMES_B, String.valueOf(c_games_v));

        myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_SETS_A, String.valueOf(c_sets_h));
        myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_SETS_B, String.valueOf(c_sets_v));

        onResume();
    }

// ******************************************************************************

    private void Games(String gameType) {

        if (c_points_h > c_points_v) {
            c_games_h++;
        } else {
            c_games_v++;
        }
        c_points_h = 0;
        c_points_v = 0;

        flip_server();

        Sets(gameType);

        NextGame();
    }

// ******************************************************************************

    private void Sets(String gameType) {

        if (Objects.equals(gameType, "Adv")) {

            // Advantage Game

            if ((c_games_h < intMinGamesToWinSet) && (c_games_v < intMinGamesToWinSet)) {
                Intent intent = new Intent(MainActivity.this, GameSplashActivity.class);
                startActivity(intent);

                return;
            }

            if ((c_games_h >= intMinGamesToWinSet) && (c_games_v <= (c_games_h - 2))) {
                c_sets_h++;
                c_games_h = 0;
                c_games_v = 0;

                if (!Match()) {
                    Intent intent = new Intent(MainActivity.this, SetSplashActivity.class);
                    startActivity(intent);
                }
                return;
            }

            if ((c_games_v >= intMinGamesToWinSet) && (c_games_h <= (c_games_v - 2))) {
                c_sets_v++;
                c_games_h = 0;
                c_games_v = 0;

                if (!Match()) {
                    Intent intent = new Intent(MainActivity.this, SetSplashActivity.class);
                    startActivity(intent);
                }
            }

        } else {

            // Tie Break Set

            if (boolLastSet) {
                if (c_games_h > c_games_v) {
                    c_sets_h++;
                } else {
                    c_sets_v++;
                }

                Match();

            } else {
                Intent intent = new Intent(MainActivity.this, SetSplashActivity.class);
                startActivity(intent);
            }
            c_games_h = 0;
            c_games_v = 0;
        }
    }

// ******************************************************************************

    private boolean Match() {

        if (c_sets_h == intSetsToWin || c_sets_v == intSetsToWin) {

            Intent intent = new Intent(MainActivity.this, MatchSplashActivity.class);
            startActivity(intent);

            AllButtonsOff();

            matchComplete = true;

            return true;
        }
        return false;
    }

// ******************************************************************************

    private void NextGame() {
        boolTieBreakGame = false;           // Advantage Game
        GameNotice(false);

        if (!boolAdvSetType) {               // Advantage Game
                                            // Tie Break
            if ((c_games_h == intMinGamesToWinSet) && (c_games_v == intMinGamesToWinSet)) {
                if (!boolAdvLastSet) {
                    boolTieBreakGame = true;
                    GameNotice(true);
                }
                if ((c_sets_h == intNextToLastSet) && (c_sets_v == intNextToLastSet)) {
                    boolLastSet = true;
                }
            }
        }
    }

// ******************************************************************************

    private void GameNotice(boolean type) {             // 'true' for Tie Brea
//       if (!type) {
//            t = (TextView) findViewById(id.advantage);          // Temporary to remove "Advantage"
//            t.setVisibility(View.VISIBLE);
//
//            t = (TextView) findViewById(id.tie_break);
//            t.setVisibility(View.INVISIBLE);
//        } else {
//            t = (TextView) findViewById(id.advantage);
//            t.setVisibility(View.INVISIBLE);
//
//            t = (TextView) findViewById(id.tie_break);
//            t.setVisibility(View.VISIBLE);
//        }
    }

// ******************************************************************************

    private void Deuce() {

        Intent intent = new Intent(MainActivity.this, DeuceSplashActivity.class);
        startActivity(intent);
    }

// ******************************************************************************

    private void Advantage() {

        Intent intent = new Intent(MainActivity.this, AdvSplashActivity.class);
        startActivity(intent);
    }

// ******************************************************************************

    private void Back_to_Deuce() {

        Intent intent = new Intent(MainActivity.this, DeuceSplashActivity.class);
        startActivity(intent);

        c_points_h--;
        c_points_v--;
    }
// ******************************************************************************

    private void resetAlertDialog() {

        Context context = MainActivity.this;
        String message = "You REALLY want to Reset everything?";
        String button1String = "Reset";
        String button2String = "Cancel";
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setMessage(message);
        ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {

                myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_NAME_A, "Home");
                myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_NAME_B, "Visitors");

                myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_POINTS_A, "0");
                myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_POINTS_B, "0");

                myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_GAMES_A, "0");
                myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_GAMES_B, "0");

                myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_SETS_A, "0");
                myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_SETS_B, "0");

                t = (TextView) findViewById(id.b_pointsNeg_a);
                t.setVisibility(View.INVISIBLE);
                t = (TextView) findViewById(id.b_pointsNeg_b);
                t.setVisibility(View.INVISIBLE);

                t = (TextView) findViewById(id.server_mark_a);
                t.setVisibility(View.INVISIBLE);
                t = (TextView) findViewById(id.server_mark_b);
                t.setVisibility(View.INVISIBLE);

                server = "Z";
                myDb.updateSystemStr(DBAdapter.KEY_SYSTEM_SERVER, server);

                boolTieBreakGame = false;

                matchComplete = false;

                t = (TextView) findViewById(id.tie_break);
                t.setVisibility(View.INVISIBLE);

                Intent s_intent = new Intent(MainActivity.this, RulesActivity.class);
                startActivity(s_intent);

                strPlayerButton = "Z";
                ButtonsOn();

                myDb.K_Log("Reset Yes");
                onResume();
            }
        });
        ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                // do nothing
                myDb.K_Log("Reset No");
            }
        });
        ad.show();
    }

// ******************************************************************************

    private void quitAlertDialog() {

        Context context = MainActivity.this;
        String message = "You REALLY want to Quit?";
        String button1String = "Quit";
        String button2String = "Cancel";
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setMessage(message);
        ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {

                myDb.K_Log("Quit App");
                finish();
            }
        });
        ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                // do nothing
                myDb.K_Log("Quit Not");
            }
        });
        ad.show();
    }

// ******************************************************************************
// Timer to delay running onResume on start up
// ******************************************************************************

    public void start_ResumeTimer() {

        if (!res_timerActive) {
            res_timerActive = true;
            res_counter = new resumeTimer(500, 500);
            res_counter.start();
        }
    }

// ******************************************************************************

    private class resumeTimer extends CountDownTimer {

        private resumeTimer(long res_millisInFuture, long countDownInterval) {

            super(res_millisInFuture, countDownInterval);
        }

        // ******************************************************************************

        @Override
        public void onFinish() {

            onResume();
            res_counter = null;
            res_timerActive = false;
        }

        // ******************************************************************************

        @Override
        public void onTick(long millisUntilFinished) {

        }
    }

// ******************************************************************************
// Timer to show scoring buttons after a delay
// ******************************************************************************

    public void start_ButtonTimer() {

        button_counter = new buttonTimer(500, 1000);
        button_counter.start();
    }

// ******************************************************************************

    public void stop_ButtonTimer() {

        if (button_counter != null) {
            button_counter.cancel();
        }
    }

// ******************************************************************************

    private class buttonTimer extends CountDownTimer {

        buttonTimer(long button_millisInFuture, long countDownInterval) {

            super(button_millisInFuture, countDownInterval);
        }

        // ******************************************************************************

        @Override
        public void onFinish() {

            ButtonsOn();
            button_counter = null;
        }

        // ******************************************************************************

        @Override
        public void onTick(long millisUntilFinished) {

        }
    }

// ******************************************************************************
// Timer for repetitive battery level tests
// ******************************************************************************

    public void start_batteryTimer() {

        if (battery_counter != null) {
            battery_counter.cancel();
        }

        battery_counter = new batteryTimer(30000, 30000);
        battery_counter.start();
    }

// ******************************************************************************

    public void stop_batteryTimer() {

        if (battery_counter != null) {
            battery_counter.cancel();
        }
    }

// ******************************************************************************

    private class batteryTimer extends CountDownTimer {

        batteryTimer(long battery_millisInFuture, long countDownInterval) {

            super(battery_millisInFuture, countDownInterval);
        }

        // ******************************************************************************

        @Override
        public void onFinish() {

            battery_counter = null;
            start_batteryTimer();
            /*
              Computes the battery level by registering a receiver to the intent triggered
              by a battery status/level change.
             */
            BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {

                    context.unregisterReceiver(this);
                    int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                    int level;
                    if (rawlevel >= 0 && scale > 0) {
                        level = (rawlevel * 100) / scale;
                        checkBattery(level);
                    }
                }
            };
            IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            registerReceiver(batteryLevelReceiver, batteryLevelFilter);
        }

        // ******************************************************************************

        @Override
        public void onTick(long millisUntilFinished) {
        }
    }

// ******************************************************************************

    private void checkBattery(int level) {

        String mess = "";
        if (level <= batteryTestLevel) {
            batteryTestLevel = batteryTestLevel - getInc();
            switch (batteryMessageNo) {
                case 5:
                    mess = "The Battery is getting low.";
                    break;
                case 4:
                    mess = "The Battery is getting lower.";
                    break;
                case 3:
                    mess = "The Battery is getting lower and lower.";
                    break;
                case 2:
                    mess = "The Battery is getting very low.";
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.screenBrightness = 20 / 100.0f;
                    getWindow().setAttributes(lp);
                    break;
                case 1:
                    mess = "Scorer will SHUT DOWN in 30 seconds";
                    batteryTestLevel = batteryTestLevel + getInc();
                    break;
                case 0:
                    android.os.Process.killProcess(Process.myPid());
                    System.exit(0);
                    finish();
                    break;
            }
            myDb.K_Log("Battery Message - " + mess);
            batteryAlertDialog(mess, Integer.toString(batteryMessageNo));
            batteryMessageNo--;
        }
    }

// ******************************************************************************

    private void batteryAlertDialog(String message, String no) {

        if (ad != null) {
            ad.cancel();
            Alarm.stopAlarm();
        }
        Alarm.soundAlarm(context);
        myDb.K_Log(message + "  " + no);
        Context context = MainActivity.this;
        String button1String = "Acknowledge";
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setMessage(message);
        adb.setTitle(no);
        adb.setPositiveButton(button1String, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {

                Alarm.stopAlarm();
            }
        });
        ad = adb.create();
        ad.show();
    }


// ***************************WiFiWiFiWiFiWiFiWiFiWiFi*********************************************
// WiFi
// ***************************WiFiWiFiWiFiWiFiWiFiWiFi*********************************************

    private boolean checkWiFiEnabled() {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);

        if (wifi != null) {
            if (!wifi.isWifiEnabled()) {
                t = (TextView) findViewById(id.wifi_message);
                t.setTextColor(ContextCompat.getColor(this, R.color.red));
                t.setText(R.string.wifi_disabled);

                t = (TextView) findViewById(id.wifi_connected);
                t.setTextColor(ContextCompat.getColor(this, R.color.red));
                t.setText(R.string.wifi_not_connected);

                wifi.setWifiEnabled(true);
                wifiEnabled = false;

                return false;
            } else {
                t = (TextView) findViewById(id.wifi_message);
                t.setTextColor(ContextCompat.getColor(this, R.color.dk_green));
                t.setText(R.string.wifi_enabled);
                wifiEnabled = true;

                return true;
            }
        }
        return false;
    }
    // ******************************************************************************

    private boolean checkWiFiConnected() {
        final WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (cm != null) {
            networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        }
        WifiInfo wifiInfo = wifi.getConnectionInfo();

        if (!(networkInfo.isConnected() && wifiInfo.getSSID().replace("\"", "").equals(WifiSSID))) {
            t = (TextView) findViewById(id.wifi_connected);
            t.setTextColor(ContextCompat.getColor(this, R.color.red));
            t.setText(R.string.wifi_not_connected);
            stopSendThread = true;
            stopRecvThread = true;

            sendThread = null;
            receiveThread = null;

            stop_flashTimer();

            wifiConnected = false;

            return false;
        } else {
            t = (TextView) findViewById(id.wifi_connected);
            t.setTextColor(ContextCompat.getColor(this, R.color.dk_green));
            t.setText(R.string.wifi_connected);
            stopSendThread = false;
            stopRecvThread = false;

            if (sendThread == null) {
                sendThread = new SendThread();
                new Thread(sendThread).start();
            }
            if (receiveThread == null) {
                receiveThread = new ReceiveThread();
                new Thread(receiveThread).start();
            }

            start_flashTimer();

            wifiConnected = true;

            return true;
        }
    }

// ******************************************************************************

    /**
     * Start to connect to a specific wifi network
     */
    private void connectToSpecificNetwork() {
        if (!connect_in_progress) {

            final WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);

            ScanReceiver scanReceiver = new ScanReceiver();
            registerReceiver(scanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

            if (wifi != null) {
                wifi.startScan();
            }

            connect_in_progress = true;
        }
    }

// ******************************************************************************

    /**
     * Broadcast receiver for wifi scanning related events
     */
    private class ScanReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
            List<ScanResult> scanResultList = null;
            if (wifi != null) {
                scanResultList = wifi.getScanResults();
            }
            boolean found = false;
            for (ScanResult scanResult : scanResultList) {
                if (scanResult.SSID.equals(WifiSSID)) {
                    found = true;
                    break;                  // found don't need continue
                }
            }
            if (!found) {
                unregisterReceiver(ScanReceiver.this);
                connect_in_progress = false;

            } else {
                // configure based on security
                final WifiConfiguration conf = new WifiConfiguration();
                conf.SSID = "\"" + WifiSSID + "\"";

//                String SS = padLeftZero("" + getSSID(), 2);
                String SS = "" + getSSID();

                String wifiPass = "1" + SS + "2" + SS + "3" + SS + "4";
                conf.preSharedKey = "\"" + wifiPass + "\"";

                int netId = wifi.addNetwork(conf);
                wifi.disconnect();
                wifi.enableNetwork(netId, true);
                wifi.reconnect();

                unregisterReceiver(this);
            }
        }
    }

// ******************************************************************************

    private void wifi_enable_off() {
        t = (TextView) findViewById(id.wifi_message);
        t.setTextColor(ContextCompat.getColor(this, R.color.red));
        t.setText(R.string.wifi_disabled);

        t = (TextView) findViewById(id.wifi_connected);
        t.setTextColor(ContextCompat.getColor(this, R.color.red));
        t.setText(R.string.wifi_not_connected);
    }

// ******************************************************************************
//      Send Thread
// ******************************************************************************

    private class SendThread implements Runnable {
        SendThread() {
//        puts the thread in the background
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        }

        @Override
        public void run() {

            try {
                socket = new Socket("1.2.3.4", 2000);
            } catch (IOException e) {
                e.printStackTrace();
            }


            sleep(1000);

            while (!stopSendThread) {
                if (wifiEnabled) {
                    if (wifiConnected) {

                        try {
                            dataOut = new DataOutputStream(socket.getOutputStream());
                            dataOut.writeUTF(dataString());
                        } catch (IOException ignored) {
                        }

                        sleep(1000);
                    } else {
                        sleep(1000);
                    }
                } else {
                    sleep(1000);
                }
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (dataOut == null) {
                try {
                    dataOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

// ******************************************************************************
//      Receive Thread
// ******************************************************************************

    private class ReceiveThread implements Runnable {
        ReceiveThread() {
//        puts the thread in the background
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        }

        public void run() {

            char[] buff = new char[4];

            while (true) {
                if (!(socket == null)) break;
            }

            try {
                dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (!stopRecvThread) {
                try {
                    final int read = dataIn.read(buff, 0, 4);

                    String result = new String(buff);

                    if (result.substring(0, 3).equals("GJC")) {
                        String recvResult = result.substring(3, 4);

                        if (recvResult.equals("0")) {
                            stopRecv = false;
                        }
                        if (recvResult.equals("3") && !stopRecv) {
                            stopRecv = true;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pointsPlus_b();
                                }
                            });
                        }
                        if (recvResult.equals("2") && !stopRecv) {
                            stopRecv = true;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pointsPlus_a();
                                }
                            });
                        }
                    }
                } catch (IOException ignored) {
                }

                sleep(500);
            }

            if (socket != null) {
                try {
                    socket.close();
                    socket = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dataIn == null) {
                try {
                    dataIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

// ******************************************************************************

    private String dataString() {

        // Length of string should be a multple of 4 characters


        String s1;
        if (getStartTesting()) {
            if (getTest()) {


                setTest(false);
                s1 = "GJCXXXZ8888888888";
            } else {
                setTest(true);
                s1 = "GJCXXXZBBBBBBBBBB";
            }
        } else {
            s1 = "GJCXXX" //+ "Z8888888888";
                    + server
                    + String.valueOf(c_sets_v)
                    + String.valueOf(padLeftZero(c_games_v, 2))
                    + String.valueOf(convert_Points_Trans(c_points_v))
                    + String.valueOf(c_sets_h)
                    + String.valueOf(padLeftZero(c_games_h, 2))
                    + String.valueOf(convert_Points_Trans(c_points_h));
        }

        return s1;
    }

// ******************************************************************************
// Timer to check WiFi
// ******************************************************************************

    public void start_wifiTimer() {
        if (wifi_counter != null) {
            wifi_counter.cancel();
        }

        wifi_counter = new wifiTimer(2000, 1000);
        wifi_counter.start();
    }

// ******************************************************************************

    public void stop_wifiTimer() {

        if (wifi_counter != null) {
            wifi_counter.cancel();
        }
    }

// ******************************************************************************

    private class wifiTimer extends CountDownTimer {

        wifiTimer(long wifi_millisInFuture, long countDownInterval) {

            super(wifi_millisInFuture, countDownInterval);
        }

        // ******************************************************************************

        @Override
        public void onFinish() {
            if (checkWiFiEnabled()) {
                if (checkWiFiConnected()) {

                    connect_in_progress = false;

                } else {
                    connectToSpecificNetwork();
                }
            } else {
                wifi_enable_off();
            }
            wifi_counter = null;
            start_wifiTimer();
        }

        // ******************************************************************************

        @Override
        public void onTick(long millisUntilFinished) {
        }
    }

// ******************************************************************************
// Timer for flashing WiFi Box Connected
// ******************************************************************************

    private boolean flash = true;

    public void start_flashTimer() {

        if (flash_counter != null) {
            return;
        }
        flash_counter = new flashTimer(250, 250);
        flash_counter.start();

        flash = !flash;
    }

// ******************************************************************************

    public void stop_flashTimer() {

        if (flash_counter != null) {
            flash_counter.cancel();
            flash_counter = null;

            i = (ImageView) findViewById(R.id.wifi_dot);
            i.setVisibility(View.INVISIBLE);
        }
    }

// ******************************************************************************

    private class flashTimer extends CountDownTimer {
        flashTimer(long flash_millisInFuture, long countDownInterval) {
            super(flash_millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            flash_counter = null;

            if (flash) {
                i = (ImageView) findViewById(R.id.wifi_dot);
                i.setVisibility(View.VISIBLE);
            } else {
                i = (ImageView) findViewById(R.id.wifi_dot);
                i.setVisibility(View.INVISIBLE);
            }
            start_flashTimer();
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

    }

// ******************************************************************************

    public static String padLeftZero(int s, int n) {

        return String.format("%0" + n + "d", Integer.parseInt(String.valueOf(s)));
    }
}
