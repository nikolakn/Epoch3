package nk.code.epoch;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.android.colorpicker.ColorPickerDialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

//Activity for Event data input,edit and validation
public class AddEventActivity extends Activity implements
		DialogInterface.OnDismissListener {

	private EditText name;
	private EditText date;
	private EditText time;
	private Button colorb;
	ColorPickerDialog colorcalendar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_event);
		name = (EditText) findViewById(R.id.editText1);
		date = (EditText) findViewById(R.id.editText2);
		time = (EditText) findViewById(R.id.editText3);
		
		//validate input for name 
		name.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
	            if (actionId == EditorInfo.IME_ACTION_DONE) {
					//name input validation
					if( name.getText().toString().length() == 0 ){
					    name.setError( "Name is required!" );
					    return true;
					} else {
						name.setError(null);
					}
	            }
				return false;
			}
	    });
		
		//validate input for date 
		date.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
	            if (actionId == EditorInfo.IME_ACTION_DONE) {
					//date input validation
					try {
						String pattern = "dd.MM.yyyy";
						DateTime.parse(date.getText().toString(),
								DateTimeFormat.forPattern(pattern));
						date.setError(null);
					} catch (Exception e) {
						date.setError("Format day.month.year e.g.: 19.2.2015");
						return false;
					}
	            }
				return false;
			}
	    });
	    
		//validate input for time 
		time.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
	            if (actionId == EditorInfo.IME_ACTION_DONE) {
					//date input validation
					//time input validation
					try {
						String pattern = "HH:mm";
						DateTime.parse(time.getText().toString(),
								DateTimeFormat.forPattern(pattern));
						time.setError(null);
					} catch (Exception e) {
						time.setError("Format hours:minute e.g.: 19:15");
						return false;
					}
	            }
				return false;
			}
	    });
		//Colours for colour picker dialog
		String color_array[] = { "#33b5e5", "#aa66cc", "#99cc00", "#ffbb33",
				"#ff4444", "#0099cc", "#9933cc", "#669900", "#ff8800",
				"#cc0000", "#ffffff", "#eeeeee", "#cccccc", "#888888" };
		int[] mColor = new int[color_array.length];
		for (int i = 0; i < color_array.length; i++) {
			mColor[i] = Color.parseColor(color_array[i]);
		}
		
		colorcalendar = ColorPickerDialog.newInstance(
				R.string.color_picker_default_title, mColor, 0, 5,
				ColorPickerDialog.SIZE_SMALL);
		
		//save button
		Button save = (Button) findViewById(R.id.saveButton);
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//name input validation
				if( name.getText().toString().length() == 0 ){
				    name.setError( "Name is required!" );
				    return;
				}
				//date input validation
				try {
					String pattern = "dd.MM.yyyy";
					DateTime.parse(date.getText().toString(),
							DateTimeFormat.forPattern(pattern));
				} catch (Exception e) {
					date.setError("Format day.month.year e.g.: 19.2.2015");
					return;
				}
				
				//time input validation
				try {
					String pattern = "HH:mm";
					DateTime.parse(time.getText().toString(),
							DateTimeFormat.forPattern(pattern));
				} catch (Exception e) {
					time.setError("Format hours:minute e.g.: 19:15");
					return;
				}

				
				Intent returnIntent = new Intent();
				returnIntent.putExtra("name", name.getText().toString());
				returnIntent.putExtra("date", date.getText().toString());
				returnIntent.putExtra("time", time.getText().toString());
				setResult(RESULT_OK, returnIntent);
				finish();
			}
		});
		colorb = (Button) findViewById(R.id.colorButton);
		colorb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				colorcalendar.show(getFragmentManager(), "cal");
			}
		});
	}
	//called when colour picker dialog is dismissed 
	@Override
	public void onDismiss(final DialogInterface dialog) {

		int col = colorcalendar.getSelectedColor();
		colorb.setBackgroundColor(col);
		Log.d("nk", Integer.toString(col));
		Log.d("nk", "dismis");
	}

}
