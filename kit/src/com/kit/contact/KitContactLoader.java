package com.kit.contact;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class KitContactLoader extends AsyncTaskLoader<List<KitContact>> {

    private List<KitContact> contacts;
    private KitContactDatabase dbhelper;

    public KitContactLoader(Context context) {
        super(context);
        dbhelper = new KitContactDatabase(context);
    }

    @Override
    public List<KitContact> loadInBackground() {
        return dbhelper.list();
    }

    @Override
    public void deliverResult(List<KitContact> data) {

        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            onReleaseResources(data);
            return;
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // The old data may still be in use (i.e. bound to an adapter, etc.), so
        // we must protect it until the new data has been delivered.
        List<KitContact> oldData = contacts;
        contacts = data;

        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            super.deliverResult(data);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldData != null && oldData != data) {
            onReleaseResources(oldData);
        }
    }

    @Override
    protected void onStartLoading() {

        if (contacts != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(contacts);
        }

        if (takeContentChanged() || contacts == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        // Ensure the loader has been stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'mData'.
        if (contacts != null) {
            onReleaseResources(contacts);
            contacts = null;
        }

    }

    @Override
    public void onCanceled(List<KitContact> data) {
        super.onCanceled(data);
        onReleaseResources(data);

    }

    protected void onReleaseResources(List<KitContact> data) {
        dbhelper.close();
    }
}
