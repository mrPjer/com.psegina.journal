package com.psegina.journal;

import android.database.Cursor;

public class JournalEntry {
	private long timestamp;
	private String message;
	private String category;
	
	/**
	 * Constructs a new general JournalEntry from the message 
	 * @param msg The message to include in the entry
	 */
	public JournalEntry(String msg){
		timestamp = System.currentTimeMillis()/1000;
		message = msg;
	}
	
	/**
	 * Assigns the entry to a category
	 * @param cat The category to assign the entry to
	 */
	public void assignCategory(int cat){
		//TODO
	}
	
	/**
	 * 
	 * @return Displays all the data related to the entry in a human readable format
	 */
	public String getDescriptor(){
		return "("+timestamp+")"+category+"::"+message;
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
	
}
