package com.psegina.journal.data;

import java.sql.SQLException;
import java.text.DateFormat;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.psegina.journal.App;
import com.psegina.journal.R;
import com.psegina.journal.gui.SingleItemView;

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
		mDB.open();
		Cursor result = mDB.getAll();
		return result;
	}
	
	public static Cursor getEntries(int page){
		mDB.open();
		Cursor result = mDB.getByPage(page);
		return result;
	}
	
	/**
	 * 
	 * @return Total number of entries in the database
	 */
	public static int getNumberOfEntries(){
		mDB.open();
		return ( mDB.getAll() ).getCount();
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
		mDB.close();
		return result;
	}
	
	public static JournalEntry[] getByTag(String tag){
		return mDB.getEntriesByTag(tag);
	}
	
	public static Cursor getCursorByTag(String tag){
		mDB.open();
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
		private boolean mShorten = true;
		private int mDateFormat = DateFormat.FULL;
		private int mTimeFormat = -1;
		
		public void setShorten(boolean shorten){
			mShorten = shorten;
		}
		
		public void setDateFormat(int dateFormat){
			mDateFormat = dateFormat;
		}
		
		public void setTimeFormat(int timeFormat){
			mTimeFormat = timeFormat;
		}
		
		/**
		 * Fills the View with data from a JournalEntry
		 * @param v View to fill with data
		 * @param entry JournalEntry from which to get the data
		 * @return A View filled with data from entry
		 */
		public View populateView(View v, JournalEntry entry){
			/*
			 * Check in the preferences whether the body of the Entry
			 * should be shortened before display
			 */
			String body = entry.getBody();
			if( ( App.Prefs.shorten() ) && (body.length() > App.Prefs.shortLength()) && (mShorten))
				body = body.substring(0, App.Prefs.shortLength()) + App.getContext().getString(R.string.EntryClickForMore);
			
			String timestamp = null;
			if(mTimeFormat != -1)
				timestamp = DateFormat.getDateTimeInstance(mDateFormat, mTimeFormat).format(entry.getTimestamp() * 1000);
			else
				timestamp = DateFormat.getDateInstance(mDateFormat).format(entry.getTimestamp() * 1000);
			
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
				view.setText(timestamp);
			
			body = null;
			view = null;
			return v;
		}
				
	}
	
	/**
	 * A class that provides a method to create a
	 * Paginated display of JournalEntries
	 * @author Petar Šegina <psegina@ymail.com>
	 *
	 */
	public static class Paginator{
	    	private Button mButtonPrev, mButtonNext, mButtonNew;
	    	private TextView mStatusField;
	    	private ListView mListView;
	    	private int mPage = 0;
	    	private Activity mParent;
	    	
			private Paginator(){
	    	}
	    	
			/**
			 * Constructs a Paginator that applies Pagination to a
			 * ListView
			 * @param listView ListView to apply Pagination to
			 * @param activity the Activity that owns listView
			 */
	    	public Paginator(ListView listView, Activity activity){
	    		this();
	    		mListView = listView;
	    		mParent = activity;
	    		
	            mListView.setEmptyView((TextView) mParent.findViewById(android.R.id.empty));
	            if(App.Prefs.showPaginatorLocation())
	            	mListView.addHeaderView(mParent.getLayoutInflater().inflate(R.layout.entries_display_header, null));
	            mListView.addFooterView(mParent.getLayoutInflater().inflate(R.layout.previous_next_buttons, null));
	            
	            mButtonPrev = (Button) mParent.findViewById(R.id.PreviousButton);
	            mButtonNext = (Button) mParent.findViewById(R.id.NextButton);
	            mButtonNew = (Button) mParent.findViewById(R.id.MainNewButton);
	            
	            mStatusField = (TextView) mParent.findViewById(R.id.EntryDisplayHeader);
	            
	            mButtonPrev.setOnClickListener(mButtonListener);
	            mButtonNext.setOnClickListener(mButtonListener);
	            mButtonNew.setOnClickListener(mButtonListener);
	            

	    		mParent.registerForContextMenu(mListView);
	            mListView.setOnItemClickListener(new OnItemClickListener() {
	    			@Override
	    			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	    					long arg3) {
	    				Intent i = new Intent(mParent.getApplicationContext(), SingleItemView.class);
	    				i.putExtra(Database.KEY_ID, arg3);
	    				mParent.startActivity(i);
	    			}
	    		});
	            
	            buildList();
	    	}
	    	
	    	/**
	    	 * 
	    	 * @return True if Paginator is on the first page
	    	 */
	    	private boolean isPageFirst(){
	    		if(mPage == 0)
	    			return true;
	    		else
	    			return false;
	    	}
	    	
	    	/**
	    	 * 
	    	 * @return True if Paginator is on the last page
	    	 */
	    	private boolean isPageLast(){
	    		if( ( (mPage + 1) * App.Prefs.entriesPerPage()) > JournalEntry.getNumberOfEntries() ) 
	    			return true;
	    		else
	    			return false;
	    	}
	    	
	    	/**
	    	 * Creates the Paginated list according to the
	    	 * page the Paginator is on
	    	 */
	    	private void buildList(){
	    		mButtonPrev.setEnabled(!isPageFirst());
	    		mButtonPrev.setClickable(!isPageFirst());
	    		mButtonNext.setEnabled(!isPageLast());
	    		mButtonNext.setClickable(!isPageLast());
	    		if(mStatusField != null)
		    		mStatusField.setText(
		    				mParent.getString(R.string.PaginatorDisplayingEntries) + " " +
		    				mPage*App.Prefs.entriesPerPage() +
		    				"-" +
		    				(mPage+1)*App.Prefs.entriesPerPage() + " " +
		    				mParent.getString(R.string.PaginatorOf) + " " +
		    				JournalEntry.getNumberOfEntries()
		    				);
	    		mListView.setAdapter(new EntriesCursorAdapter(mParent, JournalEntry.getEntries(mPage), true));
	            mParent.setTitle(mParent.getString(R.string.MainTitle) + " ("+JournalEntry.getNumberOfEntries()+")");
	    	}
	    	
	    	/**
	    	 * Moves the Paginator to the next page
	    	 */
	    	public void PageNext(){
	    		if(isPageLast())
	    			throw new IllegalStateException("Can't move to next page because the Paginator is already on the last one");
	    		mPage = mPage + 1;
	    		buildList();
	    	}
	    	
	    	/**
	    	 * Moves the Paginator to the previous page
	    	 */
	    	public void PagePrev(){
	    		if(isPageFirst())
	    			throw new IllegalStateException("Can't move to previous page because the Paginator is already on the first one");
	    		mPage = mPage - 1;
	    		buildList();
	    	}
	    	
	    	/**
	    	 * Moves the Paginator to the first page
	    	 */
	    	public void PageHome(){
	    		mPage = 0;
	    		buildList();
	    	}
	    	
	    	OnClickListener mButtonListener = new OnClickListener(){
	    		@Override
	    		public void onClick(View v) {
	    			if(mParent == null)
	    				throw(new IllegalStateException("mParent has not been set!"));
	    			switch(v.getId()){
	    			case R.id.PreviousButton:
	    				PagePrev();
	    				break;
	    			case R.id.NextButton:
	    				PageNext();
	    				break;
	    			case R.id.MainNewButton:
	    				mParent.startActivityForResult(App.QUICK_INPUT, 0);
	    				break;
	    			default:
	    				Toast.makeText(mParent.getApplicationContext(), "Unhandled action", Toast.LENGTH_SHORT).show();
	    			}	
	    		}	
	    	};
	    	
	    }

	
}
