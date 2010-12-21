package com.psegina.journal.data;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;

import com.psegina.journal.App;
import com.psegina.journal.R;

import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * A class that represents a single JournalEntry, but also
 * provides static methods to manipulate entries in the
 * database.
 * @author Petar Šegina <psegina@ymail.com>
 *
 */
public class JournalEntry {
	private long mTimestamp;
	private long mId;
	private String mBody;
	private String mTags;
	private String mExtra;
	private static Database mDB = new Database();
	
	/**
	 * Constructs a new JournalEntry.
	 * Note that you must set a body with setBody before
	 * using submit if you use this constructor.
	 */
	public JournalEntry(){
		setTimestamp();
	}
		
	/**
	 * If you need to get the JournalEntry in a human readable format for debug purposes,
	 * simply write out the result of this function.
	 * @return String representing all the data related to the entry in a human readable format
	 */
	public String getDescriptor(){
		return "("+mTimestamp+")"+mTags+"::"+mBody;
	}
	
	/**
	 * Submits the JournalEntry into the database
	 * @return true on success, else false
	 */
	public boolean submit(){
		mDB.open();
		mDB.insert(this);
		mDB.close();
		return true;
	}

	public long update(){
		mDB.open();
		long i =mDB.update(this);
		mDB.close();
		return i;
	}
	
	public void delete(){
		mDB.open();
		mDB.delete(mId);
		mDB.close();
	}
	
	public static void delete(long id){
		mDB.open();
		mDB.delete(id);
		mDB.close();
	}
	
	/**
	 * Retrieves all journal entries from the database
	 * @return A Cursor pointing to fetched entries
	 */
	public static Cursor getEntries(){
		if(mDB!=null)
		mDB.open();
		else Log.e("TEST", "NULL");
		return mDB.getAll();
	}
	
	/**
	 * 
	 * @return an array of all entries from the database
	 */
	public static JournalEntry[] getAll(){
		Cursor c = getEntries();
		int count = c.getCount();
		JournalEntry[] result = new JournalEntry[count];
		if(c.moveToFirst())
			for(int i=0; i<count; i++){
				JournalEntry row = JournalEntry.Builder.fromCursor(c);
				result[i] = row;
				c.moveToNext();
			}
		
		c.close();
		return result;
	}
	
