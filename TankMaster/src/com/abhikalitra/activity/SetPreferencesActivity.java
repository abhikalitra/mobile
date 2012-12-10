package com.abhikalitra.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;
import com.abhikalitra.R;
import com.abhikalitra.util.Constants;
import com.abhikalitra.util.YesNoDialogPreference;

/**
 * Copyright sushil
 * Date: 29 Dec, 2010
 * Time: 8:12:24 PM
 */
public class SetPreferencesActivity extends PreferenceActivity implements
		YesNoDialogPreference.YesNoDialogListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceManager().setSharedPreferencesMode(MODE_PRIVATE);
        getPreferenceManager().setSharedPreferencesName(Constants.PREFERENCE_NAME);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        Preference eraseGameButton = getPreferenceManager().findPreference("erasegame");
        if (eraseGameButton != null) {
        	YesNoDialogPreference yesNo = (YesNoDialogPreference)eraseGameButton;
        	yesNo.setListener(this);
        }

//        Preference configureKeyboardPref = getPreferenceManager().findPreference("keyconfig");
//        if (configureKeyboardPref != null) {
//        	KeyboardConfigDialogPreference config = (KeyboardConfigDialogPreference)configureKeyboardPref;
//        	config.setPrefs(getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE));
//        	config.setContext(this);
//        }
    }

    public void onDialogClosed(boolean positiveResult) {

        if (positiveResult) {
			SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.remove(Constants.PREFERENCE_LEVEL_ROW);
			editor.remove(Constants.PREFERENCE_LEVEL_INDEX);
			editor.remove(Constants.PREFERENCE_LEVEL_COMPLETED);
			editor.commit();
			Toast.makeText(this, R.string.saved_game_erased_notification,
                    Toast.LENGTH_SHORT).show();
		}
    }
}
