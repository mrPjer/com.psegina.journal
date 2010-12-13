package com.psegina.journal.gui;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.psegina.journal.App;
import com.psegina.journal.R;
import com.psegina.journal.data.Database;
import com.psegina.journal.data.JournalEntry;

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
	public static final String TAG = "TAGKEY";
	public static final String BODY = "BODYKEY";
	public static final String KEY_ID = "id";
	public static final String KEY_ACTION = "ac";
	public static final int ACTION_EDIT = 0;
	public static final int ACTION_DELETE = 1;
	
	private JournalEntry old = null;
	private Database mDB = new Database();
	private AutoCompleteTextView mTagField;
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
		 /*
		  * OnClickListener for the dialog buttons.
		  * PositiveButton submits the entry
		  * 
		  */
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
		
		mTagField = (AutoCompleteTextView) mDialog.findViewById(R.id.QuickInputTagField);
		mBodyField = (TextView) mDialog.findViewById(R.id.QuickInputBodyField);

		/*
		 * Create the AutoComplete form
		 * First we open a connection to the database.
		 * Then we create a String adapter from all the tag entries in the database
		 * Finally, we set it to the mTagField 
		 */
		if(App.Prefs.useAutoComplete()){
			mDB.open();
			ArrayAdapter<String> autocompleteAdapter = new ArrayAdapter<String>(this,
					R.layout.autocomplete_list_item, 
					mDB.getUniqueTags());
			mTagField.setAdapter(autocompleteAdapter);
			mDB.close();
		}
		/*
		 * Done with the AutoComplete form
		 */
		
		/*
		 * Check if there was any data passed with the intent (most notably, see
		 * if we have to update)
		 */
		if( ( ( (Intent) getIntent() ).hasExtra(KEY_ACTION) ) ){
			old = JournalEntry.getById ( ( ( (Intent) getIntent() ).getExtras() ).getLong(KEY_ID) );
			mTagField.setText(old.getTags());
			mBodyField.setText(old.getBody());
		}
		else
		if( App.Prefs.restoreLastTag())
			mTagField.setText(App.Prefs.getLastTag());
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
		/*
		 * Don't do anything if the entry is invalid
		 */
		if(!validate()){
		}
		/*
		 * Update the entry if we're in updating mode
		 */
		else if( ( ( (Intent) getIntent() ).hasExtra(KEY_ACTION) )){
			if(old != null){
				old.setBody( ""+mBodyField.getText());
				old.setTags(""+mTagField.getText());
				Toast.makeText(getApplicationContext(), R.string.QuickInputUpdated, Toast.LENGTH_SHORT).show();
			}
			finish();
		}
		/*
		 * Else create a new entry
		 */
		else{
			JournalEntry entry = new JournalEntry();
			entry.setBody(""+mBodyField.getText());
			entry.setTags(""+mTagField.getText());
			entry.submit();
			App.Prefs.setLastTag(""+mTagField.getText());
			Toast.makeText(getApplicationContext(), R.string.QuickInputSaved, Toast.LENGTH_SHORT).show();
			finish();
		}
	}
	
	private boolean validate(){
		if(mBodyField.getText().length()>0)
			return true;
		else{
			Toast.makeText(getApplicationContext(), R.string.QuickInputSubmitNoBody, Toast.LENGTH_SHORT).show();
			finish();
			return false;
		}
	}
	
	public void share(String subject,String text) {
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, text);
		startActivity(Intent.createChooser(intent, "Share"));
	}
	
}