package nk.code.epoch;

import java.util.ArrayList;
import nk.code.data.EpochDatabase;

import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

	// private SharedPreferences mPrefs;

	ListView list;
	ArrayAdapter<String> adapter;
	public final static String EXTRA_MESSAGE = "nk.code.epoch.START";
	public final static String EXTRA_MESSAGE_NEW = "nk.code.epoch.NEW";
	
	private Context con; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);   
		con = this;
		list = (ListView) findViewById(R.id.mainlistview);

		ArrayList<String> listad = new ArrayList<String>(); 
		adapter = new ArrayAdapter<String>(this,R.layout.simplerow, listad);
		// ////////////
		EpochDatabase helper = new EpochDatabase(this);
		SQLiteDatabase database = helper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from "
				+ EpochDatabase.N_TABLE, null);
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			String s=cursor.getString(cursor.getColumnIndex(EpochDatabase.N_IME));
			if(s!=null)
				adapter.add(s);
			cursor.moveToNext();
		}
		
		// make sure to close the cursor
		cursor.close();
		// ///////////
		list.setAdapter(adapter);
		// ListView Item Click Listener
		list.setOnItemClickListener(new OnItemClickListener() {


			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
	               // ListView Clicked item index
	               //int itemPosition     = position;
	               
	               // ListView Clicked item value
	               String  naziv    = (String) list.getItemAtPosition(position);
	               startEpoch(naziv);
			}

         });
		
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int pos, long id) {
                // TODO Auto-generated method stub

                final String  naziv    = (String) list.getItemAtPosition(pos);
             
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            EpochDatabase helper=new EpochDatabase(con);
                    		SQLiteDatabase database=helper.getWritableDatabase();
                    		String naslov = EpochDatabase.N_IME +"='"+naziv+"'";
                    		database.delete(EpochDatabase.N_TABLE, naslov,null);
                    		
                    		String selection = EpochDatabase.U_EPOCH +"='"+naziv+"'";
                    		database.delete(EpochDatabase.U_TABLE, selection,null);
                    		
                    		String sk = EpochDatabase.S_EPOCH +"='"+naziv+"'";
                    		database.delete(EpochDatabase.S_TABLE, sk,null);
                    		osvezi();
                            break;
                           
                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(con);
                builder.setMessage("Are you sure you want to delete this timeline?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();             
                
                return true;
            }
        }); 
		
	}
	@Override
	public void onResume(){
		super.onResume();  // Always call the superclass method first
		osvezi();
	}
	
	private void osvezi(){
		
		if(adapter != null){
			adapter.clear();
			EpochDatabase helper = new EpochDatabase(this);
			SQLiteDatabase database = helper.getWritableDatabase();
			Cursor cursor = database.rawQuery("select * from "
					+ EpochDatabase.N_TABLE, null);
			cursor.moveToFirst();
			
			while (!cursor.isAfterLast()) {
				String s=cursor.getString(cursor.getColumnIndex(EpochDatabase.N_IME));
				if(s!=null)
					adapter.add(s);
				cursor.moveToNext();
			}		
		}
	}
	private void startEpoch(String naziv) {
		Intent intent = new Intent(this, EpochActivity.class);
		String message = naziv;
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}
	private void startNewEpoch(String naziv) {
		Intent intent = new Intent(this, EpochActivity.class);
		String message = naziv;
		intent.putExtra(EXTRA_MESSAGE_NEW, message);
		startActivity(intent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();

		// SharedPreferences.Editor ed = mPrefs.edit();
		// ed.putInt("view_mode", mCurViewMode);
		// ed.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.main_menu_new) {
			Log.d("nk","new");
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Title");

			// Set up the input
			final EditText input = new EditText(this);
			// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
			input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);
			builder.setView(input);

			// Set up the buttons
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        String m_Text = input.getText().toString();
			        startNewEpoch(m_Text);
			    }
			});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        dialog.cancel();
			    }
			});

			builder.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
