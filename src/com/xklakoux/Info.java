package com.xklakoux;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Info extends ListActivity{

	
	static final String [] categories = new String[]{
		"OSPF","RIP","BGP","EIGRP","ISIS","IPv6","Masks"
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setListAdapter( new ArrayAdapter<String>(this,R.layout.category,categories));
		
		ListView list = getListView();
		list.setTextFilterEnabled(true);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent i = new Intent("com.xklakoux.SHOWINFO");
				i.putExtra("about", categories[position]);
				startActivity(i);
			}
			
		});

	}

}
