package com.xklakoux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class Ping extends Activity implements OnCheckedChangeListener {
	// String thisLine = new String();

	public static final String PREFS = "MyPrefs";
	SharedPreferences settings;
	AutoCompleteTextView ip;
	EditText intervalValue;
	EditText sizeValue;
	EditText countValue;

	CheckBox interval;
	CheckBox size;
	CheckBox count;
	CheckBox record;

	String output = new String();
	String i = new String();
	String iV = new String();
	String s = new String();
	String sV = new String();
	String cV = new String();
	String r = new String();
	String devhis = new String();

	Button attack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ping);

		ip = (AutoCompleteTextView) findViewById(R.id.etIP);
		intervalValue = (EditText) findViewById(R.id.etInterval);
		sizeValue = (EditText) findViewById(R.id.etSize);
		countValue = (EditText) findViewById(R.id.etCount);
		interval = (CheckBox) findViewById(R.id.cbInterval);
		size = (CheckBox) findViewById(R.id.cbSize);
		count = (CheckBox) findViewById(R.id.cbNumber);
		record = (CheckBox) findViewById(R.id.cbRecord);
		attack = (Button) findViewById(R.id.bPing);

		intervalValue.setEnabled(false);
		sizeValue.setEnabled(false);
		countValue.setEnabled(false);

		interval.setOnCheckedChangeListener(this);
		size.setOnCheckedChangeListener(this);
		count.setOnCheckedChangeListener(this);
		record.setOnCheckedChangeListener(this);

		
		settings = getSharedPreferences(PREFS, MODE_PRIVATE);
		devhis = settings.getString("devhis", "");
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, (String [])new HashSet( Arrays.asList(devhis.split("!@#")) ).toArray( new String[]{}));
        ip.setAdapter(adapter);


		attack.setOnClickListener(new OnClickListener() {
			Thread thread;
			Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					attack.setText("Ping");
					new AlertDialog.Builder(Ping.this).setTitle("result")
							.setMessage(output).setNegativeButton("ok", null)
							.show();
					output = "";
				}
			};

			@Override
			public void onClick(View v) {
				devhis = devhis.concat("!@#" + ip.getText().toString());
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("devhis", devhis);
				
				editor.commit();
				if (intervalValue.isEnabled())
					iV = intervalValue.getText().toString();
				else {
					iV = "";
				}
				Log.d("ping", iV);

				if (sizeValue.isEnabled())
					sV = sizeValue.getText().toString();
				else
					sV = "";
				if (countValue.isEnabled())
					cV = countValue.getText().toString();
				else
					cV = "1";
				Log.d("ping", iV);
				if (interval.isChecked() && iV.length() == 0) {
					Toast iToast = Toast.makeText(getApplicationContext(),
							"Illegal interval!", Toast.LENGTH_SHORT);
					iToast.show();
					return;

				}
				if (interval.isChecked() && Float.valueOf(iV) < 0.2) {
					Toast iToast = Toast.makeText(getApplicationContext(),
							"Inteval be greater than 0.2!", Toast.LENGTH_SHORT);
					iToast.show();
					return;
				}
				if (size.isChecked() && iV.length() == 0) {
					Toast sToast = Toast.makeText(getApplicationContext(),
							"Illegal packet size!", Toast.LENGTH_SHORT);
					sToast.show();
					return;
				}
				if (count.isChecked() && cV.length() == 0) {
					Toast cToast = Toast.makeText(getApplicationContext(),
							"Illegal count value!", Toast.LENGTH_SHORT);
					cToast.show();
					cV = "2";
				}

				if (ip.getText().toString().length() == 0) {
					Toast ipToast = Toast
							.makeText(getApplicationContext(),
									"IP or hostname must be given!",
									Toast.LENGTH_SHORT);
					ipToast.show();
					return;
				}
				final String command = "ping " + i + String.valueOf(iV) + " "
						+ s + String.valueOf(sV) + " " + "-c"
						+ String.valueOf(cV) + " " + r + " "
						+ ip.getText().toString();
				Log.d("ping", command);
				thread = new Thread() { // TODO Fix this thread regarding
										// infinite ping

					@Override
					public void run() {

						Runtime runtime = Runtime.getRuntime();
						Process proc;
						try {
							proc = runtime.exec(command);

							BufferedReader br = new BufferedReader(
									new InputStreamReader(proc.getInputStream()));
							while (true) {

								// enter a loop where we read what the
								// program has to say and wait for it to
								// finish
								// read all the program has to say
								while (br.ready()) {
									String line = new String();
									line = br.readLine();
									output = output.concat(line + "\n");
								}

								try {
									proc.exitValue();
									Log.d("exit", output);
									handler.sendMessage(handler.obtainMessage());

									// if we get here then the process
									// finished executing

									break;
								} catch (IllegalThreadStateException ex) {
									// ignore
								}
							}
							// wait 200ms and try again
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
				thread.start();
				attack.setText("Stop");
			}
		});
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (isChecked) {
			switch (buttonView.getId()) {
			case R.id.cbInterval:
				i = "-i";
				intervalValue.setEnabled(true);
				break;
			case R.id.cbSize:
				s = "-s";
				sizeValue.setEnabled(true);
				break;
			case R.id.cbNumber:
				countValue.setEnabled(true);
				break;
			case R.id.cbRecord:
				r = "-R";
				Log.d("R", "record");
				break;
			}

		} else {
			switch (buttonView.getId()) {
			case R.id.cbInterval:
				i = "";
				intervalValue.setEnabled(false);
				break;
			case R.id.cbSize:
				s = "";
				sizeValue.setEnabled(false);
				break;
			case R.id.cbNumber:
				countValue.setEnabled(false);
				break;
			case R.id.cbRecord:
				r = "";
				countValue.setEnabled(false);
				break;
			}
		}
	}
}
