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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

	@Override
	public void onDismiss(final DialogInterface dialog) {

		int col = colorcalendar.getSelectedColor();
		colorb.setBackgroundColor(col);
		Log.d("nk", Integer.toString(col));
		Log.d("nk", "dismis");
	}
}
