package nk.code.epoch;

import java.util.ArrayList;

import nk.code.data.EpochDatabase;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

	// private SharedPreferences mPrefs;

	ListView list;
	public final static String EXTRA_MESSAGE = "nk.code.epoch.START";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		list = (ListView) findViewById(R.id.mainlistview);
		ArrayList<String> listad = new ArrayList<String>();
		// ////////////
		EpochDatabase helper = new EpochDatabase(this);
		SQLiteDatabase database = helper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from "
				+ EpochDatabase.N_TABLE, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			listad.add(cursor.getString(cursor
					.getColumnIndex(EpochDatabase.N_IME)));
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();

		// ///////////

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, listad);
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
		
	}

	private void startEpoch(String naziv) {
		Intent intent = new Intent(this, EpochActivity.class);
		String message = naziv;
		intent.putExtra(EXTRA_MESSAGE, message);
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
		return super.onOptionsItemSelected(item);
	}
}
