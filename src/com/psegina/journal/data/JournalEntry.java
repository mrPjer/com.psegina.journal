package com.psegina.journal.data;

import java.sql.SQLException;
import java.util.Date;

import android.database.Cursor;
import android.util.Log;

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
				JournalEntry row = new JournalEntry();
				
				row.setBody(c.getString(c.getColumnIndex(Database.KEY_BODY)));
				row.setExtra(c.getString(c.getColumnIndex(Database.KEY_EXTRA)));
				row.setId(c.getLong(c.getColumnIndex(Database.KEY_ID)));
				row.setTags(c.getString(c.getColumnIndex(Database.KEY_TAG)));
				row.setTimestamp(c.getLong(c.getColumnIndex(Database.KEY_TIMESTAMP)));
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
			result.setBody(c.getString(c.getColumnIndex(Database.KEY_BODY)));
			result.setExtra(c.getString(c.getColumnIndex(Database.KEY_EXTRA)));
			result.setTags(c.getString(c.getColumnIndex(Database.KEY_TAG)));
			result.setTimestamp(c.getLong(c.getColumnIndex(Database.KEY_TIMESTAMP)));
			result.setId(c.getLong(c.getColumnIndex(Database.KEY_ID)));
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
		Date date = new Date();
		mTimestamp = date.getTime();
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
	
}
