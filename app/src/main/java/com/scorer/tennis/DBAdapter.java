
package com.scorer.tennis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;


public class DBAdapter {

    // Database Name
    private static final String DATABASE_NAME = "Tennis_Score_Db";
    // ***********************************

    // Database Version
    private static final int DATABASE_VERSION = 4;
    // ***********************************

    // Table Names
    public static final String TABLE_SYSTEM = "system";
    public static final String TABLE_LOG = "log";
    // ***********************************

    // ***********************************

    // System Table - column names
    public static final String KEY_SYSTEM_ID = "_id";

    public static final String KEY_SYSTEM_CLUB = "system_club";

    public static final String KEY_SYSTEM_NAME_A = "system_name_a";
    public static final String KEY_SYSTEM_NAME_B = "system_name_b";

    public static final String KEY_SYSTEM_POINTS_A = "system_points_a";
    public static final String KEY_SYSTEM_POINTS_B = "system_points_b";

    public static final String KEY_SYSTEM_GAMES_A = "system_games_a";
    public static final String KEY_SYSTEM_GAMES_B = "system_games_b";

    public static final String KEY_SYSTEM_SETS_A = "system_sets_a";
    public static final String KEY_SYSTEM_SETS_B = "system_sets_b";

    public static final String KEY_SYSTEM_SERVER = "system_server";

    public static final String KEY_SYSTEM_SET_TYPE = "system_set_type";     // 0 - Adv  1 - Tb
    public static final String KEY_SYSTEM_LAST_SET = "system_last_set";     // 0 - Adv  1 - Tb

    public static final String KEY_SYSTEM_SET_NO_1 = "system_set_no_1";     // 0 - No  1 - Yes
    public static final String KEY_SYSTEM_SET_NO_3 = "system_set_no_3";
    public static final String KEY_SYSTEM_SET_NO_5 = "system_set_no_5";

    public static final String KEY_SYSTEM_BATT_THRESH = "system_batt_thresh";
    public static final String KEY_SYSTEM_BATT_INC = "system_batt_inc";

    public static final String KEY_SYSTEM_SSID = "system_ssid";
    public static final String KEY_SYSTEM_CHANNEL = "system_channel";

    public static final String KEY_SYSTEM_NO_ADV = "system_no_adv";          // 0 - No  1 - Yes
    public static final String KEY_SYSTEM_SHORT_SETS = "system_short_sets";  // 0 - No  1 - Yes
    public static final String KEY_SYSTEM_MATCH_TB = "system_match_tb";      // 0 - No  1 - Yes
    public static final String KEY_SYSTEM_MTB_7 = "system_mtb_7";            // 0 - No  1 - Yes
    public static final String KEY_SYSTEM_MTB_10 = "system_mtb_10";          // 0 - No  1 - Yes
    public static final String KEY_SYSTEM_FAST4 = "system_fast4";            // 0 - No  1 - Yes

    public static final String KEY_SYSTEM_SET_1_H = "system_set_1_h";
    public static final String KEY_SYSTEM_SET_1_V = "system_set_1_v";
    public static final String KEY_SYSTEM_SET_2_H = "system_set_2_h";
    public static final String KEY_SYSTEM_SET_2_V = "system_set_2_v";
    public static final String KEY_SYSTEM_SET_3_H = "system_set_3_h";
    public static final String KEY_SYSTEM_SET_3_V = "system_set_3_v";
    public static final String KEY_SYSTEM_SET_4_H = "system_set_4_h";
    public static final String KEY_SYSTEM_SET_4_V = "system_set_4_v";
    public static final String KEY_SYSTEM_SET_5_H = "system_set_5_h";
    public static final String KEY_SYSTEM_SET_5_V = "system_set_5_v";

