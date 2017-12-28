package com.scorer.tennis;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class LogActivity extends Activity {

    DBAdapter myDb;

    // ***********************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        myDb = new DBAdapter(this);
        myDb.open();

        populateListViewFromDB();
    }

    // ***********************************

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDb.close();
    }

    // ***********************************

    public void Clear_Log(View view) {
        myDb.Clear_Log();
        finish();
    }

    // ***********************************

    private void populateListViewFromDB() {

        Cursor cursor = myDb.getAllLogRows();

        // Allow activity to manage lifetime of the cursor.
        // DEPRECATED! Runs on the UI thread, OK for small/short queries.
        startManagingCursor(cursor);

        // Setup mapping from cursor to view fields:
        String[] fromFieldNames = new String[]{DBAdapter.KEY_LOG_ID, DBAdapter.KEY_LOG_DATE, DBAdapter.KEY_LOG_TIME, DBAdapter.KEY_LOG_LOG};

        int[] toViewIDs = new int[]{R.id.item_id, R.id.item_date, R.id.item_time, R.id.item_log};

        // Create adapter to many columns of the DB onto elements in the UI.
        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(this, // Context
                R.layout.log_item_layout, // Row layout template
                cursor, // cursor (set of DB records to map)
                fromFieldNames, // DB Column names
                toViewIDs // View IDs to put information in
        );

        // Set the adapter for the list view
        ListView myList = (ListView) findViewById(R.id.log_list);
        myList.setAdapter(myCursorAdapter);
    }
}
