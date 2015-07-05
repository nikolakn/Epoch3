package nk.code.epoch;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import nk.code.data.Event;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View; 


public class EpochActivity extends ActionBarActivity {
	private ScalaView skala;
	private EpochView epochv;
	public static String fileName = "nktemp.ser";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_epoch);
		
		skala = (ScalaView) findViewById(R.id.skala);
		epochv = (EpochView) findViewById(R.id.epoch);
		
		epochv.init(skala);
		skala.init(epochv);
		
		if (savedInstanceState != null) {
	        // Restore value of members from saved state
			skala.setLen(savedInstanceState.getDouble("skala_len"));
			skala.setDy(savedInstanceState.getDouble("skala_dy"));
			skala.setZoomLen(savedInstanceState.getInt("skala_zoomlen"));
			skala.setPeriod(savedInstanceState.getInt("skala_period"));
			skala.setScaleFactor(savedInstanceState.getFloat("skala_mScaleFactor"));
	    } 
		
		try {
			FileInputStream fis =  getApplicationContext().openFileInput(fileName);
		    epochv.getDoc().deSerialize(fis);
		    fis.close();
		} catch (Exception e) {
			Log.e("nk",e.toString());
		}
		skala.invalidate();

	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    // Save the user's current game state
	    savedInstanceState.putDouble("skala_dy", skala.getDy());
	    savedInstanceState.putDouble("skala_len", skala.getLen());
	    savedInstanceState.putInt("skala_zoomlen", skala.getZoomLen());
	    savedInstanceState.putInt("skala_period", skala.getPeriod());
	    savedInstanceState.putFloat("skala_mScaleFactor", skala.getScaleFactor());
	    // Always call the superclass so it can save the view hierarchy state
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
	    super.onStop();  // Always call the superclass method first
	    FileOutputStream fos;
		try {
			fos = getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
		    epochv.getDoc().serialize(fos);
		    fos.close();
		} catch (Exception e) {
			Log.e("nk",e.toString());
		}   
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
	
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
	super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Context Menu");
		menu.add(0, v.getId(), 0, "Action 1");
		menu.add(0, v.getId(), 0, "Action 2");
	}
    public void StartAddEventActivity(String name,String date,String time,int color,int size,int style){
    	Intent i = new Intent(this, AddEventActivity.class);
    	i.putExtra("name", name);
    	i.putExtra("date", date);
    	i.putExtra("time", time);
    	i.putExtra("color", color);
    	i.putExtra("size", size);
    	i.putExtra("style", style);
    	
    	startActivityForResult(i, 2);
    }
    public void StartAddEventActivity(){
    	Intent i = new Intent(this, AddEventActivity.class);
    	startActivityForResult(i, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == 1) {
	        if(resultCode == RESULT_OK){
	        	 String name=data.getStringExtra("name");
	        	 String date=data.getStringExtra("date");
	        	 String time=data.getStringExtra("time");
	        	 int boja=data.getIntExtra("boja",Event.DEFEVENTCOLOR);
	        	 int size=data.getIntExtra("size",Event.DEFEVENTSIZE);
	        	 int style=data.getIntExtra("style",Event.DEFEVENTSTYLE);
	        	 Log.i("nk1", Integer.toString(boja));
	        	 Log.i("nk1", Integer.toString(size));
	        	 Log.i("nk1", Integer.toString(style));
	        	 try{
	        	 String pattern = "dd.MM.yyyy HH:mm";
	             DateTime dateTime  = DateTime.parse(date+" "+time, DateTimeFormat.forPattern(pattern));
	             //Log.i("nk", dateTime.toString("dd.MM.yyyy HH:mm"));
	             epochv.addEpoch(name,dateTime,boja,size,style);
	        	 } catch(Exception e){
	        		 
	        	 }
	        	     	 
	        }

	        if (resultCode == RESULT_CANCELED) {
	            //Write your code if there's no result
	        }
	    }
	    
	    if (requestCode == 2) {
	        if(resultCode == RESULT_OK){
	        	 String name=data.getStringExtra("name");
	        	 String date=data.getStringExtra("date");
	        	 String time=data.getStringExtra("time");
	        	 int boja=data.getIntExtra("boja",Event.DEFEVENTCOLOR);
	        	 int size=data.getIntExtra("size",Event.DEFEVENTSIZE);
	        	 int style=data.getIntExtra("style",Event.DEFEVENTSTYLE);
	        	 Log.i("nk2", Integer.toString(boja));
	        	 Log.i("nk2", Integer.toString(size));
	        	 Log.i("nk2", Integer.toString(style));
	        	 try{
	        	 String pattern = "dd.MM.yyyy HH:mm";
	             DateTime dateTime  = DateTime.parse(date+" "+time, DateTimeFormat.forPattern(pattern));
	             epochv.EditEpoch(name,dateTime,boja,size,style);
	        	 } catch(Exception e){
	        		 
	        	 }	     	 
	        }

	        if (resultCode == RESULT_CANCELED) {
	            //Write your code if there's no result
	        }
	    }
    }    

}
