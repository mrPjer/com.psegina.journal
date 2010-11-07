package com.psegina.journal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/*
 * TODO
 * Add AutoComplete for the tag field based on existing entries
 */

/**
 * QuickInput Activity Class
 * This activity is used as the interface which allows
 * the user to enter data into the database.
 * @author Petar Šegina <psegina@ymail.com>
 *
 */
public class QuickInput extends Activity {
	private static final String TAG = "TAGKEY";
	private static final String BODY = "BODYKEY";
	
	private TextView mTagField;
	private TextView mBodyField;
	private static AlertDialog.Builder mBuilder;
	private static AlertDialog mDialog = null;
	private LayoutInflater mInflater;
	private View mDialogLayout;
	private OnCancelListener mOnCancelListener;
	private OnClickListener mOnClickListener;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		/*
		 * We need to load our custom layout for the AlertDialog
		 */
		mInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		mDialogLayout = mInflater.inflate(R.layout.quickinput, (ViewGroup) findViewById(R.id.QuickInputRoot));
		/*
		 * If the user dismisses the QI dialog with the back button,
		 * we should call finish() on the activity in order to free up
		 * the screen and memory. 
		 */
		 mOnCancelListener = new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				finish();
			}
		};
		
		mOnClickListener = new OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which){
				// Submit button
				case -1:
					submit();
					break;
				// Cancel button;
				case -2:
					finish();
					break;
				}
			}
		};
		
		/*
		 * We build the AlertDialog with the appropriate options
		 */
		mBuilder = new AlertDialog.Builder(this)
			.setPositiveButton(R.string.QuickInputSubmit, mOnClickListener)
			.setNegativeButton(R.string.QuickInputCancel, mOnClickListener)
			.setTitle(R.string.QuickInputTitle)
			.setOnCancelListener(mOnCancelListener);
		mBuilder.setView(mDialogLayout);
		mDialog = mBuilder.create();
		
		mDialog.show();
		
		mTagField = (TextView) mDialog.findViewById(R.id.QuickInputTagField);
		mBodyField = (TextView) mDialog.findViewById(R.id.QuickInputBodyField);

	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState){
		savedInstanceState.putString(TAG, ""+mTagField.getText());
		savedInstanceState.putString(BODY, ""+mBodyField.getText());
		super.onSaveInstanceState(savedInstanceState);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState){
		super.onRestoreInstanceState(savedInstanceState);
		mTagField.setText(savedInstanceState.getString(TAG));
		mBodyField.setText(savedInstanceState.getString(BODY));
	}
	
	/**
	 * This function submits the data into the database.
	 */
	private void submit(){
		JournalEntry entry = new JournalEntry(""+mBodyField.getText(), ""+mTagField.getText());
		entry.submit();
		finish();
	}
	
	public void share(String subject,String text) {
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, text);
		startActivity(Intent.createChooser(intent, "Share"));
	}
	
}