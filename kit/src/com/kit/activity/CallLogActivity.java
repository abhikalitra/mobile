package com.kit.activity;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.widget.SimpleCursorAdapter;

public class CallLogActivity extends ListActivity {

    private static final int LOADER_ID = 0x04;
    private String[] projection = new String[] {CallLog.Calls._ID, CallLog.Calls.NUMBER};
    private SimpleCursorAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.calllog_layout);

        int[] viewIds = {android.R.id.text1};
        String[] datacolums = {CallLog.Calls.NUMBER};
        adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                null,
                datacolums,
                viewIds,
                0);

        this.setListAdapter(adapter);

        LoaderManager.LoaderCallbacks<Cursor> callbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
                return new CursorLoader(CallLogActivity.this, CallLog.Calls.CONTENT_URI, projection, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
                switch (cursorLoader.getId()) {
                    case LOADER_ID:
                        // The asynchronous load is complete and the data
                        // is now available for use. Only now can we associate
                        // the queried Cursor with the SimpleCursorAdapter.
                        adapter.swapCursor(cursor);
                        break;
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {
                adapter.swapCursor(null);
            }
        };

        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, callbacks);
    }
}
