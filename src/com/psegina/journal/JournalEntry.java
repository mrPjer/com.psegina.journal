package com.psegina.journal;

import android.content.Context;
import android.database.Cursor;

import com.psegina.journal.data.Database;

/**
 * A class that represents a single JournalEntry, but also
 * provides static methods to manipulate entries in the
 * database.
 * @author Petar Šegina <psegina@ymail.com>
 *
 */
public class JournalEntry {
	private long mTimestamp;
	private int mId;
	private String mBody;
	private String mTags;
	private String mExtra;
	private Context mContext;
	private static Database mDB;
	
	/**
	 * Constructs a new JournalEntry.
	 * Note that you must set a body with setBody before
	 * using submit if you use this constructor.
	 */
	public JournalEntry(Context context){
		setTimestamp();
		mContext = context;
		mDB = new Database(mContext);
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

	public void update(){
		mDB.update(this);
	}
	
	public void delete(){
		mDB.delete(mId);
	}
	
	public static void delete(int id){
		mDB.delete(id);
	}
	
	/**
	 * Retrieves all journal entries from the database
	 * @return A Cursor pointing to fetched entries
	 */
	public static Cursor getEntries(){
		return mDB.getAll();
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
	 * Sets any extra parameters
	 * @param extra Parameters to set in the extra field
	 */
	public void setExtra(String extra){
		mExtra = extra;
	}
	
	/**
	 * 
	 * @return The id of this entry in the database
	 */
	public int getId(){
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
