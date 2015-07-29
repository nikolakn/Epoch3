package nk.code.epoch;


import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditEventDescActivity extends ActionBarActivity {
	private EditText text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_event_desc);
		text = (EditText) findViewById(R.id.editTextdesk);
		String des=getIntent().getStringExtra("des");
		if (des == null)
			des = "Description";
		text.setText(des);
			
		Button done = (Button) findViewById(R.id.descDoneButton);
		done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//name input validation
				Intent returnIntent = new Intent();	
				returnIntent.putExtra("des", text.getText().toString());
				setResult(RESULT_OK, returnIntent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_event_desc, menu);
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
}
