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
 *	This activity is used as a simple guide which
 *	shows the user in which ways can he contribute
 *	to the application development.
 */
public class Support extends Activity {

	@Override
	public void onCreate(Bundle SavedInstanceState){
		super.onCreate(SavedInstanceState);
		setTitle(getString(R.string.app_name)+" :: "+getString(R.string.SupportTitle));
		setContentView(R.layout.support);
		/*
		 * Fill the view with appropriate sections
		 */
		LinearLayout root = (LinearLayout) findViewById(R.id.SupportRoot);
		View intro = ( getLayoutInflater() ).inflate(R.layout.support_single_item,null);
			((TextView) intro.findViewById(R.id.SupportHeader) ).setText(R.string.SupportIntro);
			((TextView) intro.findViewById(R.id.SupportBody) ).setText(R.string.SupportIntroText);
		View bugreporting = ( getLayoutInflater() ).inflate(R.layout.support_single_item,null);
			((TextView) bugreporting.findViewById(R.id.SupportHeader) ).setText(R.string.SupportBugReporting);
			((TextView) bugreporting.findViewById(R.id.SupportBody) ).setText(R.string.SupportBugReportingText);
		View translate = ( getLayoutInflater() ).inflate(R.layout.support_single_item,null);
			((TextView) translate.findViewById(R.id.SupportHeader) ).setText(R.string.SupportTranslate);
			((TextView) translate.findViewById(R.id.SupportBody) ).setText(R.string.SupportTranslateText);
		View coding = ( getLayoutInflater() ).inflate(R.layout.support_single_item,null);
			((TextView) coding.findViewById(R.id.SupportHeader) ).setText(R.string.SupportContributeToTheSource);
			((TextView) coding.findViewById(R.id.SupportBody) ).setText(R.string.SupportContributeToTheSourceText);
		View donations = ( getLayoutInflater() ).inflate(R.layout.support_single_item, null);
			((TextView) donations.findViewById(R.id.SupportHeader) ).setText(R.string.SupportDonate);
			((TextView) donations.findViewById(R.id.SupportBody) ).setText(R.string.SupportDonateText);
	
		root.addView(intro);
		root.addView(bugreporting);
		root.addView(translate);
		root.addView(coding);
		root.addView(donations);
	}
}
