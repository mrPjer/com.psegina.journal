package com.psegina.journal;

import android.database.Cursor;

public class JournalEntry {
	private long mTimestamp;
	private String mBody;
	private String mTags;
	
	/**
	 * Constructs a new JournalEntry.
	 * Note that you must set a body with setBody before
	 * using submit if you use this constructor.
	 */
	public JournalEntry(){
		setTimestamp();
	}
	
	/**
	 * Constructs a new general JournalEntry from a body 
	 * @param body The message to include in the entry
	 */
	public JournalEntry(String body){
		setTimestamp();
		mBody = body;
	}
	
	/**
	 * Constructs a JournalEntry using supplied arguments.
	 * An entry constructed like this can immediately be
	 * submitted using submit.
	 * @param body The body of the entry
	 * @param tags The tagline of the entry
	 */
	public JournalEntry(String body, String tags){
		setTimestamp();
		mBody = body;
		mTags = tags;
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
		// TODO
		return true;
	}

	/**
	 * Retrieves all journal entries from the database
	 * @return A Cursor pointing to fetched entries
	 */
	public static Cursor getEntries(){
		// TODO
		 return (Cursor) null;
	}
	
	/**
	 * Sets the timestamp of the entry to current time in seconds
	 */
	private void setTimestamp(){
		mTimestamp = System.currentTimeMillis()/1000;
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
	
}
