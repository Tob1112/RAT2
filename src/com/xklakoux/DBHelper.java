package com.xklakoux;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "subnetcalculator.db";
	public static final int DB_VERSION = 1;

	// Column names for table 1 @see Hosts.java
	public static final String TABLE_NAME = "hosts";
	public static final String COL_PIC = "pic";
	public static final String COL_HOST = "number";
	public static final String COL_HOWMANY = "howmany";

	// Column names for table 2 @see HostsOut.java
	public static final String TABLE_NAME2 = "output";
	public static final String COL_HMANY = "howmany";
	public static final String COL_MASK = "mask";
	public static final String COL_START = "first";
	public static final String COL_END = "last";
	public static final String COL_USABLE = "usable";
	public static final String COL_NET = "network";
	public static final String COL_BROAD = "broadcast";
	public static final String COL_WILD = "wildcard";

	// Column names for table3 @see ShowContactData.java
	public static final String TABLE_NAME3 = "contactsdata";
	public static final String COL_FIRSTN = "firstname";
	public static final String COL_SECONDN = "secname";
	public static final String COL_TELNUM = "_id";
	public static final String COL_STREET = "street";
	public static final String COL_BUILDNR = "buildnum";
	public static final String COL_ROOMNR = "roomnum";
	public static final String COL_SWROOMNR = "swroomnum";
	public static final String COL_CODE = "code";
	public static final String COL_CITY = "city";
	public static final String COL_SWITCHNR = "switchnum";
	public static final String COL_PORTNR = "portnum";
	public static final String COL_MAC = "mac";

	// Additional variable I needed in two activities
	// It's weak solution but it works
	public static String MASK;
	public static String IP;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	/** Create both tables if they aren't there */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = " create table " + TABLE_NAME2
				+ " (_id integer not null primary key autoincrement, "
				+ COL_HMANY + " integer, " + COL_START + " char[15],  "
				+ COL_END + " char[15],  " + COL_USABLE + " integer, "
				+ COL_NET + " char[15], " + COL_BROAD + " char[15], "
				+ COL_WILD + " char[15], " + COL_MASK + " char[19])";
		db.execSQL(sql);
		sql = "create table " + TABLE_NAME3
				+ " ("
				+ COL_FIRSTN + " varchar(20), " + COL_SECONDN
				+ " varchar(20), " + COL_TELNUM + " varchar(13) not null primary key, " + COL_STREET
				+ " varchar(25), " + COL_CODE + " varchar(6), " + COL_CITY
				+ " varchar(20), " + COL_SWROOMNR + " varchar(6), "
				+ COL_BUILDNR + " varchar(6), " + COL_ROOMNR + " varchar(6), "
				+ COL_SWITCHNR + " varchar(6), " + COL_PORTNR + " integer, "
				+ COL_MAC + " varchar(13))";
		db.execSQL(sql);
		sql = "create table " + TABLE_NAME
				+ " (_id integer not null primary key autoincrement, "
				+ COL_PIC + " integer, " + COL_HOST + " integer, "
				+ COL_HOWMANY + " integer )";
		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