	public static JournalEntry getById(long id){
		mDB.open();
		JournalEntry result = new JournalEntry();
		try {
			Cursor c = mDB.getSpecific(id);
			result = JournalEntry.Builder.fromCursor(c);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	public static JournalEntry[] getByTag(String tag){
		return mDB.getEntriesByTag(tag);
	}
	
	public static Cursor getCursorByTag(String tag){
		return mDB.getCursorByTag(tag);
	}
	
	/*
	 * Setters and getters below. Do not change their format
	 * since other parts of the application might depend
	 * on them.
	 */
	
	/**
	 * Sets the timestamp of the entry to current time in seconds
	 */
	public void setTimestamp(){
		mTimestamp = System.currentTimeMillis()/1000;
	}
	
	/**
	 * Sets the timestamp to the specified time
	 * @param time Time to set to the timestamp
	 */
	public void setTimestamp(long time){
		mTimestamp = time;
	}
	
	/**
	 * Sets the body value of the entry
	 * @param body The body of the entry to record
	 */
	public void setBody(String body){
		mBody = body;
	}
	
	/**
	 * Sets the tagline of the entry
	 * @param tags The tagline to apply
	 */
	public void setTags(String tags){
		mTags = tags;
	}
	
	/**
	 * Sets any extra parameters :: CURRENTLY UNUSED
	 * @param extra Parameters to set in the extra field
	 */
	public void setExtra(String extra){
		mExtra = extra;
	}
	
	/**
	 * Sets the id of the entry
	 * @param id Numerical value to assign the id
	 */
	public void setId(long id){
		mId = id;
	}
	
	/**
	 * 
	 * @return The id of this entry in the database
	 */
	public long getId(){
		return mId;
	}
	
	/**
	 * Returns the tag field of the JournalEntry object
	 * @return The tag field in a String format
	 */
	public String getTags(){
		return mTags;
	}
	
	/**
	 * Returns the body field of the JournalEntry object
	 * @return The body field in a String format
	 */
	public String getBody(){
		return mBody;
	}
	
	/**
	 * Returns the timestamp field of the JournalEntry object
	 * @return The timestamp field in a long format
	 */
	public long getTimestamp(){
		return mTimestamp;
	}
	
	/**
	 * Returns the extra field of the JournalEntry object
	 * @return The extra field in a String format
	 */
	public String getExtra(){
		return mExtra;
	}
	
	/**
	 * A static nested class that provides methods for
	 * building JournalEntry objects from various sources.
	 * @author Petar Šegina <psegina@ymail.com>
	 *
	 */
	public static class Builder{
		/**
		 * Builds a JournalEntry object from a Cursor pointing
		 * to a properly formated table row.
		 * @param c Cursor pointing to an entry in a properly formated table
		 * @return A valid JournalEntry with data from the Cursor
		 */
		public static JournalEntry fromCursor(Cursor c){
			JournalEntry result = new JournalEntry();
			result.setBody(c.getString(c.getColumnIndex(Database.KEY_BODY)));
			result.setExtra(c.getString(c.getColumnIndex(Database.KEY_EXTRA)));
			result.setId(c.getLong(c.getColumnIndex(Database.KEY_ID)));
			result.setTags(c.getString(c.getColumnIndex(Database.KEY_TAG)));
			result.setTimestamp(c.getLong(c.getColumnIndex(Database.KEY_TIMESTAMP)));
			return result;
		}
		
	}
	
	/**
	 * A static nested class that provides methods for
	 * filling out Views with data from a JournalEntry object
	 * @author Petar Šegina <psegina@ymail.com>
	 *
	 */
	public static class ViewBuilder{
		/**
		 * Fills the View with data from a JournalEntry
		 * @param v View to fill with data
		 * @param entry JournalEntry from which to get the data
		 * @return A View filled with data from entry
		 */
		public static View populateView(View v, JournalEntry entry){
			/*
			 * Check in the preferences whether the body of the Entry
			 * should be shortened before display
			 */
			String body = entry.getBody();
			if( ( App.Prefs.shorten() ) && (body.length() > App.Prefs.shortLength()) )
				body = body.substring(0, App.Prefs.shortLength()) + App.getContext().getString(R.string.EntryClickForMore);
			
			/*
			 * Fill out the View
			 */
			TextView view;
			view = (TextView) v.findViewById(R.id.JournalEntry_Body);
			if(view != null)
				view.setText(body);
			view = (TextView) v.findViewById(R.id.JournalEntry_Extra);
			if(view != null)
				view.setText(entry.getExtra());
			view = (TextView) v.findViewById(R.id.JournalEntry_Tags);
			if(view != null)
				view.setText(entry.getTags());
			view = (TextView) v.findViewById(R.id.JournalEntry_Id);
			if(view != null)
				view.setText(Long.toString(entry.getId()));
			view = (TextView) v.findViewById(R.id.JournalEntry_Timestamp);
			if(view != null)
				view.setText(Long.toString(entry.getTimestamp()));
			
			body = null;
			view = null;
			return v;
		}
		
		/**
		 * Fills the View with data from a JournalEntry and
		 * formats the timestamp with date only
		 * @param v View to fill with data
		 * @param entry JournalEntry from which to get the data
		 * @param dateFormat one of the DateFormat formatting constants.
		 * @return View with data filled out
		 */
		public static View populateView(View v, JournalEntry entry, int dateFormat){
			v = populateView(v, entry);
			( (TextView) v.findViewById(R.id.JournalEntry_Timestamp)).setText
					((DateFormat.getDateInstance(dateFormat)).format(entry.getTimestamp()*1000));
			return v;
		}
		
		/**
		 * Fills the View with data from a JournalEntry and
		 * formats the timestamp with date and time
		 * @param v View to fill with data
		 * @param entry JournalEntry from which to get the data
		 * @param dateFormat one of the DateFormat formatting constants
		 * @param timeFormat one of the DateFormat formatting constants.
		 * @return View with data filled out
		 */	
		public static View populateView(View v, JournalEntry entry, int dateFormat, int timeFormat){
			v = populateView(v, entry);
			( (TextView) v.findViewById(R.id.JournalEntry_Timestamp)).setText
			((DateFormat.getDateTimeInstance(dateFormat, timeFormat)).format(entry.getTimestamp()*1000));				
			return v;
		}
	}
	
}