    public static final String[] SYSTEM_KEYS = new String[]{KEY_SYSTEM_ID, KEY_SYSTEM_CLUB, KEY_SYSTEM_NAME_A, KEY_SYSTEM_NAME_B, KEY_SYSTEM_POINTS_A,
            KEY_SYSTEM_POINTS_B, KEY_SYSTEM_GAMES_A, KEY_SYSTEM_GAMES_B, KEY_SYSTEM_SETS_A, KEY_SYSTEM_SETS_B, KEY_SYSTEM_SERVER, KEY_SYSTEM_SET_TYPE,
            KEY_SYSTEM_LAST_SET, KEY_SYSTEM_SET_NO_1, KEY_SYSTEM_SET_NO_3, KEY_SYSTEM_SET_NO_5, KEY_SYSTEM_BATT_THRESH, KEY_SYSTEM_BATT_INC,
            KEY_SYSTEM_SSID, KEY_SYSTEM_CHANNEL, KEY_SYSTEM_NO_ADV, KEY_SYSTEM_SHORT_SETS, KEY_SYSTEM_MATCH_TB, KEY_SYSTEM_MTB_7, KEY_SYSTEM_MTB_10,
            KEY_SYSTEM_FAST4, KEY_SYSTEM_SET_1_H, KEY_SYSTEM_SET_1_V, KEY_SYSTEM_SET_2_H, KEY_SYSTEM_SET_2_V, KEY_SYSTEM_SET_3_H,
            KEY_SYSTEM_SET_3_V, KEY_SYSTEM_SET_4_H, KEY_SYSTEM_SET_4_V, KEY_SYSTEM_SET_5_H, KEY_SYSTEM_SET_5_V};

    // ***********************************

    // Log Table - column names
    public static final String KEY_LOG_ID = "_id";
    public static final String KEY_LOG_DATE = "log_date";
    public static final String KEY_LOG_TIME = "log_time";
    public static final String KEY_LOG_LOG = "log_log";

    public static final String[] LOG_KEYS = new String[]{KEY_LOG_ID, KEY_LOG_DATE, KEY_LOG_TIME, KEY_LOG_LOG};

    // ***********************************
    // Table Create Statements

    // System table create statement
    private static final String CREATE_TABLE_SYSTEM = "CREATE TABLE if not exists " + TABLE_SYSTEM + "(" + KEY_SYSTEM_ID + " INTEGER PRIMARY KEY autoincrement ,"
            + KEY_SYSTEM_CLUB + " TEXT," + KEY_SYSTEM_NAME_A + " TEXT," + KEY_SYSTEM_NAME_B + " TEXT," + KEY_SYSTEM_POINTS_A + " TEXT," + KEY_SYSTEM_POINTS_B + " TEXT,"
            + KEY_SYSTEM_GAMES_A + " TEXT," + KEY_SYSTEM_GAMES_B + " TEXT," + KEY_SYSTEM_SETS_A + " TEXT,"+ KEY_SYSTEM_SETS_B + " TEXT," + KEY_SYSTEM_SERVER + " TEXT,"
            + KEY_SYSTEM_SET_TYPE + " INTEGER," + KEY_SYSTEM_LAST_SET + " INTEGER," + KEY_SYSTEM_SET_NO_1 + " INTEGER," + KEY_SYSTEM_SET_NO_3 + " INTEGER,"
            + KEY_SYSTEM_SET_NO_5 + " INTEGER," + KEY_SYSTEM_BATT_THRESH + " INTEGER," + KEY_SYSTEM_BATT_INC + " INTEGER," + KEY_SYSTEM_SSID + " INTEGER,"
            + KEY_SYSTEM_CHANNEL + " INTEGER," + KEY_SYSTEM_NO_ADV + " INTEGER," + KEY_SYSTEM_SHORT_SETS + " INTEGER," + KEY_SYSTEM_MATCH_TB + " INTEGER,"
            + KEY_SYSTEM_MTB_7 + " INTEGER," + KEY_SYSTEM_MTB_10 + " INTEGER," + KEY_SYSTEM_FAST4 + " INTEGER" + ")" + KEY_SYSTEM_SET_1_H + " INTEGER" + ")"
            + KEY_SYSTEM_SET_1_V + " INTEGER" + ")" + KEY_SYSTEM_SET_2_H + " INTEGER" + ")" + KEY_SYSTEM_SET_2_V + " INTEGER" + ")" + KEY_SYSTEM_SET_3_H + " INTEGER" + ")"
            + KEY_SYSTEM_SET_3_V + " INTEGER" + ")" + KEY_SYSTEM_SET_4_H + " INTEGER" + ")" + KEY_SYSTEM_SET_4_V + " INTEGER" + ")" + KEY_SYSTEM_SET_5_H + " INTEGER" + ")"
            + KEY_SYSTEM_SET_5_V + " INTEGER" + ")";

