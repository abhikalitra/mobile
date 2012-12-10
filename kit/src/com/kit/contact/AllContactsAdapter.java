package com.kit.contact;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.kit.activity.R;

public class AllContactsAdapter extends CursorAdapter {

    private KitContactDatabase contactdb;
    private LayoutInflater inflater;
    private Context context;

    public AllContactsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
        inflater = LayoutInflater.from(context);
        contactdb = new KitContactDatabase(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.contacts_item_row, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {


        final String contactid = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        final String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

        TextView cname = (TextView) view.findViewById(R.id.cname);
        if (cname != null) {
            cname.setText(name);
        }

        CheckBox cb = (CheckBox) view.findViewById(R.id.cb);

        boolean kitcontact = contactdb.hasContact(contactid);

        cb.setChecked(kitcontact);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    contactdb.insertOrUpdate(contactid, name);
                    ListenerFactory.getInstance().refresh();
                }
                else {
                    contactdb.disable(contactid);
                    ListenerFactory.getInstance().refresh();
                }

            }
        });

        cb.setSelected(false);

    }
}
