package com.psegina.journal.gui;

import java.text.DateFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.psegina.journal.R;
import com.psegina.journal.data.Database;
import com.psegina.journal.data.EntriesCursorAdapter;
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
			setTitle(getString(R.string.SingleItemTitle)+" "+getIntent().getLongExtra(Database.KEY_ID,1));
			JournalEntry entry = JournalEntry.getById(getIntent().getLongExtra(Database.KEY_ID, 1));
			ListView list =  (ListView) findViewById(R.id.singleItemSimilarList);
			JournalEntry.ViewBuilder builder = new JournalEntry.ViewBuilder();
			builder.setDateFormat(DateFormat.FULL);
			builder.setDateFormat(DateFormat.LONG);
			builder.setShorten(false);

			list.addHeaderView(
					builder.populateView(
							getLayoutInflater().inflate(R.layout.single_item_view_head, null),
							entry)
			);
			
			/*
			 * Fill the "Same tag" field
			 */
			list.setAdapter(new EntriesCursorAdapter(this, JournalEntry.getCursorByTag(entry.getTags()), true));
			
			/*
			 * The following is copied from main.java
			 * so perhaps there is a way to externalize
			 * this
			 */
			
	        list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					Intent i = new Intent(getApplicationContext(), SingleItemView.class);
					i.putExtra(Database.KEY_ID, arg3);
					startActivity(i);
				}
			});
			
		}
	}

}
