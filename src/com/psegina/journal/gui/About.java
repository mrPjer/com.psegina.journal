/**
 * 
 */
package com.psegina.journal.gui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.psegina.journal.R;

/**
 * @author Petar Šegina <psegina@ymail.com>
 *	An activity to show information about the app and
 *	how to use it.
 */
public class About extends Activity {
	@Override
	public void onCreate(Bundle SavedInstanceState){
		super.onCreate(SavedInstanceState);
		setTitle(getString(R.string.app_name) + " :: " + getString(R.string.AboutTitle));
		setContentView(R.layout.support);
		LinearLayout root = (LinearLayout) findViewById(R.id.SupportRoot);
		
		View aboutApp = getLayoutInflater().inflate(R.layout.support_single_item, null);
			( (TextView) aboutApp.findViewById(R.id.SupportHeader) ).setText(R.string.AboutAppTitle);
			( (TextView) aboutApp.findViewById(R.id.SupportBody)).setVisibility(View.GONE);
		View androidAtVidi = getLayoutInflater().inflate(R.layout.support_single_item, null);
			( (TextView) androidAtVidi.findViewById(R.id.SupportHeader) ).setText(R.string.AboutAAV);
			( (TextView) androidAtVidi.findViewById(R.id.SupportBody)).setText(R.string.AboutAAVText);
		View journalling = getLayoutInflater().inflate(R.layout.support_single_item, null);
			( (TextView) journalling.findViewById(R.id.SupportHeader) ).setText(R.string.AboutJournaling);
			( (TextView) journalling.findViewById(R.id.SupportBody)).setText(R.string.AboutJournalingText);
	
			
		root.addView(aboutApp);
		root.addView( getLayoutInflater().inflate(R.layout.about_application, null)  );
		root.addView(journalling);
		root.addView(androidAtVidi);
		}
}
