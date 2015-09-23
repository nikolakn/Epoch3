package nk.code.epoch;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import nk.code.data.EpochDatabase;
import nk.code.data.Event;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
//import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//import android.widget.EditText;

public class EpochActivity extends AppCompatActivity {
	private ScalaView skala;
	private EpochView epochv;
	public static String fileName = "nktemp.ser";
	//private String dialogRez;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_epoch);

		skala = (ScalaView) findViewById(R.id.skala);
		epochv = (EpochView) findViewById(R.id.epoch);

		String name = getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE);
		String newname = getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE_NEW);
		if(name != null){
			epochv.init(skala);
			epochv.newDoc(name);
			skala.init(epochv);
			openEpoch();
			getIntent().removeExtra(MainActivity.EXTRA_MESSAGE); 
			
		}
		else if (newname != null){	
			epochv.init(skala);
			epochv.newDoc(newname);
			skala.init(epochv);		
			getIntent().removeExtra(MainActivity.EXTRA_MESSAGE_NEW); 
		}
		else {
			epochv.init(skala);
			skala.init(epochv);	
			try {
				FileInputStream fis = getApplicationContext().openFileInput(fileName);
				ObjectInputStream is = new ObjectInputStream(fis);
				epochv.getDoc().deSerialize(is);
				//skala.deSerialize(is);
				is.close();
				fis.close();
			} catch (Exception e) {
				Log.e("nk", e.toString());
			}
			
			openEpoch();
		}
		skala.invalidate();

	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save the user's current game state
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.epoch, menu);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop(); // Always call the superclass method first
		
		FileOutputStream fos;
		try {
			fos = getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			epochv.getDoc().serialize(os);
			skala.serialize(os);
			os.close();
			fos.close();
		} catch (Exception e) {
			Log.e("nk", e.toString());
		}
		save();
	}

	@SuppressLint("InflateParams")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		/*
		if (id == R.id.epoch_menu_new) {

			LayoutInflater li = LayoutInflater.from(this);
			View promptsView = li.inflate(R.layout.prompts, null);

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

			alertDialogBuilder.setView(promptsView);

			final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

			// set dialog message
			alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {


				public void onClick(DialogInterface dialog, int id) {
					dialogRez=(userInput.getText()).toString();
					newEpoch(dialogRez);
				}

			}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
			return true;
		}
		if (id == R.id.epoch_menu_open) {
			openEpoch();
			return true;
		}
		if (id == R.id.epoch_menu_save) {
			// get prompts.xml view
			save();
			return true;
		}
		*/

		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Context Menu");
		menu.add(0, v.getId(), 0, "Action 1");
		menu.add(0, v.getId(), 0, "Action 2");
	}

	public void StartAddEventActivity(String name, String date, String time, int color, int size, int style,
			int visibility) {
		Intent i = new Intent(this, AddEventActivity.class);
		i.putExtra("name", name);
		i.putExtra("date", date);
		i.putExtra("time", time);
		i.putExtra("color", color);
		i.putExtra("size", size);
		i.putExtra("style", style);
		i.putExtra("visibility", visibility);
		startActivityForResult(i, 2);
	}

	public void StartAddEpochActivity(String name, String date, String time, String endDate, String endTime, int color,
			int size, int style, int visibility) {
		Intent i = new Intent(this, AddEpochActivity.class);
		i.putExtra("name", name);
		i.putExtra("date", date);
		i.putExtra("time", time);
		i.putExtra("dateend", endDate);
		i.putExtra("timeend", endTime);
		i.putExtra("color", color);
		i.putExtra("size", size);
		i.putExtra("style", style);
		i.putExtra("visibility", visibility);
		startActivityForResult(i, 5);
	}

	public void StartAddEventActivity(String date, String time) {
		Intent i = new Intent(this, AddEventActivity.class);
		i.putExtra("date", date);
		i.putExtra("time", time);
		startActivityForResult(i, 1);
	}

	public void StartAddEpochActivity(String date, String time, String endDate, String endTime) {
		Intent i = new Intent(this, AddEpochActivity.class);
		i.putExtra("date", date);
		i.putExtra("time", time);
		i.putExtra("dateend", endDate);
		i.putExtra("timeend", endTime);
		startActivityForResult(i, 4);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				String name = data.getStringExtra("name");
				String date = data.getStringExtra("date");
				String time = data.getStringExtra("time");
				int boja = data.getIntExtra("boja", Event.DEFEVENTCOLOR);
				int size = data.getIntExtra("size", Event.DEFEVENTSIZE);
				int style = data.getIntExtra("style", Event.DEFEVENTSTYLE);
				int vis = data.getIntExtra("visibility", Event.Visibility.ALWAYS.ordinal());
				boolean vischange=false;
				if(vis<0)
					vischange = true;
				try {
					String pattern = "dd.MM.yyyy HH:mm";
					DateTime dateTime = DateTime.parse(date + " " + time, DateTimeFormat.forPattern(pattern));
					// Log.i("nk", dateTime.toString("dd.MM.yyyy HH:mm"));
					epochv.addDocEvent(name, dateTime, boja, size, style, vis,vischange);
				} catch (Exception e) {

				}

			}

		}

		if (requestCode == 2) {
			if (resultCode == RESULT_OK) {
				String name = data.getStringExtra("name");
				String date = data.getStringExtra("date");
				String time = data.getStringExtra("time");
				int boja = data.getIntExtra("boja", Event.DEFEVENTCOLOR);
				int size = data.getIntExtra("size", Event.DEFEVENTSIZE);
				int style = data.getIntExtra("style", Event.DEFEVENTSTYLE);
				int vis = data.getIntExtra("visibility", Event.Visibility.ALWAYS.ordinal());
				boolean vischange=false;
				//Log.d("nk",Integer.toString(vis));
				if(vis<0)
					vischange = true;
				try {
					String pattern = "dd.MM.yyyy HH:mm";
					DateTime dateTime = DateTime.parse(date + " " + time, DateTimeFormat.forPattern(pattern));
					epochv.EditDocEvent(name, dateTime, boja, size, style, vis,vischange);
				} catch (Exception e) {

				}
			}
		}
		// edit description
		if (requestCode == 3) {
			if (resultCode == RESULT_OK) {

				String des = data.getStringExtra("des");
				// Log.d("nk", des);
				epochv.EditEpochDesc(des);

			}
		}

		// add epoch
		if (requestCode == 4) {
			if (resultCode == RESULT_OK) {
				String name = data.getStringExtra("name");
				String date = data.getStringExtra("date");
				String time = data.getStringExtra("time");
				String dateend = data.getStringExtra("dateend");
				String timeend = data.getStringExtra("timeend");
				int boja = data.getIntExtra("boja", Event.DEFEVENTCOLOR);
				int size = data.getIntExtra("size", Event.DEFEVENTSIZE);
				int style = data.getIntExtra("style", Event.DEFEVENTSTYLE);
				
				int vis = data.getIntExtra("visibility", Event.Visibility.ALWAYS.ordinal());
				boolean vischange=false;
				if(vis<0)
					vischange = true;

				try {
					String pattern = "dd.MM.yyyy HH:mm";
					DateTime dateTime2 = DateTime.parse(date + " " + time, DateTimeFormat.forPattern(pattern));
					// Log.i("nk", dateTime.toString("dd.MM.yyyy HH:mm"));
					DateTime dateTime = DateTime.parse(dateend + " " + timeend, DateTimeFormat.forPattern(pattern));

					epochv.addDocEpoch(name, dateTime, dateTime2, boja, size, style, vis,vischange);
				} catch (Exception e) {

				}

			}

		}

		if (requestCode == 5) {
			if (resultCode == RESULT_OK) {
				String name = data.getStringExtra("name");
				String date = data.getStringExtra("date");
				String time = data.getStringExtra("time");
				String dateend = data.getStringExtra("dateend");
				String timeend = data.getStringExtra("timeend");
				int boja = data.getIntExtra("boja", Event.DEFEVENTCOLOR);
				int size = data.getIntExtra("size", Event.DEFEVENTSIZE);
				int style = data.getIntExtra("style", Event.DEFEVENTSTYLE);
				int vis = data.getIntExtra("visibility", Event.Visibility.ALWAYS.ordinal());
				boolean vischange=false;
				if(vis<0)
					vischange = true;
				
				try {
					String pattern = "dd.MM.yyyy HH:mm";
					DateTime dateTime2 = DateTime.parse(date + " " + time, DateTimeFormat.forPattern(pattern));
					DateTime dateTime = DateTime.parse(dateend + " " + timeend, DateTimeFormat.forPattern(pattern));
					epochv.EditDocEpoch(name, dateTime, dateTime2, boja, size, style, vis,vischange);
				} catch (Exception e) {

				}
			}
		}

		if (resultCode == RESULT_CANCELED) {
			// Write your code if there's no result
		}

	}

	public void StartEventDesActivity(String description,String date) {
		Intent i = new Intent(this, EventDescriptionActivity.class);
		i.putExtra("des", description);
		i.putExtra("date", date);
		startActivityForResult(i, 3);
	}

	protected void save() {
		
		EpochDatabase helper=new EpochDatabase(this);
		SQLiteDatabase database=helper.getWritableDatabase();
		epochv.saveToDatabase(database);
		skala.saveToDatabase(database, epochv.getDoc().getTitle());

	}
	protected void newEpoch(String dialogRez) {
		Log.d("nk",dialogRez);

	}
	protected void openEpoch() {
		//Log.d("nk","Open");
		EpochDatabase helper=new EpochDatabase(this);
		SQLiteDatabase database=helper.getWritableDatabase();
		epochv.getDoc().openFromDatabase(database);
		skala.LoadFromDatabase(database, epochv.getDoc().getTitle());
	}
}
