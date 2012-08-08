package com.xklakoux;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class HostsOut extends Activity {

	Cursor cursor;
	SimpleCursorAdapter adapter;
	DBHelper dbhelper;
	SQLiteDatabase db;
	ListView output;

	EditText howmany;
	EditText start;
	EditText end;
	EditText usable;
	EditText net;
	EditText broad;
	EditText wildcard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cal);

		dbhelper = new DBHelper(this);
		db = dbhelper.getWritableDatabase();
		db.delete(DBHelper.TABLE_NAME2, null, null);

		Cursor ret = db.query(DBHelper.TABLE_NAME, new String[] { "_id",
				DBHelper.COL_HOST, DBHelper.COL_HOWMANY }, null, null, null,
				null, DBHelper.COL_HOST + " desc");
		ret.moveToFirst();
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

		cursor = db.query(DBHelper.TABLE_NAME2, null, null, null, null, null,
				null);
		startManagingCursor(cursor);

		String[] from = { DBHelper.COL_HMANY, DBHelper.COL_START,
				DBHelper.COL_END, DBHelper.COL_USABLE, DBHelper.COL_NET,
				DBHelper.COL_BROAD, DBHelper.COL_WILD, DBHelper.COL_MASK };

		int[] to = { R.id.tvOHostNum, R.id.tvOStart, R.id.tvOEnd, R.id.tvOHost,
				R.id.tvONet, R.id.tvOBroad, R.id.tvOWild, R.id.tvOMask };

		output = (ListView) findViewById(R.id.lvOutput);

		adapter = new SimpleCursorAdapter(this, R.layout.calrow, null, from, to);
		output.setAdapter(adapter);
		
		int mask;

		// get pool (first value) into temporary variable
		String temp = IPAddress.getNetwork(DBHelper.IP, DBHelper.MASK);
		for (int ex = 0; ex < d; ex++) {

			for (int in = 0; in < howm[ex]; in++) {

				ContentValues values = new ContentValues();

				mask = IPAddress.hostsToMask(num[ex],
						IPAddress.getCidrMask(DBHelper.MASK));

				values.put(DBHelper.COL_NET, temp);
				values.put(DBHelper.COL_BROAD,
						IPAddress.getBroadcast(temp, mask));
				values.put(DBHelper.COL_USABLE,
						IPAddress.getUsableHostsCount(mask) - num[ex]);
				values.put(DBHelper.COL_WILD, IPAddress.getWildcard(mask));
				values.put(DBHelper.COL_HMANY, num[ex]);
				values.put(DBHelper.COL_END,
						IPAddress.getLastAddress(temp, mask));
				values.put(DBHelper.COL_START, IPAddress.getFirstAddress(temp));
				values.put(DBHelper.COL_MASK, IPAddress.getDDMask(mask) + " /"
						+ mask);

				db.insert(DBHelper.TABLE_NAME2, null, values);
				cursor = db.query(DBHelper.TABLE_NAME2, null, null, null, null,
						null, null);
				startManagingCursor(cursor);

				adapter.changeCursor(cursor);

				/**
				 * that's a bit messy but the main goal is to increment each
				 * octet one by one from last to first when making new subnets
				 */
				if (Integer.valueOf(IPAddress.getOct(
						IPAddress.getBroadcast(temp, mask), 4)) == 255 // if 4th
																		// is
																		// full
																		// then
																		// increment
																		// 3th
						&& IPAddress.getCidrMask(DBHelper.MASK) < 24) {

					// if 3th is full then increment 2th, should i get further?
					if (Integer.valueOf(IPAddress.getOct(
							IPAddress.getBroadcast(temp, mask), 3)) == 255
							&& IPAddress.getCidrMask(DBHelper.MASK) < 16) {
						temp = IPAddress
								.setOct(IPAddress.getBroadcast(temp, mask),
										2,
										IPAddress.getOct(IPAddress
												.getBroadcast(temp, mask), 2) + 1);
						temp = IPAddress.setOct(temp, 4, 0);
						temp = IPAddress.setOct(temp, 3, 0);
					} else {
						temp = IPAddress
								.setOct(IPAddress.getBroadcast(temp, mask),
										3,
										IPAddress.getOct(IPAddress
												.getBroadcast(temp, mask), 3) + 1);
						temp = IPAddress.setOct(temp, 4, 0);
					}
				} else { // get broadcast of current calculated network and
					// increment it for further calculations
					temp = IPAddress.setOct(IPAddress.getBroadcast(temp, mask),
							4, IPAddress.getOct(
									IPAddress.getBroadcast(temp, mask), 4) + 1);
				}
			}
		}
		db.close();

	}
};
