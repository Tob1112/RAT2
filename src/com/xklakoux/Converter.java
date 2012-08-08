//:

package com.xklakoux;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/** This activity is a second tab, it converts binary numbers from 0-255 to decimal and reverse */
public class Converter extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.converter);

		
		//GUI elements declaration
		Button one = (Button) findViewById(R.id.bOne);
		Button zero = (Button) findViewById(R.id.bZero);
		Button conv = (Button) findViewById(R.id.bConv);
		ImageButton clear = (ImageButton) findViewById(R.id.bClear);

		final EditText bin = (EditText) findViewById(R.id.etBinary);
		final EditText dec = (EditText) findViewById(R.id.etDecimal);

		InputFilter[] filter = new InputFilter[1];
		filter[0] = new InputFilter() {
			
			/** Filter that cuts numbers greater than 255*/
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				if (end > start) {
					String destTxt = dest.toString();
					String resultingTxt = destTxt.substring(0, dstart)
							+ source.subSequence(start, end)
							+ destTxt.substring(dend);
					if (!resultingTxt.matches("^\\d{1,3}?")) {
						return "";
					} else {
						if (Integer.valueOf(resultingTxt) > 255) {
							return "";
						}
					}
				}
				return null;
			}
		};
		dec.setFilters(filter);
		bin.setEnabled(false);

		one.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				bin.append("1");
				dec.setText(Integer.toString(Integer.parseInt(bin.getText()
						.toString(), 2)));

			}
		});

		/** Converting method if anything is somehow (it shouldn't cause its disabled) typed in */
		bin.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if(bin.getText().toString().length() > 0){
					dec.setText(Integer.toString(Integer.parseInt(bin.getText()
						.toString(), 2)));
				}
				return false;
			}
		});
		
		zero.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				bin.append("0");
				dec.setText(Integer.toString(Integer.parseInt(bin.getText()
						.toString(), 2)));
			}
		});
		
		conv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (dec.getText().length() != 0) {
					bin.setText(Integer.toBinaryString(Integer.valueOf(dec
							.getText().toString())));
				}
			}
		});

		clear.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bin.setText("");
				dec.setText("");
			}
		});

	}

}
