package com.psegina.journal;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.psegina.journal.data.Preferences;
import com.psegina.journal.gui.About;
import com.psegina.journal.gui.QuickInput;
import com.psegina.journal.gui.Search;
import com.psegina.journal.gui.Support;

/**
 * A class that allows us to share data
 * between app segments and to handle certain
 * application states.
 * @author Petar Šegina <psegina@ymail.com>
 *
 */
public class App extends Application{
	public static Intent QUICK_INPUT;
	public static Intent SUPPORT;
	public static Intent PREFERENCES;
	public static Intent ABOUT;
	public static Intent SEARCH;
	
	private static Context mContext;
	public static Preferences Prefs = null;
	
	@Override
	public void onCreate(){
		super.onCreate();
		mContext = getApplicationContext();
		Prefs = new Preferences(PreferenceManager.getDefaultSharedPreferences(mContext));
		
		QUICK_INPUT = new Intent(mContext, QuickInput.class);
		SUPPORT = new Intent(mContext, Support.class);
		PREFERENCES = new Intent(mContext, Preferences.class);
		ABOUT = new Intent(mContext, About.class);
		SEARCH = new Intent(mContext, Search.class);
	}
	
	
	public static Context getContext(){
		return mContext;
	}
	
}