    // Log table create statement
    private static final String CREATE_TABLE_LOG = "CREATE TABLE if not exists " + TABLE_LOG + "(" + KEY_LOG_ID + " INTEGER PRIMARY KEY autoincrement ," + KEY_LOG_DATE + " TEXT,"
            + KEY_LOG_TIME + " TEXT," + KEY_LOG_LOG + " TEXT" + ")";
    // ***********************************
    // Constants & Data
    // ***********************************

    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    // ***********************************
    // Public methods:
    // ***********************************

    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }
    // ***********************************

    // Open the database connection.
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }
    // ***********************************

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // ***********************************
    // Private Helper Classes:
    // ***********************************

    /**
     * Private class which handles database creation and upgrading. Used to
     * handle low-level database access.
     */
    public class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // creating required tables
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_SYSTEM);
            // Insert a zero row in system table.
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_SYSTEM_CLUB, "This is an ILLEGAL copy!");
            initialValues.put(KEY_SYSTEM_NAME_A, " ");
            initialValues.put(KEY_SYSTEM_NAME_B, " ");
            initialValues.put(KEY_SYSTEM_POINTS_A, "0");
            initialValues.put(KEY_SYSTEM_POINTS_B, "0");
            initialValues.put(KEY_SYSTEM_GAMES_A, "0");
            initialValues.put(KEY_SYSTEM_GAMES_B, "0");
            initialValues.put(KEY_SYSTEM_SETS_A, "0");
            initialValues.put(KEY_SYSTEM_SETS_B, "0");
            initialValues.put(KEY_SYSTEM_SERVER, "Z");

            initialValues.put(KEY_SYSTEM_SET_TYPE, "0");
            initialValues.put(KEY_SYSTEM_LAST_SET, "0");
            initialValues.put(KEY_SYSTEM_SET_NO_1, "0");
            initialValues.put(KEY_SYSTEM_SET_NO_3, "1");
            initialValues.put(KEY_SYSTEM_SET_NO_5, "0");

            initialValues.put(KEY_SYSTEM_BATT_THRESH, 40);
            initialValues.put(KEY_SYSTEM_BATT_INC, 5);
            initialValues.put(KEY_SYSTEM_SSID, 1);
            initialValues.put(KEY_SYSTEM_CHANNEL, 1);

            initialValues.put(KEY_SYSTEM_NO_ADV, "0");
            initialValues.put(KEY_SYSTEM_SHORT_SETS, "0");
            initialValues.put(KEY_SYSTEM_MATCH_TB, "0");
            initialValues.put(KEY_SYSTEM_MTB_7, "1");
            initialValues.put(KEY_SYSTEM_MTB_10, "0");
            initialValues.put(KEY_SYSTEM_FAST4, "0");

            initialValues.put(KEY_SYSTEM_SET_1_H, "0");
            initialValues.put(KEY_SYSTEM_SET_1_V, "0");
            initialValues.put(KEY_SYSTEM_SET_2_H, "0");
            initialValues.put(KEY_SYSTEM_SET_2_V, "0");
            initialValues.put(KEY_SYSTEM_SET_3_H, "0");
            initialValues.put(KEY_SYSTEM_SET_3_V, "0");
            initialValues.put(KEY_SYSTEM_SET_4_H, "0");
            initialValues.put(KEY_SYSTEM_SET_4_V, "0");
            initialValues.put(KEY_SYSTEM_SET_5_H, "0");
            initialValues.put(KEY_SYSTEM_SET_5_V, "0");

            db.insert(TABLE_SYSTEM, null, initialValues);
            db.execSQL(CREATE_TABLE_LOG);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {



    L.d("Upgrade");



            switch (newVersion) {
                case 3:
                    db.execSQL("ALTER TABLE " + TABLE_SYSTEM + " ADD COLUMN " + KEY_SYSTEM_CLUB + " TEXT");
                    break;

                case 4:
                    db.execSQL("ALTER TABLE " + TABLE_SYSTEM + " ADD COLUMN " + KEY_SYSTEM_SET_1_H + " INTEGER");
                    db.execSQL("ALTER TABLE " + TABLE_SYSTEM + " ADD COLUMN " + KEY_SYSTEM_SET_1_V + " INTEGER");
                    db.execSQL("ALTER TABLE " + TABLE_SYSTEM + " ADD COLUMN " + KEY_SYSTEM_SET_2_H + " INTEGER");
                    db.execSQL("ALTER TABLE " + TABLE_SYSTEM + " ADD COLUMN " + KEY_SYSTEM_SET_2_V + " INTEGER");
                    db.execSQL("ALTER TABLE " + TABLE_SYSTEM + " ADD COLUMN " + KEY_SYSTEM_SET_3_H + " INTEGER");
                    db.execSQL("ALTER TABLE " + TABLE_SYSTEM + " ADD COLUMN " + KEY_SYSTEM_SET_3_V + " INTEGER");
                    db.execSQL("ALTER TABLE " + TABLE_SYSTEM + " ADD COLUMN " + KEY_SYSTEM_SET_4_H + " INTEGER");
                    db.execSQL("ALTER TABLE " + TABLE_SYSTEM + " ADD COLUMN " + KEY_SYSTEM_SET_4_V + " INTEGER");
                    db.execSQL("ALTER TABLE " + TABLE_SYSTEM + " ADD COLUMN " + KEY_SYSTEM_SET_5_H + " INTEGER");
                    db.execSQL("ALTER TABLE " + TABLE_SYSTEM + " ADD COLUMN " + KEY_SYSTEM_SET_5_V + " INTEGER");
                    break;





                default:
                    throw new IllegalStateException("onUpgrade() with unknown newVersion" + newVersion);
            }
        }
    }

    // ***********************************
    // System Table
    // ***********************************

    // Update a value in the system table.
    public void updateSystem(String strValue_Name, int intValue) {
        ContentValues Values = new ContentValues();
        Values.put(strValue_Name, intValue);
        // Update it in the database.
        db.update(TABLE_SYSTEM, Values, KEY_SYSTEM_ID + "=" + 1, null);
    }
    // ***********************************

    // Update a value in the system table.
    public void updateSystemStr(String strValue_Name, String intValue) {
        ContentValues Values = new ContentValues();
        Values.put(strValue_Name, intValue);
        // Update it in the database.
        db.update(TABLE_SYSTEM, Values, KEY_SYSTEM_ID + "=" + 1, null);
    }
    // ***********************************

    // Read a value in the system table.
    public int readSystem(String strValue_Name) {
        int intData = 0;
        Cursor c = db.query(true, TABLE_SYSTEM, SYSTEM_KEYS, KEY_SYSTEM_ID + "=" + 1, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            intData = c.getInt(c.getColumnIndex(strValue_Name));
        }
        return intData;
    }
    // ***********************************

    // Read a value in the system table.
    public String readSystemStr(String strValue_Name) {
        String strData = "";
        Cursor c = db.query(true, TABLE_SYSTEM, SYSTEM_KEYS, KEY_SYSTEM_ID + "=" + 1, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            strData = c.getString(c.getColumnIndex(strValue_Name));
        }
        return strData;
    }
    // ***********************************
    // Log Table
    // ***********************************

    // Insert a value in the Log table.
    public void K_Log(String strLog) {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        String strDate = padLeft0(("" + day), 2) + "/" + padLeft0(("" + month), 2) + "/" + ("" + year);
        String strTime = padLeft0(("" + hour), 2) + ":" + padLeft0(("" + minute), 2) + ":" + padLeft0(("" + second), 2);
        ContentValues values = new ContentValues();
        values.put(KEY_LOG_DATE, strDate);
        values.put(KEY_LOG_TIME, strTime);
        values.put(KEY_LOG_LOG, strLog);
        // Insert it in the database.
        db.insert(TABLE_LOG, null, values);
    }
    // ***********************************

    public static String padLeft0(String s, int n) {
        return String.format("%" + n + "s", s).replace(' ', '0');
    }
    // ***********************************

    // Return all log rows in the database.
    public Cursor getAllLogRows() {
        Cursor c = db.query(true, TABLE_LOG, LOG_KEYS, null, null, null, null, "_id DESC", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
    // ***********************************

    // Delete all log rows in the database.
    public void Clear_Log() {
        db.delete(TABLE_LOG, null, null);
    }





// To be deleted




    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = myDBHelper.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }




}
