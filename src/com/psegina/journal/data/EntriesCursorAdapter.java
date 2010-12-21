/**
 * 
 */
package com.psegina.journal.data;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.psegina.journal.R;

/**
 * @author Petar Šegina <psegina@ymail.com>
 *
 */
public class EntriesCursorAdapter extends CursorAdapter {
	private LayoutInflater mInflater;	

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
		view = JournalEntry.ViewBuilder.populateView(view, JournalEntry.Builder.fromCursor(cursor), DateFormat.FULL);
	}
	
	private boolean isNewGroup(Cursor cursor, int position){
		Calendar calendar = Calendar.getInstance();
		//Log.e("POS", ""+position);
		int current = 0;
		int previous = 0;
		calendar.setTime(new Date(cursor.getLong(cursor.getColumnIndex(Database.KEY_TIMESTAMP)) * 1000));
		current =  new Integer(calendar.get(Calendar.DAY_OF_MONTH)) ;
		//Log.e("CUR", ""+current);
		if(cursor.getPosition() > 0){
		cursor.moveToPosition(position - 1);
		calendar.setTime(new Date(cursor.getLong(cursor.getColumnIndex(Database.KEY_TIMESTAMP)) * 1000));
		previous = calendar.get(Calendar.DAY_OF_MONTH);
		//Log.e("PRE", ""+previous);
		//Log.e("CUR2", ""+ current);
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
