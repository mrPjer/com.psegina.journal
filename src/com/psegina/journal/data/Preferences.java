package com.psegina.journal.data;

import com.psegina.journal.App;
import com.psegina.journal.R;

import android.content.SharedPreferences;

/**
 * Class that provides methods for accessing
 * saved user preferences.
 * @author Petar Šegina <psegina@ymail.com>
 *
 */
public class Preferences {
	private SharedPreferences mPreferences;
	private SharedPreferences.Editor mEditor;
	
	public Preferences(SharedPreferences preferences){
		mPreferences = preferences;
		mEditor = mPreferences.edit();
	}	
	
	/*
	 * Methods for checking specific settings
	 * This is to ensure consistency within the app
	 */
	
	public boolean restoreLastTag(){
		return mPreferences.getBoolean(App.getContext().getString(R.string.KEY_PreferenceItemInputRememberLast), false);
	}
	
	public String getLastTag(){
		return mPreferences.getString(App.getContext().getString(R.string.KEY_PreferencesItemLastSavedTag), "");
	}
	
	public void setLastTag(String tag){
		mEditor.putString(App.getContext().getString(R.string.KEY_PreferencesItemLastSavedTag), tag);
	}
	
	public boolean useAutoComplete(){
		return mPreferences.getBoolean(App.getContext().getString(R.string.KEY_PreferencesItemInputAutocomplete), true);
	}
	
	public boolean passwordCheck(String password){
		return false;
	}
		
	public boolean shorten(){
		return mPreferences.getBoolean(App.getContext().getString(R.string.KEY_PreferencesItemGeneralShorten), false);
	}
	
	public int shortLength(){
		return Integer.parseInt(mPreferences.getString(App.getContext().getString(R.string.KEY_PreferencesItemGeneralShortenAmount), "200"));
	}
	
	public boolean showPaginatorLocation(){
		return mPreferences.getBoolean(App.getContext().getString(R.string.KEY_PreferencesItemShowPaginatorLocationHeader), false);
	}
	
	public int entriesPerPage(){
		return Integer.parseInt(mPreferences.getString(App.getContext().getString(R.string.KEY_PreferencesItemGeneralEntriesPerPage), "20"));
	}
	
}
