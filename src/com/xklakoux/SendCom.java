package com.xklakoux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;

public class SendCom extends Activity implements OnCheckedChangeListener {

	public static final String PREFS = "MyPrefs";

	AutoCompleteTextView device;
	AutoCompleteTextView command;
	AutoCompleteTextView login;
	EditText pass;
	CheckBox defaults;
	CheckBox showpass;
	SharedPreferences settings;
	String loginValue = new String();
	String passwordValue = new String();
	String output = new String();
	Button send;
	String devhis = new String();
	String comhis = new String();
	String loghis = new String();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendcom);
		//
		// FileOutputStream fosDev;
		// FileOutputStream fosCom;
		// FileOutputStream fosLog;
		//
		// try {
		// fosDev = openFileOutput(DEVFILE, Context.MODE_PRIVATE);
		// fosCom.write(.getBytes());
		// fosLog`.close();
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e){
		// e.printStackTrace();
		// }

		settings = getSharedPreferences(PREFS, MODE_PRIVATE);
		loginValue = settings.getString("login", "");
		passwordValue = settings.getString("password", "");
		devhis = settings.getString("devhis", "");
		comhis = settings.getString("comhis", "");
		loghis = settings.getString("loghis", "");
		
		login = (AutoCompleteTextView) findViewById(R.id.etLogin);
		pass = (EditText) findViewById(R.id.etPass);
		device = (AutoCompleteTextView) findViewById(R.id.etDevice);
		command = (AutoCompleteTextView) findViewById(R.id.etCommand);
		defaults = (CheckBox) findViewById(R.id.cbDefaults);
		showpass = (CheckBox) findViewById(R.id.cbShowPass);
		send = (Button) findViewById(R.id.bSend);

		ArrayAdapter<String> addev = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, (String [])new HashSet( Arrays.asList(devhis.split("!@#")) ).toArray( new String[]{}));
		device.setAdapter(addev);
		ArrayAdapter<String> adcom = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, (String [])new HashSet( Arrays.asList(comhis.split("!@#")) ).toArray( new String[]{}));
		command.setAdapter(adcom);
		ArrayAdapter<String> adlog = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, (String [])new HashSet( Arrays.asList(loghis.split("!@#")) ).toArray( new String[]{}));
		login.setAdapter(adlog);

		login.setText(loginValue);
		pass.setText(passwordValue);

		defaults.setOnCheckedChangeListener(this);
		showpass.setOnCheckedChangeListener(this);
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String hostname = device.getText().toString();
				String username = login.getText().toString();
				String password = pass.getText().toString();
				String comm = command.getText().toString();
				
				
				try {
					devhis = devhis.concat("!@#" + hostname);
					loghis = loghis.concat("!@#" + username);
					comhis = comhis.concat("!@#" + comm);
					
					
					/* Create a connection instance */

					Connection conn = new Connection(hostname);

					/* Now connect */

					conn.connect();

					/*
					 * Authenticate. If you get an IOException saying something
					 * like
					 * "Authentication method password not supported by the server at this stage."
					 * then please check the FAQ.
					 */

					boolean isAuthenticated = conn.authenticateWithPassword(
							username, password);

					if (isAuthenticated == false)
						throw new IOException("Authentication failed.");

					/* Create a session */

					Session sess = conn.openSession();
					sess.execCommand(comm);

					/*
					 * This basic example does not handle stderr, which is
					 */

					InputStream stdout = new StreamGobbler(sess.getStdout());
					BufferedReader br = new BufferedReader(
							new InputStreamReader(stdout));

					while (true) {
						String line = br.readLine();
						if (line == null)
							break;
						output = output.concat(line + '\n');
					}

					/* Show exit status, if available (otherwise "null") */
					sess.getExitStatus();
					new AlertDialog.Builder(SendCom.this).setTitle("result")
							.setMessage(output).setNegativeButton("ok", null)
							.show();
					output = "";
					
					sess.close();
					conn.close();
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("devhis", devhis);
					editor.putString("comhis", comhis);
					editor.putString("loghis", loghis);
					
					editor.commit();

				} catch (IOException e) {
					e.printStackTrace(System.err);
					new AlertDialog.Builder(SendCom.this).setTitle("Sorry!")
					.setMessage("Something went wrong, try again").setNegativeButton("ok", null)
					.show();
				}
			}
		});

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (buttonView.getId()) {
		case R.id.cbDefaults:
			if (isChecked) {
				login.setEnabled(false);
				login.setText(loginValue);
				pass.setEnabled(false);
				pass.setText(passwordValue);

			} else {
				login.setEnabled(true);
				login.setText("");
				pass.setEnabled(true);
				pass.setText("");
			}
			break;
		case R.id.cbShowPass:
			if (isChecked) {
				pass.setTransformationMethod(null);
			} else {
				pass.setTransformationMethod(new PasswordTransformationMethod());
			}
			break;
		}

	}

}