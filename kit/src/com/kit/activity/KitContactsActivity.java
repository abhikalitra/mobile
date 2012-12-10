package com.kit.activity;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import com.kit.contact.KitContactsAdapter;
import com.kit.contact.KitContactsProvider;
import com.kit.contact.KitRefreshListener;
import com.kit.contact.ListenerFactory;

public class KitContactsActivity extends ListActivity implements KitRefreshListener {

    private static final int LOADER_ID = 0x06;
    private KitContactsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        final Context context = this;
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.calllog_layout);

        adapter = new KitContactsAdapter(this, null, 0);
        this.setListAdapter(adapter);

        LoaderManager.LoaderCallbacks<Cursor> callbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
                Uri uri = KitContactsProvider.CONTENT_URI;
                return new CursorLoader(context, uri, null, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                adapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> cursorLoader) {
                adapter.swapCursor(null);
            }
        };

        LoaderManager lm = getLoaderManager();
        lm.initLoader(LOADER_ID, null, callbacks);
        getListView().setClickable(true);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent open = new Intent(context, KitContactDetailActivity.class);
                open.putExtra("com.kit.cid", id);
                startActivity(open);
            }
        });
        ListenerFactory.getInstance().setListener(this);
    }


    @Override
    public void refresh() {


    }
}
