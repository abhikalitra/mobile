package com.abhikalitra.util;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

/**
 * Copyright sushil
 * Date: 29 Dec, 2010
 * Time: 8:22:19 PM
 */
public class YesNoDialogPreference extends DialogPreference {

    private YesNoDialogListener mListener;

    public abstract interface YesNoDialogListener {
		public abstract void onDialogClosed(boolean positiveResult);
	}

	public YesNoDialogPreference(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.yesNoPreferenceStyle);
	}

	public YesNoDialogPreference(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public YesNoDialogPreference(Context context) {
        this(context, null);
    }

	public void setListener(YesNoDialogListener listener) {
		mListener = listener;
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (mListener != null) {
			mListener.onDialogClosed(positiveResult);
		}
    }
}
