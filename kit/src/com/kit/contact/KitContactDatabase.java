package com.kit.contact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class KitContactDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "kit.db";
    public static final String KITCONTACTS = "kitcontacts";
    public static final int DATABASE_VERSION = 8;

    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public KitContactDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void insertOrUpdate(String contactid, String name) {

        KitContact existing = getByContactID(contactid);

        if (existing == null) {
            KitContact contact = new KitContact();
            contact.setFreq(ConnectFreq.WEEKY);
            contact.setName(name);
            contact.setContactId(contactid);
            contact.setEnabled(true);
            create(contact);
        }
        else {
            existing.setEnabled(true);
            update(existing);
        }
    }

    public KitContact create(KitContact contact) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = toContentValues(contact);
        long insertId = db.insert(KITCONTACTS, null,values);
        contact.setId(insertId);
        return contact;
    }

    public long insert(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(KITCONTACTS, null,values);
    }


    public KitContact update(KitContact contact) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = toContentValues(contact);
        db.update(KITCONTACTS, values, CONTACT_ID + "=?", new String[] { contact.getContactId() } );
        return contact;
    }

    public int update(ContentValues values, String contactID) {
        SQLiteDatabase db = getWritableDatabase();
        return db.update(KITCONTACTS, values, CONTACT_ID + "=?", new String[] { contactID } );
    }

    public int delete(String contactID) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(DATABASE_NAME, CONTACT_ID + " = " + contactID, null);
    }

    public void delete(KitContact contact) {
        SQLiteDatabase db = getWritableDatabase();
        long id = contact.getId();
        db.delete(DATABASE_NAME, ID + " = " + id, null);
    }

    public Cursor listWithCursor() {
        SQLiteDatabase db = getReadableDatabase();
        //return db.query(KITCONTACTS, COLUMNS, ENABLED +"=1", null, null, null, null);
        //db.rawQuery()
        return db.rawQuery("SELECT * FROM kitcontacts where enabled=1 order by (julianday(datetime('now')) - julianday(datetime(lastcall))) / fdays desc",  null);
    }

    public List<KitContact> list() {

        SQLiteDatabase db = getReadableDatabase();
        List<KitContact> comments = new ArrayList<KitContact>();

        Cursor cursor = db.query(KITCONTACTS, COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            KitContact contact = cursorToContact(cursor);
            comments.add(contact);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return comments;
    }

    private ContentValues toContentValues(KitContact contact) {
        ContentValues values = new ContentValues();
        values.put(CONTACT_ID, contact.getContactId());
        values.put(CONTACT_NAME, contact.getName());

        if (contact.getLastCall() != null) values.put(LAST_CALL, sdf.format(contact.getLastCall()));
        values.put(CONTACT_FREQ, contact.getFreq().txt());
        values.put(ENABLED, contact.isEnabled() ? 1:0);
        values.put(FDAYS, contact.getFreq().days());
        return values;
    }

    public static KitContact cursorToContact(Cursor cursor) {

        KitContact contact = new KitContact();

        contact.setId(cursor.getLong(cursor.getColumnIndex(ID)));
        contact.setContactId(cursor.getString(cursor.getColumnIndex(CONTACT_ID)));
        contact.setName(cursor.getString(cursor.getColumnIndex(CONTACT_NAME)));
        contact.setFreq(ConnectFreq.locate(cursor.getString(cursor.getColumnIndex(CONTACT_FREQ))));

        try {
            String dt = cursor.getString(cursor.getColumnIndex(LAST_CALL));
            if (dt != null) contact.setLastCall(sdf.parse(dt));
        } catch (ParseException e) {
            contact.setLastCall(Calendar.getInstance().getTime());
        }
        contact.setEnabled(cursor.getInt(cursor.getColumnIndex(ENABLED)) == 1);

        return contact;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(KitContactDatabase.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + KITCONTACTS);
        onCreate(db);
    }

    public void disable(String contactid) {
        KitContact contact = getByContactID(contactid);
        contact.setEnabled(false);
        update(contact);
    }

    public Cursor getCursorByContactID(String contactid) {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(KITCONTACTS, COLUMNS , CONTACT_ID + "=?", new String[] { contactid }, null, null, null, null);
    }

    public boolean hasContact(String contactid) {
        KitContact contact = getByContactID(contactid);
        return contact != null && contact.isEnabled();
    }

    public KitContact getByContactID(String contactid) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(KITCONTACTS, COLUMNS , CONTACT_ID + "=?", new String[] { contactid }, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursorToContact(cursor);
        }
        else {
            return null;
        }
    }

    private static final String DATABASE_CREATE =
            "create table " + KITCONTACTS + " (" +
                    " _id integer primary key autoincrement, " +
                    " name text not null," +
                    " freq text not null," +
                    " lastcall text," +
                    " enabled integer not null," +
                    " contact_id text not null," +
                    " fdays integer not null);";


    public static final String CONTACT_ID = "contact_id";
    public static final String CONTACT_NAME = "name";
    public static final String LAST_CALL = "lastcall";
    public static final String CONTACT_FREQ = "freq";
    public static final String ID = "_id";
    public static final String ENABLED = "enabled";
    private static final String FDAYS = "fdays";

    public String[] COLUMNS = {
            ID,
            CONTACT_ID,
            CONTACT_NAME,
            CONTACT_FREQ,
            LAST_CALL,
            ENABLED,
            FDAYS

    };


}


//SELECT *,  (julianday(datetime('now')) - julianday(datetime(lastcall))) ,  (julianday(datetime('now')) - julianday(datetime(lastcall))) / fdays FROM kitcontacts order by (julianday(datetime('now')) - julianday(datetime(lastcall))) / fdays desc