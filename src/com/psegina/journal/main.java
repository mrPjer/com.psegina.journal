package com.psegina.journal;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

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
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Your latest entries");
        
        setContentView(R.layout.main);
        
                final EditText qi = (EditText) findViewById(R.id.quickInput);
        qi.setOnEditorActionListener(new OnEditorActionListener() {
        	@Override
        	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        		// On keypad ENTER
        		if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
        			JournalEntry entry = new JournalEntry(qi.getText()+"");
        			entry.submit();
        			qi.setText("");
        			return true;
        		}
        		return false;
        	}
        });
        
    }
}