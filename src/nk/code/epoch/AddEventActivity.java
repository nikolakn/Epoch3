package nk.code.epoch;

import java.util.ArrayList;

import nk.code.data.Event;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.android.colorpicker.ColorPickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

//Activity for Event data input,edit and validation
public class AddEventActivity extends AppCompatActivity implements
		DialogInterface.OnDismissListener {

	private EditText name;
	private EditText date;
	private EditText time;
	private Button colorb;
	ColorPickerDialog colorcalendar;
	private RadioGroup radiog1;
	private RadioGroup radiog2;
	private int boja;
	private Spinner s;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_event);
		name = (EditText) findViewById(R.id.editText1);
		date = (EditText) findViewById(R.id.editText2);
		time = (EditText) findViewById(R.id.editText3);
		colorb = (Button) findViewById(R.id.colorButton);
		radiog1 = (RadioGroup) findViewById(R.id.radio_group1);
		radiog2 = (RadioGroup) findViewById(R.id.radio_group2);
		ArrayList<String> SourceArray = new ArrayList<String>();
		s = (Spinner) findViewById(R.id.Spinner01);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, SourceArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s.setAdapter(adapter);
		adapter.add(Event.Visibility.ALWAYS.getFieldDescription());
		adapter.add(Event.Visibility.HEREANDMINUS.getFieldDescription());
		adapter.add(Event.Visibility.ONLYHERE.getFieldDescription());
		adapter.add(Event.Visibility.HEREANDPLUS.getFieldDescription());

		String argname = getIntent().getStringExtra("name");
		String argdate = getIntent().getStringExtra("date");
		String argtime = getIntent().getStringExtra("time");
		int argcolor = getIntent().getIntExtra("color", Event.DEFEVENTCOLOR);
		int argsize = getIntent().getIntExtra("size", Event.DEFEVENTSIZE);
		int argstyle = getIntent().getIntExtra("style", Event.DEFEVENTSTYLE);

		int argvisibility = getIntent().getIntExtra("visibility",
				Event.Visibility.ALWAYS.ordinal());

		if (savedInstanceState != null) {
			// Restore value of members from saved state
			argname = savedInstanceState.getString("name");
			argdate = savedInstanceState.getString("date");
			argtime = savedInstanceState.getString("time");
			argcolor = savedInstanceState.getInt("boja");
			argsize = savedInstanceState.getInt("size");
			argstyle = savedInstanceState.getInt("style");
			argvisibility = savedInstanceState.getInt("visibility");
		}

		s.setSelection(argvisibility);

		if (name != null)
			name.setText(argname);
		if (date != null)
			date.setText(argdate);
		if (time != null)
			time.setText(argtime);
		colorb.setBackgroundColor(argcolor);
		boja = argcolor;
		switch (argsize) {
		case 0:
			radiog1.check(R.id.radio1);
			break;
		case 1:
			radiog1.check(R.id.radio2);
			break;
		case 2:
			radiog1.check(R.id.radio3);
			break;
		case 3:
			radiog1.check(R.id.radio4);
			break;
		case 4:
			radiog1.check(R.id.radio5);
			break;
		}
		switch (argstyle) {
		case 0:
			radiog2.check(R.id.sradio1);
			break;
		case 1:
			radiog2.check(R.id.sradio2);
			break;
		case 2:
			radiog2.check(R.id.sradio3);
			break;
		case 3:
			radiog2.check(R.id.sradio4);
			break;

		}

		String color_array[] = { "#ff8000", "#fcb314", "#067ab4", "#00ff00",
				"#f2ff00", "#19e3d9", "#52a74f", "#fedf83", "#9c2902",
				"#cc0000", "#800080", "#696969", "#95a484", "#00ffff" };
		int[] mColor = new int[color_array.length];
		for (int i = 0; i < color_array.length; i++) {
			mColor[i] = Color.parseColor(color_array[i]);
		}
		colorcalendar = ColorPickerDialog.newInstance(
				R.string.color_picker_default_title, mColor, 0, 5,
				ColorPickerDialog.SIZE_SMALL);

		// validate input for name
		name.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					// name input validation
					if (name.getText().toString().length() == 0) {
						name.setError("Name is required!");
						return true;
					} else {
						name.setError(null);
					}
				}
				return false;
			}
		});

		// validate input for date
		date.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					// date input validation
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

		// validate input for time
		time.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					// date input validation
					// time input validation
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

		// save button
		Button save = (Button) findViewById(R.id.saveButton);
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// name input validation
				if (name.getText().toString().length() == 0) {
					name.setError("Name is required!");
					return;
				}
				// date input validation
				try {
					String pattern = "dd.MM.yyyy";
					DateTime.parse(date.getText().toString(),
							DateTimeFormat.forPattern(pattern));
				} catch (Exception e) {
					date.setError("Format day.month.year e.g.: 19.2.2015");
					return;
				}

				// time input validation
				try {
					String pattern = "HH:mm";
					DateTime.parse(time.getText().toString(),
							DateTimeFormat.forPattern(pattern));
				} catch (Exception e) {
					time.setError("Format hours:minute e.g.: 19:15");
					return;
				}

				Intent returnIntent = new Intent();

				int radioButtonID = radiog1.getCheckedRadioButtonId();
				View radioButton = radiog1.findViewById(radioButtonID);
				int a1 = radiog1.indexOfChild(radioButton);

				int radioButtonID2 = radiog2.getCheckedRadioButtonId();
				View radioButton2 = radiog2.findViewById(radioButtonID2);
				int a2 = radiog2.indexOfChild(radioButton2);

				int vis = (int) s.getSelectedItemId();
				if (vis == android.widget.AdapterView.INVALID_ROW_ID)
					vis = 0;
				returnIntent.putExtra("name", name.getText().toString());
				returnIntent.putExtra("date", date.getText().toString());
				returnIntent.putExtra("time", time.getText().toString());
				returnIntent.putExtra("boja", boja);
				returnIntent.putExtra("size", a1);
				returnIntent.putExtra("style", a2);
				returnIntent.putExtra("visibility", vis);
				setResult(RESULT_OK, returnIntent);
				finish();
			}
		});

		colorb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Colours for colour picker dialog
				colorcalendar.show(getFragmentManager(), "cal");
			}
		});
	}

	// called when colour picker dialog is dismissed
	@Override
	public void onDismiss(final DialogInterface dialog) {

		int col = colorcalendar.getSelectedColor();
		colorb.setBackgroundColor(col);
		boja = col;
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(savedInstanceState);
		// Save the user's current game state
		int radioButtonID = radiog1.getCheckedRadioButtonId();
		View radioButton = radiog1.findViewById(radioButtonID);
		int a1 = radiog1.indexOfChild(radioButton);

		int radioButtonID2 = radiog2.getCheckedRadioButtonId();
		View radioButton2 = radiog2.findViewById(radioButtonID2);
		int a2 = radiog2.indexOfChild(radioButton2);

		int vis = (int) s.getSelectedItemId();
		if (vis == android.widget.AdapterView.INVALID_ROW_ID)
			vis = 0;

		savedInstanceState.putString("name", name.getText().toString());
		savedInstanceState.putString("date", date.getText().toString());
		savedInstanceState.putString("time", time.getText().toString());
		savedInstanceState.putInt("boja", boja);
		savedInstanceState.putFloat("size", a1);
		savedInstanceState.putFloat("style", a2);
		savedInstanceState.putFloat("visibility", vis);

	}

}
