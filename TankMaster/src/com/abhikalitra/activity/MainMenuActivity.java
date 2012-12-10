package com.abhikalitra.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.abhikalitra.R;
import com.abhikalitra.level.LevelTree;
import com.abhikalitra.util.Constants;


/**
 * Copyright sushil
 * Date: 29 Dec, 2010
 * Time: 7:15:13 PM
 */
public class MainMenuActivity extends Activity {

    private boolean mPaused;
    private View mStartButton;
    private View mOptionsButton;
    private View mBackground;
    private View mTicker;
    private Animation mButtonFlickerAnimation;
    private Animation mFadeOutAnimation;
    private Animation mAlternateFadeOutAnimation;
    private Animation mFadeInAnimation;

    private final static int WHATS_NEW_DIALOG = 0;
    
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);

        mPaused = true;

        mStartButton = findViewById(R.id.startButton);
        mOptionsButton = findViewById(R.id.optionButton);
        mBackground = findViewById(R.id.mainMenuBackground);

        if (mStartButton != null) {
            mStartButton.setOnClickListener(sStartButtonListener);
        }

        if (mOptionsButton != null) {
            mOptionsButton.setOnClickListener(sOptionButtonListener);
        }


        mButtonFlickerAnimation = AnimationUtils.loadAnimation(this, R.anim.button_flicker);
        mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        mAlternateFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        mFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        if (!LevelTree.isLoaded()) {
        	LevelTree.loadLevelTree(R.xml.level_tree, this);
        	LevelTree.loadAllDialog(this);
        }

        mTicker = findViewById(R.id.ticker);
        if (mTicker != null) {
        	mTicker.setFocusable(true);
        	mTicker.requestFocus();
        	mTicker.setSelected(true);
        }

        // Keep the volume control type consistent across all activities.
        setVolumeControlStream(AudioManager.STREAM_MUSIC);


    }

    @Override
    protected void onPause() {
        super.onPause();
        mPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPaused = false;


        if (mStartButton != null) {
        	mStartButton.setVisibility(View.VISIBLE);
        	mStartButton.clearAnimation();
            mStartButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_slide));

            // Change "start" to "continue" if there's a saved game.
            SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
            final int row = prefs.getInt(Constants.PREFERENCE_LEVEL_ROW, 0);
            final int index = prefs.getInt(Constants.PREFERENCE_LEVEL_INDEX, 0);
            if (row != 0 || index != 0) {
            	((ImageView)mStartButton).setImageDrawable(getResources().getDrawable(R.drawable.ui_button_continue));
            } else {
            	((ImageView)mStartButton).setImageDrawable(getResources().getDrawable(R.drawable.ui_button_start));
            }

            final int lastVersion = prefs.getInt(Constants.PREFERENCE_LAST_VERSION, 0);
            if (lastVersion == 0) {
            	// This is the first time the game has been run.
            	// Pre-configure the control options to match the device.
            	// The resource system can tell us what this device has.
            	// TODO: is there a better way to do this?  Seems like a kind of neat
            	// way to do custom device profiles.
            	final String navType = getString(R.string.nav_type);
            	if (navType != null) {
            		if (navType.equalsIgnoreCase("DPad")) {
            			// Turn off the click-to-attack pref on devices that have a dpad.
            			SharedPreferences.Editor editor = prefs.edit();
                    	editor.putBoolean(Constants.PREFERENCE_CLICK_ATTACK, false);
                    	editor.commit();
            		} else if (navType.equalsIgnoreCase("None")) {
            			// Turn on tilt controls if there's nothing else.
            			SharedPreferences.Editor editor = prefs.edit();
                    	editor.putBoolean(Constants.PREFERENCE_TILT_CONTROLS, true);
                    	editor.commit();
            		}
            	}

            }



            if (Math.abs(lastVersion) < Math.abs(Constants.VERSION)) {
            	// This is a new install or an upgrade.

            	// Check the safe mode option.
            	// Useful reference: http://en.wikipedia.org/wiki/List_of_Android_devices
            	if (Build.PRODUCT.contains("morrison") ||	// Motorola Cliq/Dext
            			Build.MODEL.contains("Pulse") ||	// Huawei Pulse
            			Build.MODEL.contains("U8220") ||	// Huawei Pulse
            			Build.MODEL.contains("U8230") ||	// Huawei U8230
            			Build.MODEL.contains("MB300") ||	// Motorola Backflip
            			Build.MODEL.contains("Behold+II")) {	// Samsung Behold II
            		// These are all models that users have complained about.  They likely use
            		// the same buggy QTC graphics driver.  Turn on Safe Mode by default
            		// for these devices.
            		SharedPreferences.Editor editor = prefs.edit();
                	editor.putBoolean(Constants.PREFERENCE_SAFE_MODE, true);
                	editor.commit();
            	}

            	// show what's new message
            	SharedPreferences.Editor editor = prefs.edit();
            	editor.putInt(Constants.PREFERENCE_LAST_VERSION, Constants.VERSION);
            	editor.commit();

            	showDialog(WHATS_NEW_DIALOG);

            }

        }

        if (mOptionsButton != null) {
        	mOptionsButton.setVisibility(View.VISIBLE);
        	mOptionsButton.clearAnimation();
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.button_slide);
            anim.setStartOffset(200L);
            mOptionsButton.startAnimation(anim);
        }

        if (mBackground != null) {
        	mBackground.clearAnimation();
        }

        if (mTicker != null) {
        	mTicker.clearAnimation();
        	mTicker.setAnimation(mFadeInAnimation);
        }


    }

    @Override
	protected Dialog onCreateDialog(int id) {
    	Dialog dialog;
		if (id == WHATS_NEW_DIALOG) {
			dialog = new AlertDialog.Builder(this)
            .setTitle(R.string.whats_new_dialog_title)
            .setPositiveButton(R.string.whats_new_dialog_ok, null)
            .setMessage(R.string.whats_new_dialog_message)
            .create();
		} else {
			dialog = super.onCreateDialog(id);
		}
		return dialog;
	}

        // Create an anonymous implementation of OnClickListener
    private View.OnClickListener sStartButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (!mPaused) {
                Intent i = new Intent(getBaseContext(), TankMasterMainActivity.class);

                v.startAnimation(mButtonFlickerAnimation);
                mFadeOutAnimation.setAnimationListener(new StartActivityAfterAnimation(i));
                mBackground.startAnimation(mFadeOutAnimation);
                mOptionsButton.startAnimation(mAlternateFadeOutAnimation);
                mTicker.startAnimation(mAlternateFadeOutAnimation);
                mPaused = true;
            }
        }
    };

    private View.OnClickListener sOptionButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            
            if (!mPaused) {
                Intent i = new Intent(getBaseContext(), SetPreferencesActivity.class);

                v.startAnimation(mButtonFlickerAnimation);
                mFadeOutAnimation.setAnimationListener(new StartActivityAfterAnimation(i));
                mBackground.startAnimation(mFadeOutAnimation);
                mStartButton.startAnimation(mAlternateFadeOutAnimation);
                mTicker.startAnimation(mAlternateFadeOutAnimation);
                mPaused = true;
            }
        }
    };

    protected class StartActivityAfterAnimation implements Animation.AnimationListener {
        private Intent mIntent;

        StartActivityAfterAnimation(Intent intent) {
            mIntent = intent;
        }


        public void onAnimationEnd(Animation animation) {
        	mStartButton.setVisibility(View.INVISIBLE);
        	mStartButton.clearAnimation();
        	mOptionsButton.setVisibility(View.INVISIBLE);
        	mOptionsButton.clearAnimation();
            startActivity(mIntent);
        }

        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }

        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub

        }

    }
}
