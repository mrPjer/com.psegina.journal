package com.psegina.journal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/*
 * TODO
 * Create the Menu
 * 		About
 * 		Back up and restore
 * Add a button for "New entry"
 * Create the methods for loading the items from the
 * 		database and displaying them in a timeline with entries
 * 		grouped by days.
 * Add an onClick handler to the list so that when the user
 * 		clicks an item, he will be taken to a list of all entries with
 * 		the same tag
 * 		-> Think about how to handle this with multiple tags
 */

/**
 * Main Activity class
 * This is the activity that is launched when
 * the app icon is launched.
 * @author Petar Šegina <psegina@ymail.com>
 *
 */
public class main extends Activity {
   private static Button mButtonNew;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Your latest entries");
        
        OnClickListener mOnClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(v.getId()){
				case R.id.MainNewButton:
					Intent i = new Intent(getApplicationContext(), QuickInput.class);
					startActivity(i);
					break;
				}
			}
		};
        
        setContentView(R.layout.main);        
        mButtonNew = (Button) findViewById(R.id.MainNewButton);
        
        mButtonNew.setOnClickListener(mOnClickListener);
        
    }
}