package nk.code.epoch;


import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class EpochActivity extends ActionBarActivity {
	private ScalaView skala;
	private EpochView epochv;
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
