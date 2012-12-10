package com.kit.activity;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import com.kit.contact.AllContactsAdapter;

public class AllContactsActivity extends ListActivity {

    private static final int LOADER_ID = 0x04;
    String[] projection = new String[] { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME };
    private AllContactsAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.calllog_layout);

        int[] viewIds = {android.R.id.text1};
        String[] datacolums = {ContactsContract.Contacts.DISPLAY_NAME};
        adapter = new AllContactsAdapter(this,null,0);

        this.setListAdapter(adapter);

        LoaderManager.LoaderCallbacks<Cursor> callbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
                return new CursorLoader(AllContactsActivity.this, ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);
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