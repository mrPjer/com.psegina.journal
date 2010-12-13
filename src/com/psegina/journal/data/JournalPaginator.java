package com.psegina.journal.data;

import com.psegina.journal.App;

/**
 * A class that allows us to get Journal entries
 * by pages in order to avoid showing them all
 * at once.
 * @author Petar Šegina <psegina@ymail.com>
 *
 */
public class JournalPaginator {
	private JournalEntry[] mSource = null;
	private int mStep = App.Prefs.entriesPerPage();
	private int mPosition = 0;
	
	public JournalPaginator(){
	
	}
	
	/**
	 * @param source Source of Journal Entries to paginate
	 */
	public void setSource(JournalEntry[] source){
		mSource = source;
	}
	
	/**
	 * 
	 * @return JournalEntries on the current page
	 */
	public JournalEntry[] getEntries(){
		JournalEntry[] result = new JournalEntry[mStep];
		long end = mPosition + mStep;
		if(end > mSource.length)
			end = mSource.length;
		int j = 0;
		for(int i = mPosition; i < end; i++){
			result[j] = mSource[i];
			j++;
		}
			
		return result;
	}
	
	/**
	 * Moves to the first page
	 */
	public void moveToFirst(){
		mPosition = 0;
	}
	
	/**
	 * Moves to the last page
	 */
	public void moveToLast(){
		int newPosition = 0;
		while((newPosition + mStep) < mSource.length)
			newPosition += mStep;
		mPosition = newPosition;
	}
	
	/**
	 * 
	 * @return true if current page is also the first
	 */
	public boolean isFirst(){
		if(mPosition == 0)
			return true;
		else
			return false;
	}
	
	/**
	 * 
	 * @return true if current page is also the last
	 */
	public boolean isLast(){
		if(mPosition > (mSource.length - mStep))
			return false;
		else
			return true;
	}
	
	/**
	 * Moves the dataset to the next page
	 * @return true if succeeds, false if already on last page
	 */
	public boolean moveToNext(){
		validate();
		int newPosition = mPosition + mStep;
		if( mSource.length > (newPosition - mStep) )
			return false;
		else{
			mPosition = newPosition;
			return true;
		}
	}
	
	/**
	 * Moves to the previous page
	 * @return true on success, false if already on first page
	 */
	public boolean moveToPrevious(){
		validate();
		int newPosition = mPosition - mStep;
		if( mPosition == 0 )
			return false;
		else{
			mPosition = newPosition;
			return true;
		}
	}
	
	/**
	 * Moves to arbitrary position
	 * @param position Page to move to
	 * @return true on success, false if out of bounds
	 */
	public boolean moveToPosition(int position){
		validate();
		if(position < 0)
			return false;
		int newPosition = position * mStep;
		if( mSource.length > (newPosition - mStep) )
			return false;
		else{
			mPosition = newPosition;
			return true;
		}
	}
	
	/**
	 * Reload user preferences
	 */
	public void reloadPreferences(){
		mStep = App.Prefs.entriesPerPage();
	}
	
	/**
	 * Validates the current source
	 * @throws JournalPaginatorException if source is not valid
	 */
	private void validate() throws JournalPaginatorException{
		if(mSource == null)
			throw new JournalPaginatorException(0, JournalPaginatorException.SOURCE_NOT_SET);
	}
	
	/**
	 * An exception class used to create a ForceClose
	 * if the class is not utilised properly
	 * @author Petar Šegina <psegina@ymail.com>
	 *
	 */
	public class JournalPaginatorException extends RuntimeException{
		private static final long serialVersionUID = 1L;
		private static final String SOURCE_NOT_SET = "Data source was not set!";
		private int mErrorNumber;
		private String mMessage;
		
		public JournalPaginatorException(int errorNumber, String message){
			mErrorNumber = errorNumber;
			mMessage = message;
		}
		
		public String toString(){
			return("Fatal JournalPaginatorException["+mErrorNumber+"]  :: "+mMessage);
		}
	}
	
}
