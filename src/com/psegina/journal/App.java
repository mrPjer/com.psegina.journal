package com.psegina.journal;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

import com.psegina.journal.data.Preferences;

/**
 * A class that allows us to share data
 * between app segments and to handle certain
 * application states.
 * @author Petar Šegina <psegina@ymail.com>
 *
 */
public class App extends Application{
	private static Context mContext;
	public static Preferences Prefs = null;
	
	@Override
	public void onCreate(){
		super.onCreate();
		mContext = getApplicationContext();
		Prefs = new Preferences(PreferenceManager.getDefaultSharedPreferences(mContext));
	}
	
	
	public static Context getContext(){
		return mContext;
	}
	
}
