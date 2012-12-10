package com.kit.contact;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

public class KitContactsProvider extends ContentProvider {

    private KitContactDatabase dbhelper;
    public static final String PROVIDER_NAME = "com.kitcontact";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/contacts" );

    /** Constants to identify the requested operation */
    private static final int CONTACTS = 1;
    private static final int CONTACT_ID = 2;

    private static final UriMatcher uriMatcher ;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "contacts", CONTACTS);
        uriMatcher.addURI(PROVIDER_NAME, "contacts/#", CONTACT_ID);
    }

    /** A callback method which is invoked when the content provider is starting up */
    @Override
    public boolean onCreate() {
        dbhelper = new KitContactDatabase(getContext());
        return true;
    }

    /** A callback method which is invoked when delete operation is requested on this content provider */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int cnt = 0;
        if(uriMatcher.match(uri) == CONTACT_ID ){
            String contactID = uri.getPathSegments().get(1);
            cnt = dbhelper.delete(contactID);
        }
        return cnt;
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }

    /** A callback method which is invoked when insert operation is requested on this content provider */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = dbhelper.insert(values);
        Uri _uri=null;
        if(rowID>0){
            _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
        }else {
            try {
                throw new SQLException("Failed to insert : " + uri);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return _uri;

    }

    /** A callback method which is by the default content uri */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        if(uriMatcher.match(uri)==CONTACTS){
            return dbhelper.listWithCursor();
        }else{
            String contactID = uri.getPathSegments().get(1);
            return dbhelper.getCursorByContactID(contactID);
        }
    }

    /** A callback method which is invoked when update operation is requested on this content provider */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int cnt = 0;
        if(uriMatcher.match(uri) == CONTACT_ID){
            String contactID = uri.getPathSegments().get(1);
            cnt = dbhelper.update(contentValues, contactID);

        }
        return cnt;
    }
}
