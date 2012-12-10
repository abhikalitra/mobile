package com.kit.contact;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.kit.activity.KitContactDetailActivity;
import com.kit.activity.R;


public class KitContactsAdapter extends CursorAdapter {

    private LayoutInflater inflater;
    private Context context;


    public KitContactsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.kitcontacts_item_row, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        final KitContact cc = KitContactDatabase.cursorToContact(cursor);

        TextView cname = (TextView) view.findViewById(R.id.kname);
        if (cname != null) {
            cname.setText(cc.getName());
        }

        TextView cfreq = (TextView) view.findViewById(R.id.freq);
        cfreq.setText(cc.getFreq().txt());

        TextView lastcall = (TextView) view.findViewById(R.id.lastcall);
        lastcall.setText(cc.getLastCallString());

        view.setClickable(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open = new Intent(context, KitContactDetailActivity.class);
                open.putExtra("com.kit.cid",cc.getContactId());
            }
        });
    }

}
