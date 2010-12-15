/**
 * 
 */
package com.psegina.journal.data;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.psegina.journal.App;
import com.psegina.journal.R;
import com.psegina.journal.gui.SingleItemView;

/**
 * @author Petar Šegina <psegina@ymail.com>
 *
 */
public class EntriesCursorAdapter extends CursorAdapter {
	private LayoutInflater mInflater;	
	
	private long millis  = 0;
		
	public EntriesCursorAdapter(Context context, Cursor c, boolean requery) {
		super(context, c, requery);
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View v = mInflater.inflate(R.layout.entries_list_single_item, parent, false); 

		return (v);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor){
		//if(  !isNewGroup( cursor, cursor.getPosition() ) )
		//	view.findViewById(R.id.EntryItemTimestamp).setVisibility(View.GONE);
		millis = cursor.getLong(cursor.getColumnIndex(Database.KEY_TIMESTAMP)) * 1000;

		/*
		 * Fill out the date field
		 */
		( (TextView) view.findViewById(R.id.EntryItemTimestamp) ).setText(android.text.format.DateFormat.format("EEEE, dd MMMM, yyyy.", millis));
			
		/*
		 * Fill out the tag field
		 */
		( (TextView) view.findViewById(R.id.EntryItemTagField)).setText(cursor.getString(cursor.getColumnIndex(Database.KEY_TAG)));
		
		/*
		 * Fill out the body field
		 */
		
		String body = cursor.getString(cursor.getColumnIndex(Database.KEY_BODY));
		if( App.Prefs.shorten() && (body.length() > App.Prefs.shortLength()))
			body = body.substring(0, App.Prefs.shortLength()) + context.getString(R.string.EntryClickForMore);
		( (TextView) view.findViewById(R.id.EntryItemText)).setText(body);

	}
	
	
	
	private boolean isNewGroup(Cursor cursor, int position){
		Calendar calendar = Calendar.getInstance();
		Log.e("POS", ""+position);
		int current = 0;
		int previous = 0;
		calendar.setTime(new Date(cursor.getLong(cursor.getColumnIndex(Database.KEY_TIMESTAMP)) * 1000));
		current =  new Integer(calendar.get(Calendar.DAY_OF_MONTH)) ;
		Log.e("CUR", ""+current);
		if(cursor.getPosition() > 0){
		cursor.moveToPosition(position - 1);
		calendar.setTime(new Date(cursor.getLong(cursor.getColumnIndex(Database.KEY_TIMESTAMP)) * 1000));
		previous = calendar.get(Calendar.DAY_OF_MONTH);
		Log.e("PRE", ""+previous);
		Log.e("CUR2", ""+ current);
		cursor.moveToPosition(position);
		if( previous != current )
			return true;
		else
			return false;
		}
		else
		return true;
	}


	
}
