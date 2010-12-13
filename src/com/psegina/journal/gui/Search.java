package com.psegina.journal.gui;

import java.util.Vector;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.psegina.journal.data.Database;
import com.psegina.journal.data.JournalEntry;

/*
 * An activity that searches the database
 * and outputs result in a Listview
 */
public class Search extends ListActivity {
	@Override
	public void onCreate(Bundle SavedInstanceState){
		super.onCreate(SavedInstanceState);
		
		Intent intent = getIntent();
		if(Intent.ACTION_SEARCH.equals(intent.getAction())){
			String query = intent.getStringExtra(SearchManager.QUERY);
			Database mDB = new Database();
			mDB.open();
			String[] tags = mDB.getUniqueTags();
			mDB.close();
			Vector<String> result = new Vector<String>();
			long tagCount = tags.length;
			/*
			 * Add the tag results to the query
			 */
			for(int i=0; i<tagCount; i++)
				if( (tags[i].toLowerCase()).contains(query.toLowerCase()))
					result.add(tags[i]);
			String[] tagResult =new String[result.size()];
			result.toArray(tagResult);
			result = null;
			setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tagResult));
			
			JournalEntry[] entries = JournalEntry.getAll();
			Vector<JournalEntry> entryResult = new Vector<JournalEntry>();
			long entryCount = entries.length;
			for(int i=0; i<entryCount; i++)
				if( (entries[i].getBody().toLowerCase()).contains(query)  )
						entryResult.add(entries[i]);
			
		}
	}
}
