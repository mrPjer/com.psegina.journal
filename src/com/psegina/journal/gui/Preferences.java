package com.psegina.journal.gui;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.psegina.journal.R;

/**
 * @author Petar Šegina <psegina@ymail.com>
 *	PreferenceActivity for managing user preferences.
 *	Layout is defined in res/xml/preferences.xml
 */
public class Preferences extends PreferenceActivity {
	@Override
	public void onCreate(Bundle SavedInstanceState){
		super.onCreate(SavedInstanceState);
		setTitle(getString(R.string.app_name)+" :: "+getString(R.string.PreferencesTitle));
		addPreferencesFromResource(R.xml.preferences);
	}
}