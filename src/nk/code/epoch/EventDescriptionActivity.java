package nk.code.epoch;


import android.support.v7.app.ActionBarActivity;
import android.text.Html;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EventDescriptionActivity extends ActionBarActivity {

	private TextView text;
	private String des;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_description);
		text = (TextView) findViewById(R.id.editTextdec1);
		des = "<font size=\"40\" face=\"arial\" color=\"red\">ggg</font><br><br>";
		des=getIntent().getStringExtra("des");
		if (des == null)
			des = "Description";
		text.setText(Html.fromHtml(des));

		//edit button
		Button edit = (Button) findViewById(R.id.descEditButton);
		edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//name input validation
				startEdit();
			}
		});
		//save button
		Button save = (Button) findViewById(R.id.descSaveButton1);
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//name input validation
				Intent returnIntent = new Intent();	
				returnIntent.putExtra("des", des);
				setResult(RESULT_OK, returnIntent);
				
				finish();
			}
		});

	}

	void startEdit() {
    	Intent i = new Intent(this, EditEventDescActivity.class);
    	i.putExtra("des", des);
    	startActivityForResult(i, 1);		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_description, menu);
		return true;
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
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		    if (requestCode == 1) {
		        if(resultCode == RESULT_OK){
		        	 String mtext=data.getStringExtra("des");
		        	 des = mtext;
		        	 text.setText(Html.fromHtml(des));
		        	     	 
		        }

		        if (resultCode == RESULT_CANCELED) {
		            //Write your code if there's no result
		        }
		    }
		    

	    }
}
