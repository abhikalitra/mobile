package com.kit.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class KitMainActivity extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TabHost tabHost = getTabHost();

        // Tab for Kit Contacts
        TabSpec kitspecs = tabHost.newTabSpec("KIT");
        kitspecs.setIndicator("KIT", getResources().getDrawable(R.drawable.icon_photos_tab));
        Intent kitintent = new Intent(this, KitContactsActivity.class);
        kitspecs.setContent(kitintent);

        // Tab for All Contacts
        TabSpec allcontactsspec = tabHost.newTabSpec("ALL");
        allcontactsspec.setIndicator("ALL", getResources().getDrawable(R.drawable.icon_songs_tab));
        Intent allcontactsintent = new Intent(this, AllContactsActivity.class);
        allcontactsspec.setContent(allcontactsintent);

        // Tab for Call Logs
        TabSpec callogsspec = tabHost.newTabSpec("CALLS");
        callogsspec.setIndicator("CALLS", getResources().getDrawable(R.drawable.icon_songs_tab));
        Intent callogsintent = new Intent(this, CallLogActivity.class);
        callogsspec.setContent(callogsintent);

        // Tab for Videos
        TabSpec settingspecs = tabHost.newTabSpec("Settings");
        settingspecs.setIndicator("Settings", getResources().getDrawable(R.drawable.icon_videos_tab));
        Intent settingintent = new Intent(this, SettingsActivity.class);
        settingspecs.setContent(settingintent);

        // Adding all TabSpec to TabHost
        tabHost.addTab(kitspecs);
        tabHost.addTab(allcontactsspec);
        tabHost.addTab(callogsspec);
        //tabHost.addTab(settingspecs);

    }
}
