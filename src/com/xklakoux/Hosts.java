package com.xklakoux;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Hosts extends Activity implements OnClickListener {

	Cursor cursor;
	SimpleCursorAdapter adapter;
	EditText ipadd;
	EditText howmany;
	Button adder;
	ListView numbers;
	SQLiteDatabase db;
	Spinner spinner;
	String mask = new String();
	DBHelper dbhelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.host);

		ipadd = (EditText) findViewById(R.id.etIpAddress);
		ipadd.setFilters(IPAddress.getIpFilter());

		//SPINNER
		class MyOnItemSelectedListener implements OnItemSelectedListener {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				mask = IPAddress.getDDMask(Integer.valueOf((String) parent
						.getItemAtPosition(pos)));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				// do nothing
			}
		}
		spinner = (Spinner) findViewById(R.id.sMask);
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter
				.createFromResource(this, R.array.bits,
						android.R.layout.simple_spinner_item);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinnerAdapter);
		spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
		spinner.setSelection(6);
		
		
		mask = IPAddress.getDDMask(Integer.valueOf((String) spinner
				.getItemAtPosition(6))); //set mask for 24 by default
		howmany = (EditText) findViewById(R.id.etHowMany);
		adder = (Button) findViewById(R.id.bAdd);
		adder.setOnClickListener(this);

		numbers = (ListView) findViewById(R.id.lvNumbers);
		registerForContextMenu(numbers);

		dbhelper = new DBHelper(this);
		db = dbhelper.getWritableDatabase();
		// db.delete(DBHelper.TABLE_NAME, null, null);

		cursor = db.query(DBHelper.TABLE_NAME, null, null, null, null, null,
				DBHelper.COL_HOST + " desc");

		startManagingCursor(cursor);

		String[] from = { DBHelper.COL_PIC, DBHelper.COL_HOST,
				DBHelper.COL_HOWMANY };
		int[] to = { R.id.ivPic, R.id.tvCount, R.id.tvHowMany };

		adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, from, to);
		adapter.setViewBinder(new MyViewBinder());

		numbers.setAdapter(adapter);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.bCalculate:
			// i have to collect data to find out if it fits in the pool
			Cursor ret = db.query(DBHelper.TABLE_NAME, new String[] { "_id",
					DBHelper.COL_HOST, DBHelper.COL_HOWMANY }, null, null,
					null, null, DBHelper.COL_HOST + " desc");
			int[] num = new int[ret.getCount()];
			int[] howm = new int[ret.getCount()];
			ret.moveToFirst();

			int d = 0;
			while (!ret.isAfterLast()) { 
				num[d] = ret.getInt(1);
				howm[d++] = ret.getInt(2);
				ret.moveToNext();
			}
			
			ret.close();
			//some checkers
			if (!IPAddress.isCorrectIP(ipadd.getText().toString())) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Invalid IP Address", Toast.LENGTH_SHORT);
				toast.show();
				return false;
			} else if (spinner.getSelectedItemPosition() == 23) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Bad mask", Toast.LENGTH_SHORT);
				toast.show();
			} else if (!IPAddress.fits(num, howm, IPAddress.getCidrMask(mask))) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Sorry, but it won't fit", Toast.LENGTH_SHORT);
				toast.show();
			} else {
				//store mask and ip in static (global) variables
				DBHelper.MASK = mask;
				DBHelper.IP = ipadd.getText().toString();
				startActivity(new Intent("com.xklakoux.HOSTSOUT"));
			}
			break;
		case R.id.bClear:
			db.delete(DBHelper.TABLE_NAME, null, null);
			tidyUp();
			break;
		}
		return true;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bAdd:
			if (howmany.getText().length() != 0) {
				ContentValues values = new ContentValues();
				Cursor ret = db
						.query(DBHelper.TABLE_NAME,
								new String[] { "_id", DBHelper.COL_HOST,
										DBHelper.COL_HOWMANY },
								DBHelper.COL_HOST
										+ "="
										+ Integer.valueOf(howmany.getText()
												.toString()), null, null, null,
								null);
				//if there's already this much, add +1
				if (!ret.moveToFirst()) { 

					values.put(DBHelper.COL_HOST,
							Integer.valueOf(howmany.getText().toString()));
					values.put(DBHelper.COL_HOWMANY, 1);
					values.put(DBHelper.COL_PIC,
							Integer.valueOf(howmany.getText().toString()));
					db.insert(DBHelper.TABLE_NAME, null, values);
					ret.close();
				} else { //if no get a new one
					values.put(DBHelper.COL_HOWMANY, ret.getInt(2) + 1);
					ret.close();
					db.update(
							DBHelper.TABLE_NAME,
							values,
							DBHelper.COL_HOST
									+ "="
									+ Integer.valueOf(howmany.getText()
											.toString()), null);
				}

				tidyUp();
				howmany.setText("");
			}
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		switch (item.getItemId()) {
		case R.id.iAdd1: //add +1 do given row
			Cursor ret = db.query(DBHelper.TABLE_NAME, new String[] { "_id",
					DBHelper.COL_HOST, DBHelper.COL_HOWMANY }, "_id" + "="
					+ info.id, null, null, null, null);

			ret.moveToFirst();

			ContentValues values = new ContentValues();
			values.put(DBHelper.COL_HOST, ret.getInt(1));
			values.put(DBHelper.COL_HOWMANY, ret.getInt(2) + 1);
			ret.close();
			db.update(DBHelper.TABLE_NAME, values, "_id" + "=" + info.id, null);
			tidyUp();
			return true;

		case R.id.iDelete:
			db.delete(DBHelper.TABLE_NAME, "_id" + "=" + info.id, null);
			tidyUp();
			return true;

		default:
			return super.onContextItemSelected(item);
		}
	}
	/** tidy up the things with database */
	void tidyUp() {
		cursor = db.query(DBHelper.TABLE_NAME, null, null, null, null, null,
				DBHelper.COL_HOST + " desc");
		startManagingCursor(cursor);
		adapter.changeCursor(cursor);
	}

};