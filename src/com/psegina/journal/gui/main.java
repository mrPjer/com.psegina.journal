package com.psegina.journal.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

import com.psegina.journal.App;
import com.psegina.journal.R;
import com.psegina.journal.data.JournalEntry;

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
	
	private OnMenuItemClickListener mMenuItemClickListener; 

	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        setContentView(R.layout.main);        
            
        new JournalEntry.Paginator(
        		(ListView) findViewById(R.id.MainTextView),
        		this
        );
        
    }
	
    /**
     * We create an onKeyUp listener so that we can handle keyboard shortcuts
     */
    @Override
    public boolean onKeyUp(int keycode, KeyEvent e){
    	switch(keycode){
    	case 42:
    		// "n" for "New"
    		startActivity(App.QUICK_INPUT);
    		return true;
    	default:
        	//Toast.makeText(getApplicationContext(), ""+keycode, Toast.LENGTH_SHORT).show();
    		return false;
    	}
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
    		startActivity(App.SUPPORT);
    		return true;
    	case R.id.MainMenuPreferences:
    		startActivity(App.PREFERENCES);
    		return true;
    	case R.id.MainMenuAbout:
    		startActivity(App.ABOUT);
    		return true;
    	/*
    	case R.id.MainMenuSearch:
    		startActivity(SEARCH);
    		return true;
 		*/
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
    		Intent edit = App.QUICK_INPUT;
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
    
}