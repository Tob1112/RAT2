package com.xklakoux;

import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MyViewBinder implements SimpleCursorAdapter.ViewBinder {

	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		int viewId = view.getId();
		switch (viewId) { //Is it tv or iv
		case R.id.tvCount:
			TextView noteName = (TextView) view;
			noteName.setText(cursor.getString(columnIndex));
			break;

		case R.id.tvHowMany:
			TextView notName = (TextView) view;
			notName.setText(cursor.getString(columnIndex));
			break;

		case R.id.ivPic:

			ImageView noteTypeIcon = (ImageView) view;

			int noteType = cursor.getInt(columnIndex); //which image to choose
			switch (noteType) {

			case 2:
				noteTypeIcon.setImageResource(R.drawable.p2p); //for point to point
				break;

			default:
				noteTypeIcon.setImageResource(R.drawable.host); // for everything else
				break;
			}

			break;

		}
		return true;
	}
}