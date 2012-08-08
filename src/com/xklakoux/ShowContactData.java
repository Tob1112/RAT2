package com.xklakoux;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ShowContactData extends Activity {
	String whereClause = new String();
	DBHelper dbhelper;
	SQLiteDatabase db;
	Cursor cursor;
	SimpleCursorAdapter adapter;
	ListView output;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		whereClause = getIntent().getExtras().get("whereClause").toString();

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data);
//		Toast a = Toast.makeText(getApplicationContext(), whereClause,
//				Toast.LENGTH_SHORT);
//		a.show();
		
		dbhelper = new DBHelper(this);
		db = dbhelper.getWritableDatabase();

		cursor = db.query(DBHelper.TABLE_NAME3, new String[] {
				"firstame", DBHelper.COL_SECONDN, DBHelper.COL_TELNUM,
				DBHelper.COL_STREET, DBHelper.COL_BUILDNR, DBHelper.COL_ROOMNR,
				DBHelper.COL_CODE, DBHelper.COL_CITY, DBHelper.COL_SWROOMNR,
				DBHelper.COL_SWITCHNR, DBHelper.COL_PORTNR, DBHelper.COL_MAC },
				whereClause , null, null, null, null);
		startManagingCursor(cursor);
		Log.d("tag","\"");
		if(cursor.getCount()!=0){
		
		String[] from = { "firstame", DBHelper.COL_SECONDN, DBHelper.COL_TELNUM,
				DBHelper.COL_STREET, DBHelper.COL_BUILDNR, DBHelper.COL_ROOMNR,
				DBHelper.COL_CODE, DBHelper.COL_CITY, DBHelper.COL_SWROOMNR,
				DBHelper.COL_SWITCHNR, DBHelper.COL_PORTNR, DBHelper.COL_MAC };

		int[] to = { R.id.tvFName, R.id.tvSName, R.id.tvTelnum, R.id.tvStreet,
				R.id.tvBuild, R.id.tvRoom, R.id.tvCode, R.id.tvCity, R.id.tvSWRoom,
				R.id.tvSWNr, R.id.tvPort, R.id.tvMAC };
		
		output = (ListView) findViewById(R.id.lvListV);
		
		adapter = new SimpleCursorAdapter(this, R.layout.showcontactdata, null,
				from, to);
		output.setAdapter(adapter);
		adapter.changeCursor(cursor);
		db.close();
		}else{
			setContentView(R.layout.nothing);
		}
	}
}
