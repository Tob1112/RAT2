package com.xklakoux;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class Subnet extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subnet);
		
		
		// EditText declarations
        final EditText ip = (EditText) findViewById(R.id.etIP);
        final EditText mask = (EditText) findViewById(R.id.etMask);
        
        
        // TextView declarations
        final TextView start = (TextView) findViewById(R.id.tvStart);
        final TextView end = (TextView) findViewById(R.id.tvEnd);
        final TextView host = (TextView) findViewById(R.id.tvHost);
        final TextView net = (TextView) findViewById(R.id.tvNet);
        final TextView broad = (TextView) findViewById(R.id.tvBroad);
        final TextView wild = (TextView) findViewById(R.id.tvWild);

        ip.setFilters(IPAddress.getIpFilter());
        mask.setFilters(IPAddress.getMaskFilter());
        
        /////////SPINNER/////////
        final Spinner spinner = (Spinner) findViewById(R.id.sBits);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.bits, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
    	class MyOnItemSelectedListener implements OnItemSelectedListener {

    		@Override
    	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    				mask.setText(IPAddress.getDDMask(Integer.valueOf((String) parent.getItemAtPosition(pos))));
    		}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
    	}
    	spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
    	
    	Button cal = (Button) findViewById(R.id.bCal);
    	cal.setOnClickListener(new View.OnClickListener() {

			/** Button that does it all */
			@Override
			public void onClick(View v) {
		        if (!IPAddress.isCorrectIP(ip.getText().toString())) {
		            Toast toast = Toast.makeText(getApplicationContext(), "Invalid IP Address" , Toast.LENGTH_SHORT);
		            toast.show();
		        }else if (!IPAddress.isCorrectMask(mask.getText().toString())) {
		            Toast toast = Toast.makeText(getApplicationContext(), "Invalid Mask" , Toast.LENGTH_SHORT);
		            toast.show();
		        }else{
		        	host.setText(String.valueOf(IPAddress.getUsableHostsCount(mask.getText().toString())));
		        	net.setText(IPAddress.getNetwork(ip.getText().toString(),mask.getText().toString()));
		        	start.setText(IPAddress.getFirstAddress(IPAddress.getNetwork(ip.getText().toString(),mask.getText().toString())));
		        	broad.setText(IPAddress.getBroadcast(ip.getText().toString(),mask.getText().toString()));
		        	end.setText(IPAddress.getLastAddress(ip.getText().toString(), mask.getText().toString()));
		        	wild.setText(IPAddress.getWildcard(mask.getText().toString()));
		        	
		        }
			}
		});
        mask.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus == true){
					spinner.setSelection(23); // Mask 30 chosen by default
				}
			}
		});
	}


}
