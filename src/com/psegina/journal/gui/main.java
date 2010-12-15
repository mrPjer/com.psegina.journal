package com.psegina.journal.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.psegina.journal.App;
import com.psegina.journal.R;
import com.psegina.journal.data.Database;
import com.psegina.journal.data.EntriesCursorAdapter;
import com.psegina.journal.data.EntryArrayAdapter;
import com.psegina.journal.data.JournalEntry;
import com.psegina.journal.data.JournalPaginator;

/*
 * TODO
 * Create the Menu
 * 		About
 * 		Back up and restore
 * Create the methods for loading the items from the
 * 		database and displaying them in a timeline with entries
 * 		grouped by days.
 * Add an onClick handler to the list so that when the user
 * 		clicks an item, he will be taken to a list of all entries with
 * 		the same tag
 * 		-> Think about how to handle this with multiple tags
 * Fix the refresh after submitting
 * Make a view that will be loaded as default if no entries
 * 		are present. This view should contain a basic tutorial
 * 		with information about the app.
 */

/**
 * Main Activity class
 * This is the activity that is launched when
 * the app icon is activated.
 * Presents the user with a list of all his submits and
 * gives him the option of adding new ones, deleting
 * existing ones or modifying and sharing them..
 * @author Petar Šegina <psegina@ymail.com>
 *
 */
public class main extends Activity {
	private static final int ID_EDIT = 0;
	private static final int ID_DELETE = 1;
	private static final int ID_SHARE = 2;
	private static final int ID_CLIPBOARD = 3;
	
	private static final Intent QUICK_INPUT = new Intent(App.getContext(), QuickInput.class);
	private static final Intent SUPPORT = new Intent(App.getContext(), Support.class);
	private static final Intent PREFERENCES = new Intent(App.getContext(), Preferences.class);
	private static final Intent ABOUT = new Intent(App.getContext(), About.class);
	private static final Intent SEARCH = new Intent(App.getContext(), Search.class);
	
   private static Button mButtonNew;
   private Cursor mCursor;
   private EntriesCursorAdapter mListAdapter;
   private ListView mListView;
   private OnMenuItemClickListener mMenuItemClickListener; 
   private JournalPaginator paginator = new JournalPaginator();
   
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
		
        setContentView(R.layout.main);        
        
        mButtonNew = (Button) findViewById(R.id.MainNewButton);
        mButtonNew.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					startActivityForResult(QUICK_INPUT, 0);
			}
		});
        
        mListView = (ListView) findViewById(R.id.MainTextView);
        mListView.setEmptyView((TextView) findViewById(android.R.id.empty));

        buildList();
        startManagingCursor(mCursor);
		registerForContextMenu(mListView);

        mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent i = new Intent(getApplicationContext(), SingleItemView.class);
				i.putExtra(Database.KEY_ID, arg3);
				startActivity(i);
			}
		});
		
    }
    
    /**
     * We create an onKeyUp listener so that we can handle keyboard shortcuts
     */
    @Override
    public boolean onKeyUp(int keycode, KeyEvent e){
    	switch(keycode){
    	case 42:
    		// "n" for "New"
    		startActivity(QUICK_INPUT);
    		return true;
    	default:
        	//Toast.makeText(getApplicationContext(), ""+keycode, Toast.LENGTH_SHORT).show();
    		return false;
    	}
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	//mCursor.requery();
    	//mListAdapter.notifyDataSetChanged();
    	buildList();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	(getMenuInflater()).inflate(R.menu.main, menu);
    	return true;
    }
   
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()){
    	case R.id.MainMenuSupport:
    		startActivity(SUPPORT);
    		return true;
    	case R.id.MainMenuPreferences:
    		startActivity(PREFERENCES);
    		return true;
    	case R.id.MainMenuAbout:
    		startActivity(ABOUT);
    		return true;
    	case R.id.MainMenuSearch:
    		startActivity(SEARCH);
    		return true;
    	default:
    		Toast.makeText(getApplicationContext(), "Not handled", Toast.LENGTH_SHORT).show();
    		return false;
        }
    }
    
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, ID_EDIT, Menu.NONE, R.string.EntryLongClickEdit).setOnMenuItemClickListener(mMenuItemClickListener);
		menu.add(0, ID_DELETE, Menu.NONE, R.string.EntryLongClickDelete).setOnMenuItemClickListener(mMenuItemClickListener);
		menu.add(0, ID_SHARE, Menu.NONE, R.string.EntryLongClickShare).setOnMenuItemClickListener(mMenuItemClickListener);
		menu.add(0, ID_CLIPBOARD, Menu.NONE, R.string.EntryLongClickCopyToClipboard).setOnMenuItemClickListener(mMenuItemClickListener);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item){
    	final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	JournalEntry selected = JournalEntry.getById(info.id);
    	switch(item.getItemId()){
    	case ID_EDIT:
    		Intent edit = QUICK_INPUT;
    		edit.putExtra(QuickInput.KEY_ID, info.id);
    		edit.putExtra(QuickInput.KEY_ACTION, QuickInput.ACTION_EDIT);
    		startActivity(edit);
    		break;
    	case ID_DELETE:
    		new AlertDialog.Builder(this)
    		.setTitle(R.string.EntryConfirmDeleteTitle)
    		.setMessage(R.string.EntryConfirmDelete)
    		.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					JournalEntry.delete(info.id);
					Toast.makeText(getApplicationContext(), R.string.EntryDeleted, Toast.LENGTH_SHORT).show();
		    		buildList();
				}
			})
    		.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {	}
			})
    		.create()
    		.show();
    		break;
    	case ID_SHARE:
    		Intent share = new Intent(Intent.ACTION_SEND);
			share.setType("text/plain");
			share.putExtra(Intent.EXTRA_TEXT, selected.getBody());
			share.putExtra(Intent.EXTRA_TITLE, selected.getTags());
			startActivity(Intent.createChooser( share, getString(R.string.EntryLongClickShareTitle) ));
    		break;
    	case ID_CLIPBOARD:
    		( (ClipboardManager) getSystemService(CLIPBOARD_SERVICE) ).setText(selected.getBody());
    		Toast.makeText(getApplicationContext(), R.string.CopiedToClipboard, Toast.LENGTH_SHORT).show();
    		break;
    	}
    	return true;
    }
    
    public void buildList(){
    	//paginator.setSource(JournalEntry.getAll());
    	//paginator.reloadPreferences();
    	//mListView.setAdapter( new EntryArrayAdapter(getApplicationContext(), R.layout.main, paginator.getEntries() ) );
    	
    	
    	mCursor = JournalEntry.getEntries();
    	startManagingCursor(mCursor);
    	mListAdapter = new EntriesCursorAdapter(this, mCursor, true);
        mListView.setAdapter(mListAdapter);
        
        setTitle(getString(R.string.MainTitle) + " ("+mCursor.getCount()+")");
    }
    
}