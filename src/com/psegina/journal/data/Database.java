package com.psegina.journal.data;

import java.sql.SQLException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.psegina.journal.App;


/**
 * A class that provides methods for database access.
 * @author Petar Šegina <psegina@ymail.com>
 *
 */
public class Database {
	private static final String DATABASE_NAME = "Journal.db";
	private static final String TABLE_NAME = "Entries";
	private static final int DATABASE_VERSION = 2;
	
	private DatabaseDBHelper mDBHelper;
	
	public static final String KEY_ID = "_id";
	public static final String KEY_TAG = "tag";
	public static final String KEY_BODY = "body";
	public static final String KEY_TIMESTAMP = "timestamp";
	public static final String KEY_EXTRA = "extra";
		
	private SQLiteDatabase mDB = null;
	
	/**
	 * Constructs a new Database object that can be
	 * used to interact with the database.
	 * This class can only be used to interact with the
	 * table that stores JournalEntries
	 * @param context Application context
	 */
	public Database(){
		mDBHelper = new DatabaseDBHelper(App.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/*
	 * Insert, delete and update
	 */
	
	/**
	 * Inserts a new JournalEntry into the database
	 * @param entry JournalEntry object
	 * @return _id of the inserted object
	 */
	public long insert(JournalEntry entry){
		ContentValues cValues = new ContentValues();
		cValues.put(KEY_BODY, entry.getBody());
		cValues.put(KEY_EXTRA, entry.getExtra());
		cValues.put(KEY_TAG, entry.getTags());
		cValues.put(KEY_TIMESTAMP, entry.getTimestamp());
		return mDB.insert(TABLE_NAME, null, cValues);
	}
	
	/**
	 * Deletes an entry with the specified ID
	 * @param id ID of the entry to delete
	 * @return Number of affected rows
	 */
	public int delete(long id){
		return mDB.delete(TABLE_NAME, KEY_ID+"="+id,null);
	}
	
	/**
	 * Updates the specified entry.
	 * @param entry JournalEntry that will be updated
	 * @return Number of affected rows
	 */
	public long update(JournalEntry entry){
		ContentValues cValues = new ContentValues();
		cValues.put(KEY_BODY, entry.getBody());
		cValues.put(KEY_EXTRA, entry.getExtra());
		cValues.put(KEY_TAG, entry.getTags());
		cValues.put(KEY_TIMESTAMP, entry.getTimestamp() / 1000);
		return mDB.update(TABLE_NAME, cValues, KEY_ID+"="+entry.getId(), null);
	}
	
	/*
	 * getAll, getOne, getEntry
	 */
	
	/**
	 * Returns all entries in the database
	 */
	public Cursor getAll(){
		Cursor tm = mDB.query(TABLE_NAME, null, null, null, null, null, KEY_TIMESTAMP+" DESC");
		return tm;
	}

	/**
	 * Get a cursor pointing to a specific entry
	 * @param id The id of the entry to point the cursor to
	 * @return A cursor pointing to the entry
	 * @throws SQLException if the entry is not founds
	 */
	public Cursor getSpecific(long id) throws SQLException{
		Cursor c = mDB.query(TABLE_NAME, null, KEY_ID+"="+id, null, null, null, null);
		if((c.getCount()==0) || !c.moveToFirst())
			throw new SQLException("DB :: Entry with key "+id+" not found!");
		return c;
	}
	
	/**
	 * Returns a JournalEntry from the database
	 * @param id Id of the entry to return
	 * @return A completely filled JournalEntry object
	 * @throws SQLException throws an exception if the entry is not found
	 */
	public JournalEntry getEntry(long id) throws SQLException{
		Cursor c = mDB.query(TABLE_NAME, null, KEY_ID+"="+id, null, null, null, null);
		if((c.getCount()==0) || (!c.moveToFirst()))
			throw new SQLException("DB :: Entry with key "+id+ " not found!");
		JournalEntry result = new JournalEntry();
		result.setBody(c.getString(c.getColumnIndex(KEY_BODY)));
		result.setExtra(c.getString(c.getColumnIndex(KEY_EXTRA)));
		result.setTags(c.getString(c.getColumnIndex(KEY_TAG)));
		result.setTimestamp(c.getLong(c.getColumnIndex(KEY_TIMESTAMP)));
		c.close();
		return result;
	}
	
	public JournalEntry[] getEntriesByTag(String tag){
		Cursor c = mDB.query(TABLE_NAME, null, KEY_TAG+"=\""+tag+"\"",	null, null, null, null);
		int rowCount = c.getCount();
		JournalEntry[] result = new JournalEntry[rowCount];
		c.moveToFirst();
		for(int i=0;i<rowCount; i++){
			JournalEntry row = new JournalEntry();
			row.setBody(c.getString(c.getColumnIndex(KEY_BODY)));
			row.setExtra(c.getString(c.getColumnIndex(KEY_EXTRA)));
			row.setTags(c.getString(c.getColumnIndex(KEY_TAG)));
			row.setTimestamp(c.getLong(c.getColumnIndex(KEY_TIMESTAMP)));
			row.setId(c.getLong(c.getColumnIndex(KEY_ID)));
			result[i] = row;
			c.moveToNext();
		}
		c.close();
		return result;
	}
	
	/**
	 * Method that returns unique tags present in the database
	 * @return String array of unique tags
	 */
	public String[] getUniqueTags(){
		Cursor c = mDB.query(true, TABLE_NAME, new String[]{KEY_TAG}, null, null, null, null, null, null);
		int count = c.getCount();
		String[] result = new String[count];
		for(int i = 0; i<count;i++){
			c.moveToPosition(i);
			result[i] = c.getString(c.getColumnIndex(KEY_TAG));
		}
		c.close();
		return result;
	}
	
	/*
	 * Open and close
	 */
	
	/**
	 * Tries to connect to the database.
	 * @return True if the database is writable, False otherwise
	 * @throws SQLiteException
	 */
	public boolean open(){
		//try{
		if(mDB == null)
			mDB = mDBHelper.getWritableDatabase();
			return true;
		//}
		//catch(SQLiteException e){
		//	mDB = mDBHelper.getReadableDatabase();
		//	return false;
		//}
	}
	
	/**
	 * Closes the connection.
	 */
	public void close(){
		mDB.close();
		mDB = null;
	}
		
	private static class DatabaseDBHelper extends SQLiteOpenHelper{
		private static final String DB_CREATE = "CREATE TABLE "+TABLE_NAME+"( "+
			KEY_ID+" integer primary key autoincrement, "+
			KEY_TAG+" text, "+
			KEY_BODY+" text not null, "+
			KEY_TIMESTAMP+ " long not null, "+
			KEY_EXTRA+ " text" +
					");";
		
		public DatabaseDBHelper(Context context, String name, CursorFactory factory, int version){
			super(context, name, factory, version);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DB_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db,  int oldVersion, int newVersion) {
			Log.w("JournalDB", "Upgrading database");
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
			onCreate(db);
		}
		
	}
	
}
