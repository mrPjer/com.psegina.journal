/**
 * 
 */
package com.psegina.journal.data;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.psegina.journal.App;
import com.psegina.journal.R;

/**
 * @author Petar Šegina <psegina@ymail.com>
 *
 */
public class EntriesCursorAdapter extends CursorAdapter {
	private LayoutInflater mInflater;	
	
	private long millis  = 0;
		
	public EntriesCursorAdapter(Context context, Cursor c) {
		super(context, c);
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View v = mInflater.inflate(R.layout.entries_list_single_item, parent, false); 
		if( (cursor.getPosition()>0) && (! isNewGroup(cursor, cursor.getPosition())) )
			v.findViewById(R.id.EntryItemTimestamp).setVisibility(View.GONE);
		return (v);
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor){
		millis = cursor.getLong(cursor.getColumnIndex(Database.KEY_TIMESTAMP)) * 1000;
		/*
		 * Check if the previous one has the same day
		 */
		if( (cursor.getPosition() >= 1) && (cursor.getPosition() < cursor.getColumnCount())){

		}
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
		// Binds the id so that it can be picked up later
		//( (TextView) view.findViewById(R.id.EntryItemId) ).setText( "" + cursor.getLong( cursor.getColumnIndex( Database.KEY_ID ) ) ); 
		
		//TextView tv = (TextView) view.findViewById(R.id.EntryItemTimestamp);
		
		//if(lastDay < (new Date(millis)).getDay())
			//tv.setText(android.text.format.DateFormat.getDateFormat(context).format(new Date(millis)));
		//else if(lastTimestamp <= Calendar.DAY_OF_YEAR)
		//	tv.setVisibility(View.GONE);
		
		//lastDay = (new Date(new Long(millis))).getDay();
	}
	
	private boolean isNewGroup(Cursor cursor, int position){
		Calendar current = Calendar.getInstance();
		Calendar previous = Calendar.getInstance();
		current.setTime(new Date(cursor.getLong(cursor.getColumnIndex(Database.KEY_TIMESTAMP)) * 1000));
		cursor.moveToPosition(position -1);
		previous.setTime(new Date(cursor.getLong(cursor.getColumnIndex(Database.KEY_TIMESTAMP)) * 1000));
		cursor.moveToPosition(position);
		if( previous.get(Calendar.DAY_OF_MONTH) != current.get(Calendar.DAY_OF_MONTH) )
			return true;
		else
			return false;
	}


	
}
