/**
 * 
 */
package com.psegina.journal.gui;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.psegina.journal.R;
import com.psegina.journal.data.Database;
import com.psegina.journal.data.JournalEntry;


/**
 * @author Petar Šegina <psegina@ymail.com>
 *
 */
public class SingleItemView extends Activity {
	@Override
	public void onCreate(Bundle SavedInstanceState){
		super.onCreate(SavedInstanceState);
		if(!getIntent().hasExtra(Database.KEY_ID)){
			TextView tv = new TextView(getApplicationContext());
			tv.setText(R.string.SingleItemViewNoData);
			setContentView( tv );
		}
		else{
			setContentView(R.layout.single_item_view);
			setTitle("Showing details for entry #"+getIntent().getLongExtra(Database.KEY_ID,1));
			JournalEntry entry = JournalEntry.getById(getIntent().getLongExtra(Database.KEY_ID, 1));
			ListView list =  (ListView) findViewById(R.id.singleItemSimilarList);

			list.addHeaderView(getLayoutInflater().inflate(R.layout.single_item_view_head, null));
			/*
			 * Set the body
			 */
			( (TextView) findViewById(R.id.singleItemBody)).setText(""+entry.getBody());
			/*
			 * Set the date and hour labels
			 */
			( (TextView) findViewById(R.id.singleItemDate) ).setText( DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.LONG).format(new Date(entry.getTimestamp())) );
			/*
			 * Set the tag label
			 */
			( (TextView) findViewById(R.id.singleItemTag) ).setText(""+entry.getTags());
			/*
			 * Fill the "Same tag" field
			 */
			final JournalEntry[] similar = JournalEntry.getByTag(entry.getTags());
			ArrayAdapter<JournalEntry> similarAdapter = new ArrayAdapter<JournalEntry>(this, android.R.layout.simple_list_item_1,similar) {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {

				TextView view = (TextView) super.getView(position, convertView, parent);
				view.setText(similar[position].getBody());
				return view;
				}
				};
			list.setAdapter(similarAdapter);
		}
	}

}
