package com.psegina.journal.data;

import java.text.DateFormat;
import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.psegina.journal.R;

/**
 * A CursorAdapter that takes a cursor pointing
 * to JournalEntries and creates a suitable View
 * from it.
 * @author Petar Šegina <psegina@ymail.com>
 * 
 */
public class EntriesCursorAdapter extends CursorAdapter {
	private LayoutInflater mInflater;	
	private int current;
	private int previous;
	private Calendar calendar;

	public EntriesCursorAdapter(Context context, Cursor c, boolean requery) {
		super(context, c, requery);
		mInflater = LayoutInflater.from(context);
		calendar = Calendar.getInstance();
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.entries_list_single_item, parent, false);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor){
		JournalEntry.ViewBuilder builder = new JournalEntry.ViewBuilder();
		builder.setDateFormat(DateFormat.FULL);
		view = builder.populateView(view, JournalEntry.Builder.fromCursor(cursor));
		if( !isNewGroup(cursor) )
			( (TextView) view.findViewById(R.id.JournalEntry_Timestamp) ).setVisibility(View.GONE);
		else
			( (TextView) view.findViewById(R.id.JournalEntry_Timestamp) ).setVisibility(View.VISIBLE);
		
	}
	
	/**
	 *
	 * @param cursor A cursor pointing to a specific JournalEntry in the database
	 * @return true if the data pointed by Cursor belongs to a new group, false otherwise
	 */
	private boolean isNewGroup(Cursor cursor){
		calendar.setTimeInMillis( cursor.getLong(cursor.getColumnIndex(Database.KEY_TIMESTAMP)) * 1000);
		current =  calendar.get(Calendar.DAY_OF_YEAR);

		if( !cursor.isFirst() ){
			cursor.moveToPrevious();
			calendar.setTimeInMillis( cursor.getLong(cursor.getColumnIndex(Database.KEY_TIMESTAMP)) * 1000);
			previous = calendar.get(Calendar.DAY_OF_YEAR);
			cursor.moveToNext();
			if( previous != current )
				return true;
			else
				return false;
		}
		else
			return true;
		
	}

}