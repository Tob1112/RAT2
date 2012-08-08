package com.xklakoux;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;

public class Settings extends Activity {

	public static final String PREFS = "MyPrefs";
	boolean saved = false;
	Spinner spinner;
	String maska = new String();
	EditText login;
	EditText pass;
	EditText ipadd;
	CheckBox chkbx;
	Button save;
	TextWatcher textWatcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		SharedPreferences settings = getSharedPreferences(PREFS, MODE_PRIVATE);
		int spinnerValue = settings.getInt("spinner", 6);
		String loginValue = settings.getString("login", "");
		String passwordValue = settings.getString("password", "");
		String ipValue = settings.getString("ip", "");

		// SPINNER
		class MyOnItemSelectedListener implements OnItemSelectedListener {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				maska = IPAddress.getDDMask(Integer.valueOf((String) parent
						.getItemAtPosition(pos)));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				// do nothing
			}
		}

		save = (Button) findViewById(R.id.bSave);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// We need an Editor object to make preference changes.
				// All objects are from android.context.Context
				SharedPreferences settings = getSharedPreferences(PREFS, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt("spinner", spinner.getSelectedItemPosition());
				editor.putString("login", login.getText().toString());
				editor.putString("password", pass.getText().toString());
				editor.putString("ip", ipadd.getText().toString());

				// Commit the edits!
				editor.commit();
				saved = true;
			}
		});

		spinner = (Spinner) findViewById(R.id.sMask);
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter
				.createFromResource(this, R.array.bits,
						android.R.layout.simple_spinner_item);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinnerAdapter);
		spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
		spinner.setSelection(spinnerValue);

		login = (EditText) findViewById(R.id.etLogin);
		login.setText(loginValue);

		pass = (EditText) findViewById(R.id.etPass);
		pass.setText(passwordValue);

		ipadd = (EditText) findViewById(R.id.etIPPool);
		ipadd.setFilters(IPAddress.getIpFilter());
		ipadd.setText(ipValue);

		chkbx = (CheckBox) findViewById(R.id.cbShowPass);
		chkbx.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					//wtf?
					pass.setTransformationMethod(null);
				} else {
					//wtf?
					pass.setTransformationMethod(new PasswordTransformationMethod());
				}

			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Handle the back button
		if (keyCode == KeyEvent.KEYCODE_BACK && saved == false) {
			// Ask the user if they want to quit
			new AlertDialog.Builder(this)
					.setTitle("Are you sure you want to quit?")
					.setMessage("Any changes made won't be saved!")
					.setPositiveButton("Git me outta der",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									// Stop the activity
									Settings.this.finish();
								}

							}).setNegativeButton("Nowait!", null).show();

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}
}
