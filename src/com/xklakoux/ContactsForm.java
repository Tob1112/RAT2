package com.xklakoux;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ContactsForm extends Activity implements OnClickListener{
	
	int PICK_CONTACT;
	EditText fname;
	EditText sname;
	EditText tel;
	EditText sw;
	EditText port;
	EditText mac;
	Button fromContacts;
	Button search;
	
	String fnameval = new String();
	String snameval = new String();
	String telval = new String();
	String swval = new String();
	String portval = new String();
	String macval = new String();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form);
		
		fname = (EditText) findViewById(R.id.etFirst);
		sname = (EditText) findViewById(R.id.etSec);
		tel = (EditText) findViewById(R.id.etTel);
		sw = (EditText) findViewById(R.id.etSwitch);
		port = (EditText) findViewById(R.id.etPort);
		mac = (EditText) findViewById(R.id.etMac);
		fromContacts = (Button) findViewById(R.id.bChoose);
		search = (Button) findViewById(R.id.bSearch);
		
		fromContacts.setOnClickListener(this);
		search.setOnClickListener(this);
		
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		String phoneNumber = new String();
		if (intent != null) {
			Uri uri = intent.getData();
			Cursor cursor = getApplicationContext().getContentResolver().query(
					uri, null, null, null, null);

			while (cursor.moveToNext()) {
				String contactId = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Contacts._ID));
				String hasPhone = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

				if (Integer.valueOf(hasPhone) == 1) {
					// You know have the number so now query it like this
					Cursor phones = getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + contactId, null, null);
					while (phones.moveToNext()) {

						phoneNumber = phones
								.getString(phones
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					}
					phones.close();

				}
				phoneNumber = phoneNumber.replaceAll("-", "");
				
				if(phoneNumber.startsWith("+48")){
//					phoneNumber = phoneNumber.substring(4);
					//check it!
				}
				Intent i = new Intent("com.xklakoux.SHOWCONTACTDATA");
				i.putExtra("whereClause", DBHelper.COL_TELNUM + " = " + phoneNumber);
				startActivity(i);
			}
		}

	}



	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.bSearch:
			fnameval = (fname.getText().toString().length()==0?"1 = 1":"firstame" + " = " + "\"" + fname.getText().toString() + "\"");
			snameval  = (sname.getText().toString().length()==0?"1 = 1":DBHelper.COL_SECONDN + " = " + "\"" + sname.getText().toString() + "\"");
			telval = (tel.getText().toString().length()==0?"1 = 1":DBHelper.COL_TELNUM + " = " + tel.getText().toString());
			swval = (sw.getText().toString().length()==0?"1 = 1":DBHelper.COL_SWITCHNR + " = " + "\"" + sw.getText().toString() + "\"");
			portval = (port.getText().toString().length()==0?"1 = 1":DBHelper.COL_PORTNR + " = " + port.getText().toString());
			macval  = (mac.getText().toString().length()==0?"1 = 1":DBHelper.COL_MAC + " = " + "\"" + mac.getText().toString() + "\"");
			
			
			
			Intent i = new Intent("com.xklakoux.SHOWCONTACTDATA");
			i.putExtra("whereClause", fnameval + " AND " + snameval + " AND " + telval + " AND " + swval + " AND " + portval  + " AND " + macval);
			startActivity(i);
			break;
		case R.id.bChoose:
			Intent intent = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, PICK_CONTACT);
			break;
			
		}
	}



}
